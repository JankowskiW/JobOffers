package pl.wj.joboffers.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ExceptionBody (
    String message,
    HttpStatus httpStatus,
    ZonedDateTime timestamp
) {}
