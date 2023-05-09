package com.bugtracker.api.repositories;

import com.bugtracker.api.entities.Role;
import com.bugtracker.api.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findRoleByName(RoleType name);
}
