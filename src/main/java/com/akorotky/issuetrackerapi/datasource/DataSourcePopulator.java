package com.akorotky.issuetrackerapi.datasource;

import com.akorotky.issuetrackerapi.dto.project.ProjectRequestDto;
import com.akorotky.issuetrackerapi.dto.user.UserRequestDto;
import com.akorotky.issuetrackerapi.entity.role.Role;
import com.akorotky.issuetrackerapi.entity.role.RoleType;
import com.akorotky.issuetrackerapi.security.principal.UserPrincipal;
import com.akorotky.issuetrackerapi.service.AuthenticationService;
import com.akorotky.issuetrackerapi.service.ProjectService;
import com.akorotky.issuetrackerapi.service.UserService;
import com.akorotky.issuetrackerapi.repository.RoleRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSourcePopulator implements CommandLineRunner {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final ProjectService projectService;
    private final AuthenticationService authenticationService;
    private final ObjectMapper objectMapper;

    private void initRoles() {
        RoleType[] roles = {RoleType.USER, RoleType.ADMIN};

        for (RoleType role : roles) {
            roleRepository.save(new Role(role));
        }
    }

    private void initUsers(List<UserRequestDto> userRequestDtoList) {
        for (UserRequestDto userRequestDto : userRequestDtoList) {
            userService.createUser(userRequestDto);
        }
    }

    private void initProjects(List<ProjectRequestDto> projectRequestDtoList, List<UserRequestDto> userRequestDtoList) {
        for (int i = 0; i < projectRequestDtoList.size() && i < userRequestDtoList.size(); i++) {
            UserRequestDto userRequestDto = userRequestDtoList.get(i);
            // JdbcMutableAclService requires the user to be authenticated in order to work
            Authentication authentication = authenticationService.getAuthenticationFromUsernamePassword(
                    userRequestDto.username(),
                    userRequestDto.password()
            );
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // create projects
            projectService.createProject(projectRequestDtoList.get(i), userPrincipal.user());
            // clear context
            SecurityContextHolder.clearContext();
        }
    }


    @Override
    public void run(String... strings) throws Exception {
        // json data file locations
        String PROJECTS_DATA_LOCATION = "src/main/resources/data/projects.json";
        String USERS_DATA_LOCATION = "src/main/resources/data/users.json";

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
        initUsers(userRequestDtoList);
        initProjects(projectRequestDtoList, userRequestDtoList);
    }
}
