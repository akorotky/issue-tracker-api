package com.projects.bugtracker.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public interface ModelAssembler<T> extends RepresentationModelAssembler<T, EntityModel<T>> {
}