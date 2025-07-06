package me.redcare.popularity.scorer.exception.dto;

import java.time.LocalDateTime;

public record ErrorResponse(String reason, String message, int status, LocalDateTime timestamp) {
}
