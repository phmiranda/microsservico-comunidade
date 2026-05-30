package br.com.phmiranda.comunidade.config.exception;

import br.com.phmiranda.comunidade.domain.dto.handler.ApiErrorResponse;
import br.com.phmiranda.comunidade.domain.dto.handler.FieldErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
        MethodArgumentNotValidException exception,
        HttpServletRequest request
    ) {
        List<FieldErrorResponse> fields = exception.getBindingResult().getFieldErrors().stream()
            .map(this::toFieldError)
            .collect(Collectors.toList());

        return build(HttpStatus.BAD_REQUEST, "Requisição inválida.", request.getRequestURI(), fields);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
        ResourceNotFoundException exception,
        HttpServletRequest request
    ) {
        return build(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI(), Collections.emptyList());
    }

    @ExceptionHandler({BusinessException.class, IllegalArgumentException.class})
    public ResponseEntity<ApiErrorResponse> handleBusiness(
        RuntimeException exception,
        HttpServletRequest request
    ) {
        return build(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getRequestURI(), Collections.emptyList());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(
        BadCredentialsException exception,
        HttpServletRequest request
    ) {
        return build(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos.", request.getRequestURI(), Collections.emptyList());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(
        AccessDeniedException exception,
        HttpServletRequest request
    ) {
        return build(HttpStatus.FORBIDDEN, "Acesso negado.", request.getRequestURI(), Collections.emptyList());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(
        DataIntegrityViolationException exception,
        HttpServletRequest request
    ) {
        return build(
            HttpStatus.CONFLICT,
            "Operação não permitida porque o recurso possui vínculos ou viola uma restrição de dados.",
            request.getRequestURI(),
            Collections.emptyList()
        );
    }

    private FieldErrorResponse toFieldError(FieldError error) {
        return new FieldErrorResponse(error.getField(), error.getDefaultMessage());
    }

    private ResponseEntity<ApiErrorResponse> build(
        HttpStatus status,
        String message,
        String path,
        List<FieldErrorResponse> fields
    ) {
        ApiErrorResponse response = new ApiErrorResponse(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            path,
            fields
        );
        return ResponseEntity.status(status).body(response);
    }
}
