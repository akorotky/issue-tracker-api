package com.bugtracker.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RestController
public class RestErrorController implements ErrorController {

    @RequestMapping(path = "api/error", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> renderError(HttpServletRequest request, HttpServletResponse response) {
        var errorAttributes = getErrorAttributes(new ServletWebRequest(request), true, false, false);
        HttpStatus status = HttpStatus.valueOf(response.getStatus());
        return new ResponseEntity<>(errorAttributes, status);
    }

    private Map<String, Object> getErrorAttributes(WebRequest webRequest,
                                                   boolean includeMessage,
                                                   boolean includeException,
                                                   boolean includeStackTrace) {
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
        if (includeMessage) options = options.including(ErrorAttributeOptions.Include.MESSAGE);
        if (includeException) options = options.including(ErrorAttributeOptions.Include.EXCEPTION);
        if (includeStackTrace) options = options.including(ErrorAttributeOptions.Include.STACK_TRACE);
        return new DefaultErrorAttributes().getErrorAttributes(webRequest, options);
    }
}
