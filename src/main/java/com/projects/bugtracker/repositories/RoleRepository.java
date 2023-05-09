package com.projects.bugtracker.repositories;

import com.projects.bugtracker.entities.Role;
import com.projects.bugtracker.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findRoleByName(RoleType name);
}
