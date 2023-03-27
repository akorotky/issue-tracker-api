package com.projects.bugtracker.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "bug_comment")
public class BugComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank
    @Column(name = "body", nullable = false)
    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "bug_id")
    private Bug bug;

    public BugComment(String body, User author, Bug bug) {
        this.body = body;
        this.author = author;
        this.bug = bug;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BugComment that = (BugComment) o;
        return id.equals(that.id)
                && body.equals(that.body)
                && author.equals(that.author)
                && bug.equals(that.bug);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, body);
    }
}
