package com.bugtracker.api.rest;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public interface RestModelAssembler<T> extends RepresentationModelAssembler<T, EntityModel<T>> {
}
