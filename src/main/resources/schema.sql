DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS project CASCADE;
DROP TABLE IF EXISTS project_user CASCADE;
DROP TABLE IF EXISTS bug CASCADE;
DROP TABLE IF EXISTS bug_comment CASCADE;
DROP TABLE IF EXISTS role CASCADE;
DROP TABLE IF EXISTS user_role CASCADE;

CREATE TABLE "user" (
    id BIGSERIAL,
    username varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    account_locked BOOLEAN,
    account_enabled BOOLEAN,
    account_expired BOOLEAN,
    credentials_expired BOOLEAN,
    created_date TIMESTAMP NOT NULL,
    modified_date TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE project (
    id BIGSERIAL,
    title varchar(255) NOT NULL,
    description TEXT,
    private BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL,
    created_date TIMESTAMP NOT NULL,
    modified_date TIMESTAMP NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_project FOREIGN KEY (user_id) REFERENCES "user"(id)
);

CREATE TABLE project_user (
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "user"(id),
    FOREIGN KEY (project_id) REFERENCES project(id),
    PRIMARY KEY (project_id, user_id),
    CONSTRAINT fk_project_project_user FOREIGN KEY (project_id) REFERENCES project(id),
    CONSTRAINT fk_user_project_user FOREIGN KEY (user_id) REFERENCES "user"(id)
);

CREATE TABLE bug (
    id BIGSERIAL,
    title varchar(255) NOT NULL,
    description TEXT,
    user_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    created_date TIMESTAMP NOT NULL,
    modified_date TIMESTAMP NOT NULL,
    modified_by BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_bug FOREIGN KEY (user_id) REFERENCES "user"(id),
    CONSTRAINT fk_project_bug FOREIGN KEY (project_id) REFERENCES project(id)
);

CREATE TABLE bug_comment (
    id BIGSERIAL,
    body TEXT NOT NULL,
    user_id BIGINT NOT NULL,
    bug_id BIGINT NOT NULL,
    created_date TIMESTAMP NOT NULL,
    modified_date TIMESTAMP NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_bug_comment FOREIGN KEY (user_id) REFERENCES "user"(id),
    CONSTRAINT fk_bug_bug_comment FOREIGN KEY (bug_id) REFERENCES bug(id)
);

CREATE TABLE role (
    id BIGSERIAL,
    name varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE user_role (
    id BIGSERIAL,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "user"(id),
    FOREIGN KEY (role_id) REFERENCES role(id),
    PRIMARY KEY (role_id, user_id),
    CONSTRAINT fk_user_user_role FOREIGN KEY (user_id) REFERENCES "user"(id),
    CONSTRAINT fk_role_user_role FOREIGN KEY (role_id) REFERENCES role(id)
);

--Postgres ACL tables
--Source: https://docs.spring.io/spring-security/reference/servlet/appendix/database-schema.html#_postgresql
DROP TABLE IF EXISTS acl_sid CASCADE;
DROP TABLE IF EXISTS acl_class CASCADE;
DROP TABLE IF EXISTS acl_entry CASCADE;
DROP TABLE IF EXISTS acl_object_identity CASCADE;

create table acl_sid(
	id bigserial not null primary key,
	principal boolean not null,
	sid varchar(100) not null,
	constraint unique_uk_1 unique(sid,principal)
);

create table acl_class(
	id bigserial not null primary key,
	class varchar(100) not null,
	constraint unique_uk_2 unique(class)
);

create table acl_object_identity(
	id bigserial primary key,
	object_id_class bigint not null,
	object_id_identity varchar(36) not null,
	parent_object bigint,
	owner_sid bigint,
	entries_inheriting boolean not null,
	constraint unique_uk_3 unique(object_id_class,object_id_identity),
	constraint foreign_fk_1 foreign key(parent_object)references acl_object_identity(id),
	constraint foreign_fk_2 foreign key(object_id_class)references acl_class(id),
	constraint foreign_fk_3 foreign key(owner_sid)references acl_sid(id)
);

create table acl_entry(
	id bigserial primary key,
	acl_object_identity bigint not null,
	ace_order int not null,
	sid bigint not null,
	mask integer not null,
	granting boolean not null,
	audit_success boolean not null,
	audit_failure boolean not null,
	constraint unique_uk_4 unique(acl_object_identity,ace_order),
	constraint foreign_fk_4 foreign key(acl_object_identity) references acl_object_identity(id),
	constraint foreign_fk_5 foreign key(sid) references acl_sid(id)
);
