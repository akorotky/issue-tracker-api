package com.bugtracker.api.controllers;

import com.bugtracker.api.dto.authdto.AuthenticationRequestDto;
import com.bugtracker.api.dto.authdto.AuthenticationResponseDto;
import com.bugtracker.api.dto.userdto.UserRequestDto;
import com.bugtracker.api.dto.tokendto.AccessTokenRequestDto;
import com.bugtracker.api.dto.tokendto.AccessTokenResponseDto;
import com.bugtracker.api.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> signUp(@RequestBody UserRequestDto userRequestDto) {
        authenticationService.register(userRequestDto);
        URI createdUserLocation = linkTo(methodOn(UserController.class).getUser(userRequestDto.username())).toUri();
        return ResponseEntity.created(createdUserLocation).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> signIn(@RequestBody AuthenticationRequestDto authenticationRequestDto) {
        AuthenticationResponseDto authenticationResponseDto = authenticationService.authenticate((authenticationRequestDto));
        return ResponseEntity.ok(authenticationResponseDto);
    }

    @PostMapping("/token")
    public ResponseEntity<?> refreshAccessToken(@RequestBody AccessTokenRequestDto accessTokenRequestDto) {
        String accessToken = authenticationService.refreshAccessToken(accessTokenRequestDto);
        if (accessToken == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Refresh token is invalid.");
        return ResponseEntity.ok(new AccessTokenResponseDto(accessToken));
    }
}
