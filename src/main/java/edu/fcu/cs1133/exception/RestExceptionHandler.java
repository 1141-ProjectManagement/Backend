package edu.fcu.cs1133.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.HashMap;

@ControllerAdvice // <-- 標示這是一個全局處理器
public class RestExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", System.currentTimeMillis());
    body.put("status", HttpStatus.NOT_FOUND.value());
    body.put("error", "Not Found");
    body.put("message", ex.getMessage());
    body.put("path", request.getDescription(false).substring(4)); // "uri=..." -> "..."

    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<?> handleBadRequestException(BadRequestException ex, WebRequest request) {
    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", System.currentTimeMillis());
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("error", "Bad Request");
    body.put("message", ex.getMessage());
    body.put("path", request.getDescription(false).substring(4));

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", System.currentTimeMillis());
    body.put("status", HttpStatus.FORBIDDEN.value());
    body.put("error", "Forbidden");
    body.put("message", "You do not have permission to access this resource."); // ex.getMessage() 可能會洩漏內部資訊
    body.put("path", request.getDescription(false).substring(4));

    return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
  }
}