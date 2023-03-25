package com.projects.bugtracker.BugComment;

import com.projects.bugtracker.Bug.Bug;
import com.projects.bugtracker.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "bug_comment")
public class BugComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "body", nullable = false)
    @NotNull
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
}
