package com.bugtracker.api.entities;

import com.bugtracker.api.entities.audit.AuditMetadata;
import com.bugtracker.api.entities.role.Role;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "\"user\"")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends AuditMetadata implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles = new HashSet<>();

    @Column(name = "account_locked")
    private Boolean accountLocked;

    @Column(name = "account_enabled")
    private Boolean accountEnabled;

    @Column(name = "account_expired")
    private Boolean accountExpired;

    @Column(name = "credentials_expired")
    private Boolean credentialsExpired;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Project> ownedProjects = new HashSet<>();

    @ManyToMany(mappedBy = "collaborators", fetch = FetchType.LAZY)
    private Set<Project> sharedProjects = new HashSet<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Bug> bugs = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id)
                && Objects.equals(username, user.username)
                && Objects.equals(password, user.password)
                && Objects.equals(email, user.email)
                && Objects.equals(roles, user.roles)
                && Objects.equals(accountLocked, user.accountLocked)
                && Objects.equals(accountEnabled, user.accountEnabled)
                && Objects.equals(accountExpired, user.accountExpired)
                && Objects.equals(credentialsExpired, user.credentialsExpired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                username,
                password,
                email,
                roles,
                accountLocked,
                accountEnabled,
                accountExpired,
                credentialsExpired);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", accountLocked=" + accountLocked +
                ", accountEnabled=" + accountEnabled +
                ", accountExpired=" + accountExpired +
                ", credentialsExpired=" + credentialsExpired +
                '}';
    }
}
