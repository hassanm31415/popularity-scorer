package me.redcare.popularity.scorer.exception;

import jakarta.validation.ConstraintViolationException;
import me.redcare.popularity.scorer.exception.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Global exception handler for REST controllers.
 *
 * <p>This class uses {@code @RestControllerAdvice} to intercept exceptions thrown
 * during controller execution and convert them into well-structured HTTP responses.</p>
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles exceptions related to GitHub API communication.
     *
     * @param ex the GitHub API exception
     * @return a detailed error response with HTTP 502 Bad Gateway
     */
    @ExceptionHandler(GitHubApiException.class)
    public Mono<ResponseEntity<ErrorResponse>>  handleGitHubError(GitHubApiException ex) {
        logger.error("GitHub API exception occurred: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(
                "GitHub API error",
                ex.getMessage(),
                HttpStatus.BAD_GATEWAY.value(),
                LocalDateTime.now()
        );
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error));

    }


    /**
     * Handles exceptions related to RequestParams Validations.
     *
     * @param ex the GitHub API exception
     * @return a detailed error response with HTTP 400 Bad Request
     */
    @ExceptionHandler({WebExchangeBindException.class, ConstraintViolationException.class})
    public Mono<ResponseEntity<ErrorResponse>> handleConstraintViolation(Exception ex) {
        String message;

        if (ex instanceof WebExchangeBindException webEx) {
            message = webEx.getBindingResult().getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("; "));
        } else if (ex instanceof ConstraintViolationException cve) {
            message = cve.getConstraintViolations().stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining("; "));
        } else {
            message = ex.getMessage();
        }
        logger.warn("Validation failed: {}", message);

        ErrorResponse error = new ErrorResponse(
                "Validation failed",
                message,
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }

        /**
         * Handles all uncaught exceptions in the application.
         *
         * @param ex the exception thrown during request processing
         * @return a detailed error response with HTTP 500 Internal Server Error
         */
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>>  handleGeneralError(Exception ex) {
        logger.error("An exception occurred: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(
                "Internal server error",
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
    }

}
