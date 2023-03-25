package com.projects.bugtracker.User;

import java.util.HashSet;
import java.util.Set;

import com.projects.bugtracker.Project.Project;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "user_name", nullable = false)
    @NotNull
    private String username;

    @Column(name = "password", nullable = false)
    @NotNull
    private String password;
    @Column(name = "email", nullable = false)
    @NotNull
    @Email
    private String email;

    @ManyToMany(mappedBy = "collaborators")
    private Set<Project> sharedProjects = new HashSet<>();;

    @OneToMany(mappedBy = "owner")
    private Set<Project> ownedProjects = new HashSet<>();;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

}
