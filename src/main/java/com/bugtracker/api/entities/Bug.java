package com.bugtracker.api.entities;

import com.bugtracker.api.entities.audit.AuditMetadata;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedBy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bug")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Bug extends AuditMetadata {

    private Long id;
    private String title;
    private String description;
    private User author;
    private Project project;
    private Long modifiedBy;
    private Set<BugComment> comments = new HashSet<>();

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    public User getAuthor() {
        return author;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false, updatable = false)
    public Project getProject() {
        return project;
    }

    @LastModifiedBy
    @Column(name = "modified_by")
    public Long getModifiedBy() {
        return modifiedBy;
    }

    @OneToMany(mappedBy = "bug", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<BugComment> getComments() {
        return comments;
    }

    @Transient
    public Set<BugComment> getCommentsView() {
        return Collections.unmodifiableSet(comments);
    }

}
