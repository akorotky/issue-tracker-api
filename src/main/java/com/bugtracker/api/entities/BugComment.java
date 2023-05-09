package com.bugtracker.api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "bug_comment")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BugComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "body", nullable = false)
    private String body;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", updatable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bug_id", updatable = false)
    private Bug bug;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BugComment that = (BugComment) o;
        return Objects.equals(id, that.id)
                && Objects.equals(body, that.body)
                && Objects.equals(author, that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, body, author);
    }

    @Override
    public String toString() {
        return "BugComment{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", author=" + author +
                '}';
    }
}
