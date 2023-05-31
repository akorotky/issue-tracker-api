package com.bugtracker.api.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

@RestController
@RequestMapping("api/error")
@RequiredArgsConstructor
public class RestErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    @Value("${debug.mode.enabled:false}")
    private boolean debuggingEnabled;

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> error(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> requestErrorAttributes = getErrorAttributes(request, debuggingEnabled);
        HttpStatus status = HttpStatus.valueOf(response.getStatus());
        if (status == HttpStatus.NO_CONTENT) {
            return new ResponseEntity<>(status);
        }
        return new ResponseEntity<>(requestErrorAttributes, status);
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean debuggingEnabled) {
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
        if (debuggingEnabled) {
            options = options
                    .including(ErrorAttributeOptions.Include.MESSAGE)
                    .including(ErrorAttributeOptions.Include.EXCEPTION)
                    .including(ErrorAttributeOptions.Include.STACK_TRACE)
            ;
        }
        return this.errorAttributes.getErrorAttributes(new ServletWebRequest(request), options);
    }
}
