package com.bugtracker.api.controllers;

import com.bugtracker.api.dto.auth.AuthenticationRequestDto;
import com.bugtracker.api.dto.auth.AuthenticationResponseDto;
import com.bugtracker.api.dto.user.UserRequestDto;
import com.bugtracker.api.dto.token.AccessTokenRequestDto;
import com.bugtracker.api.dto.token.AccessTokenResponseDto;
import com.bugtracker.api.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> signUp(@RequestBody UserRequestDto userRequestDto) {
        authenticationService.registerUser(userRequestDto);
        URI createdUserLocation = linkTo(methodOn(UserController.class).getUser(userRequestDto.username())).toUri();
        return ResponseEntity.created(createdUserLocation).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> signIn(@RequestBody AuthenticationRequestDto authenticationRequestDto) {
        Map<String, String> tokens = new HashMap<>(authenticationService.authenticateUser((authenticationRequestDto)));
        AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto(tokens.get("accessToken"), tokens.get("refreshToken"));
        return ResponseEntity.ok(authenticationResponseDto);
    }

    @PostMapping("/token")
    public ResponseEntity<?> refreshAccessToken(@RequestBody AccessTokenRequestDto accessTokenRequestDto) {
        String accessToken = authenticationService.refreshAccessToken(accessTokenRequestDto);
        if (accessToken == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Refresh token is invalid.");
        return ResponseEntity.ok(new AccessTokenResponseDto(accessToken));
    }
}
