package com.bugtracker.api.repositories;

import com.bugtracker.api.entities.role.Role;
import com.bugtracker.api.entities.role.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findRoleByName(RoleType name);
}
