package com.projects.bugtracker.controllers;

import com.projects.bugtracker.dto.*;
import com.projects.bugtracker.services.AuthenticationService;
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

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signUp(@RequestBody UserDto userDto){
        authenticationService.register(userDto);
        URI createdUserLocation = linkTo(methodOn(UserController.class).getUser(userDto.username())).toUri();
        return ResponseEntity.created(createdUserLocation).build();
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signIn(@RequestBody AuthenticationRequest authenticationRequest){
        AuthenticationResponse authenticationResponse = authenticationService.authenticate((authenticationRequest));
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/token")
    public ResponseEntity<?> refreshAccessToken(@RequestBody AccessTokenRequest accessTokenRequest){
        String accessToken = authenticationService.refreshAccessToken(accessTokenRequest);
        if(accessToken == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Refresh token is invalid.");
        return ResponseEntity.ok(new AccessTokenResponse(accessToken));
    }

}
