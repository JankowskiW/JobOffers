package pl.wj.joboffers.exception.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.wj.joboffers.exception.body.ExceptionBody;
import pl.wj.joboffers.exception.exception.ResourceNotFoundException;

import java.time.ZonedDateTime;

@RestControllerAdvice
@Log4j2
public class ControllerExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBody handleResourceNotFoundException(ResourceNotFoundException e) {
        ExceptionBody response = new ExceptionBody(e.getMessage(), HttpStatus.NOT_FOUND, ZonedDateTime.now());
        log.error(String.format("[%s]: %s", response.httpStatus(), response.message()));
        return response;
    }

}
