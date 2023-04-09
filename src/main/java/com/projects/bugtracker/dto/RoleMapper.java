package com.projects.bugtracker.dto;

import com.projects.bugtracker.entities.Role;

public class RoleMapper {

    public static RoleDto toDto(Role role){
        if (role == null) return null;
        return new RoleDto(role.getId(), role.getName());
    }

    public static Role toRole(RoleDto roleDto){
        if(roleDto == null) return  null;
        return Role.builder()
                .name(roleDto.name())
                .build();
    }
}
