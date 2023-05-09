package com.projects.bugtracker.db;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.bugtracker.dto.ProjectDto;
import com.projects.bugtracker.dto.UserDto;
import com.projects.bugtracker.entities.Role;
import com.projects.bugtracker.enums.RoleType;
import com.projects.bugtracker.entities.User;
import com.projects.bugtracker.exceptions.ResourceNotFoundException;
import com.projects.bugtracker.repositories.RoleRepository;
import com.projects.bugtracker.repositories.UserRepository;
import com.projects.bugtracker.services.ProjectService;
import com.projects.bugtracker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseLoader implements CommandLineRunner {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final ProjectService projectService;
    private final UserRepository userRepository;

    @Override
    public void run(String... strings) throws Exception {
        // json data file locations
        String PROJECTS_DATA_LOCATION = "src/main/resources/data/projects.json";
        String USERS_DATA_LOCATION = "src/main/resources/data/users.json";

        // Jackson object mapper
        ObjectMapper objectMapper = new ObjectMapper();

        // load data from the json files into DTOs
        List<ProjectDto> projectList = objectMapper.readValue(
                new File(PROJECTS_DATA_LOCATION),
                new TypeReference<List<ProjectDto>>() {
                });

        List<UserDto> userList = objectMapper.readValue(
                new File(USERS_DATA_LOCATION),
                new TypeReference<List<UserDto>>() {
                });

        // create roles
        RoleType[] roles = {RoleType.USER, RoleType.ADMIN};

        // save roles to the database
        for (RoleType role : roles) {
            roleRepository.save(new Role(role));
        }

        for (int i = 0; i < userList.size(); i++) {
            // create a new user
            userService.createUser(userList.get(i));

            // load the user from the database
            String username = userList.get(i).username();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

            // create a new project with the selected user as owner
            projectService.createProject(projectList.get(i), user);
        }
    }
}
