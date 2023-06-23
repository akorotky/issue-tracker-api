package com.bugtracker.api.repository;

import com.bugtracker.api.entity.role.Role;
import com.bugtracker.api.entity.role.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findRoleByName(RoleType name);
}
