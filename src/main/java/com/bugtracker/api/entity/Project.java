package com.bugtracker.api.entity;

import com.bugtracker.api.entity.audit.AuditMetadata;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "project")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Project extends AuditMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 79432904259497L;

    private Long id;
    private String title;
    private String description;
    private Boolean isPrivate;
    private User owner;
    private Set<Bug> bugs = new HashSet<>();
    private Set<User> collaborators = new HashSet<>();

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    public Long getId() {
        return id;
    }

    @Column(name = "title", nullable = false)
    public String getTitle() {
        return title;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    @Column(name = "private")
    public Boolean getIsPrivate() {
        return isPrivate;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    public User getOwner() {
        return owner;
    }

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Bug> getBugs() {
        return bugs;
    }

    @Transient
    public Set<Bug> getBugsView() {
        return Collections.unmodifiableSet(bugs);
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "project_user",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> getCollaborators() {
        return collaborators;
    }

    @Transient
    public Set<User> getCollaboratorsView() {
        return Collections.unmodifiableSet(collaborators);
    }

    public void addCollaborator(User user) {
        collaborators.add(user);
    }

    public void removeCollaborator(User user) {
        collaborators.remove(user);
    }

}

