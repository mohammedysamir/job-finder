package com.jobfinder.finder.controller;

import com.jobfinder.finder.exception.ApplyToClosedPositionException;
import com.jobfinder.finder.exception.CloseAClosedPositionException;
import com.jobfinder.finder.exception.PostNoLongerExistsException;
import com.jobfinder.finder.exception.UnauthorizedException;
import com.jobfinder.finder.exception.UserAlreadyExistsException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandlerController {

  @ExceptionHandler(ApplyToClosedPositionException.class)
  public ResponseEntity<String> handleApplyToClosedPositionException(ApplyToClosedPositionException ex) {
    log.error("Apply to closed position exception: {}", ex.getMessage());
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(CloseAClosedPositionException.class)
  public ResponseEntity<String> handleCloseAClosedPositionException(CloseAClosedPositionException ex) {
    log.error("Close a closed position exception: {}", ex.getMessage());
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(PostNoLongerExistsException.class)
  public ResponseEntity<String> handlePostNoLongerExistsException(PostNoLongerExistsException ex) {
    log.error("Post no longer exists exception: {}", ex.getMessage());
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
    log.error("User already exists exception: {}", ex.getMessage());
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
    log.error("Unauthorized exception: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not allowed to perform this action");
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<String> handleValidationException(ValidationException ex) {
    log.error("Validation exception: {}", ex.getMessage());
    return ResponseEntity.badRequest().body("Validation error: " + ex.getMessage());
  }
}
