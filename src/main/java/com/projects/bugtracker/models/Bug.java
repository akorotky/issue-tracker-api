package com.projects.bugtracker.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "bug")
public class Bug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "bug", cascade = CascadeType.REMOVE)
    private Set<BugComment> comments = new HashSet<>();

    public Bug(String title, String description, User author, Project project) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bug bug = (Bug) o;
        return id.equals(bug.id)
                && title.equals(bug.title)
                && description.equals(bug.description)
                && author.equals(bug.author)
                && project.equals(bug.project)
                && comments.equals(bug.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description);
    }
}
