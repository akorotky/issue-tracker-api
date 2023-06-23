package com.bugtracker.api.security.acl;

import com.bugtracker.api.entity.role.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class AclConfig {

    private final DataSource dataSource;
    private final JedisConnectionFactory jedisConnectionFactory;
    private final String ACL_CACHE = "aclCache";

    @Bean
    public AclCache aclCache() {
        AclCache aclCache = new SpringCacheBasedAclCache(
                redisAclCache(),
                permissionGrantingStrategy(),
                aclAuthorizationStrategy()
        );
        // must clear cache before using,
        aclCache.clearCache();
        return aclCache;
    }

    @Bean
    public Cache redisAclCache() {
        return aclCacheManager().getCache(ACL_CACHE);
    }

    @Bean
    public CacheManager aclCacheManager() {
        int ACL_TTL = 1; // cache time to live
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofHours(ACL_TTL))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.java()));

        return RedisCacheManager
                .builder(jedisConnectionFactory)
                .withCacheConfiguration(ACL_CACHE, redisCacheConfiguration)
                .build();
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority(RoleType.USER.name()));
    }

    @Bean
    public LookupStrategy lookupStrategy() {
        return new BasicLookupStrategy(dataSource, aclCache(), aclAuthorizationStrategy(), new ConsoleAuditLogger());
    }

    @Bean
    public JdbcMutableAclService aclService() {
        JdbcMutableAclService jdbcMutableAclService = new JdbcMutableAclService(dataSource, lookupStrategy(), aclCache());

        /*
         * Postgres ACL setup
         * For more information see
         * https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/acls/jdbc/JdbcMutableAclService.html
         * https://docs.spring.io/spring-security/reference/servlet/appendix/database-schema.html#_postgresql
         */
        String POSTGRES_CLASS_IDENTITY_QUERY = "select currval(pg_get_serial_sequence('acl_class', 'id'))";
        String POSTGRES_SID_IDENTITY_QUERY = "select currval(pg_get_serial_sequence('acl_sid', 'id'))";
        jdbcMutableAclService.setClassIdentityQuery(POSTGRES_CLASS_IDENTITY_QUERY);
        jdbcMutableAclService.setSidIdentityQuery(POSTGRES_SID_IDENTITY_QUERY);

        return jdbcMutableAclService;
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new AclPermissionEvaluator(aclService()));
        return expressionHandler;
    }
}
