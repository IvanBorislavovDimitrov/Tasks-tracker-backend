package com.tracker.taskstracker.handler;

import com.tracker.taskstracker.model.response.ApiErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class GenericExceptionHandler {

    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<ApiErrorResponse> handle(ResponseStatusException exception) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(exception.getStatus(), exception.getReason());
        return ResponseEntity.badRequest()
                .body(apiErrorResponse);
    }
}
