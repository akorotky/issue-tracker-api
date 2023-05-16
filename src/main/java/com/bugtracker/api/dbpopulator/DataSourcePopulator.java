package com.bugtracker.api.dbpopulator;

import com.bugtracker.api.dto.projectdto.ProjectRequestDto;
import com.bugtracker.api.dto.userdto.UserRequestDto;
import com.bugtracker.api.entities.role.Role;
import com.bugtracker.api.entities.User;
import com.bugtracker.api.entities.role.RoleType;
import com.bugtracker.api.repositories.RoleRepository;
import com.bugtracker.api.services.ProjectService;
import com.bugtracker.api.services.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSourcePopulator implements CommandLineRunner {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final ProjectService projectService;

    private void initRoles() {
        RoleType[] roles = {RoleType.USER, RoleType.ADMIN};

        for (RoleType role : roles) {
            roleRepository.save(new Role(role));
        }
    }

    private List<User> initUsers(List<UserRequestDto> userRequestDtoList) {
        List<User> users = new ArrayList<>();
        for (UserRequestDto userRequestDto : userRequestDtoList) {
            users.add(userService.createUser(userRequestDto));
        }
        return users;
    }

    private void initProjects(List<ProjectRequestDto> projectRequestDtoList, List<User> userList) {
        for (int i = 0; i < projectRequestDtoList.size() && i < userList.size(); i++) {
            projectService.createProject(projectRequestDtoList.get(i), userList.get(i));
        }
    }

    @Override
    public void run(String... strings) throws Exception {
        // json data file locations
        String PROJECTS_DATA_LOCATION = "src/main/resources/data/projects.json";
        String USERS_DATA_LOCATION = "src/main/resources/data/users.json";

        // initialize Jackson object mapper
        ObjectMapper objectMapper = new ObjectMapper();

        // load data from the json files into DTOs
        List<UserRequestDto> userRequestDtoList = objectMapper.readValue(
                new File(USERS_DATA_LOCATION),
                new TypeReference<List<UserRequestDto>>() {
                });

        List<ProjectRequestDto> projectRequestDtoList = objectMapper.readValue(
                new File(PROJECTS_DATA_LOCATION),
                new TypeReference<List<ProjectRequestDto>>() {
                });

        initRoles();
        initProjects(projectRequestDtoList, initUsers(userRequestDtoList));
    }
}
