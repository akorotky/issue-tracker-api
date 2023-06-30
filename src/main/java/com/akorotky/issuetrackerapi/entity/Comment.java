package com.akorotky.issuetrackerapi.entity;

import com.akorotky.issuetrackerapi.entity.audit.AuditMetadata;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "comment")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends AuditMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 874032472355232L;

    private Long id;
    private String body;
    private User author;
    private Issue issue;

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    public Long getId() {
        return id;
    }

    @Column(name = "body", nullable = false)
    public String getBody() {
        return body;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", updatable = false)
    public User getAuthor() {
        return author;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id", updatable = false)
    public Issue getIssue() {
        return issue;
    }

}
