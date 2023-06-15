package com.bugtracker.api.entities.role;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
public class Role {

    private Long id;
    private RoleType name;

    public Role(RoleType name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    public Long getId() {
        return id;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    public RoleType getName() {
        return name;
    }

}
