package com.projects.bugtracker.db;

import com.projects.bugtracker.dto.UserDto;
import com.projects.bugtracker.entities.Role;
import com.projects.bugtracker.constants.RoleType;
import com.projects.bugtracker.repositories.RoleRepository;
import com.projects.bugtracker.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@AllArgsConstructor
public class DatabaseLoader implements CommandLineRunner {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... strings) throws Exception {
        String[] usernames = {"user", "lex", "bob", "tom", "smith", "jack", "john", "jim"};
        String[] passwords = {"password", "lex12345", "bob12345", "tom12345", "smith12345", "jack12345", "john12345", "jim12345"};
        String[] emails = {"user@bugtracker.test", "lex@bugtracker.test", "bob@bugtracker.test", "tom@bugtracker.test",
                "smith@bugtracker.test", "jack@bugtracker.test", "john@bugtracker.test", "jim@bugtracker.test"};

        RoleType[] roles = {RoleType.USER, RoleType.ADMIN};

        for (RoleType role : roles) {
            roleRepository.save(new Role(role));
        }

        for (int i = 0; i < usernames.length; i++) {
            UserDto user = new UserDto(null, usernames[i], passwords[i], emails[i], new HashSet<>());

            userService.createUser(user);
        }
    }
}
