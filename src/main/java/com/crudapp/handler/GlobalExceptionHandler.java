    package com.crudapp.handler;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.BadCredentialsException;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.validation.FieldError;
    import org.springframework.web.bind.MethodArgumentNotValidException;
    import org.springframework.web.bind.annotation.ControllerAdvice;
    import org.springframework.web.bind.annotation.ExceptionHandler;
    import org.springframework.web.server.ResponseStatusException;

    import java.util.HashMap;
    import java.util.Map;

    @ControllerAdvice
    public class GlobalExceptionHandler {

        // Handle validation exceptions
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        // Handle authentication failures
        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(UsernameNotFoundException.class)
        public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }

        // Handle ResponseStatusException (added)
        @ExceptionHandler(ResponseStatusException.class)
        public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
            return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
        }

        // Generic exception handler
        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleGeneralException(Exception ex) {
            return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


