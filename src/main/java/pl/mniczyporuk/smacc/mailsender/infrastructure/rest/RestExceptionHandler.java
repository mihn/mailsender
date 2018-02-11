package pl.mniczyporuk.smacc.mailsender.infrastructure.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.mniczyporuk.smacc.mailsender.emailsender.domain.exceptions.EmailWasNotSent;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@Slf4j
class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailWasNotSent.class)
    ResponseEntity<Object> handleEmailWasNotSentException(EmailWasNotSent ex, WebRequest request) {
        return handleExceptionInternal(ex, BAD_GATEWAY, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return handleExceptionInternal(ex, BAD_REQUEST, request);
    }


    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             HttpStatus status,
                                                             WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             Object body,
                                                             HttpHeaders headers,
                                                             HttpStatus status,
                                                             WebRequest request) {
        log.error("Handling exception handling request", ex);
        super.handleExceptionInternal(ex, body, headers, status, request);
        return createErrorMessage(ex.getMessage(), headers, status);
    }

    private ResponseEntity createErrorMessage(String message, HttpHeaders headers, HttpStatus status) {
        return new ResponseEntity(new ErrorMessage(message, status.value()), headers, status);
    }
}


@AllArgsConstructor
@Getter
class ErrorMessage {
    private final String message;
    private final int httpStatus;
}