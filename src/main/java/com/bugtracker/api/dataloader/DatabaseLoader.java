package com.bugtracker.api.dataloader;

import com.bugtracker.api.dto.projectdto.ProjectRequestDto;
import com.bugtracker.api.dto.userdto.UserRequestDto;
import com.bugtracker.api.entities.role.Role;
import com.bugtracker.api.entities.User;
import com.bugtracker.api.entities.role.RoleType;
import com.bugtracker.api.exceptions.ResourceNotFoundException;
import com.bugtracker.api.repositories.RoleRepository;
import com.bugtracker.api.repositories.UserRepository;
import com.bugtracker.api.services.ProjectService;
import com.bugtracker.api.services.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        List<ProjectRequestDto> projectList = objectMapper.readValue(
                new File(PROJECTS_DATA_LOCATION),
                new TypeReference<List<ProjectRequestDto>>() {
                });

        List<UserRequestDto> userList = objectMapper.readValue(
                new File(USERS_DATA_LOCATION),
                new TypeReference<List<UserRequestDto>>() {
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
