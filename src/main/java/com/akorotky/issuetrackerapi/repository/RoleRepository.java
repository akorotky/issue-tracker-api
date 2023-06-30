package com.akorotky.issuetrackerapi.repository;

import com.akorotky.issuetrackerapi.entity.role.Role;
import com.akorotky.issuetrackerapi.entity.role.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findRoleByName(RoleType name);
}
