package com.projects.bugtracker.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class RegistrationController {

    @GetMapping("api/registration")
    public RepresentationModel<?> register() {

        return new RepresentationModel<>().add(
                linkTo(methodOn(UserController.class).getUsersPage(Pageable.unpaged())).withRel("users"),
                linkTo(methodOn(RegistrationController.class).register()).withSelfRel());
    }
}
