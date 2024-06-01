package br.com.StreamChallenge.infra.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<?> invalidId(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.badRequest().body("Id inválido");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<?> invalidId(EntityNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> invalidId(MethodArgumentNotValidException e) {
        var erros = e.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(ErrorFormatation::new));
    }
    @ExceptionHandler(SQLException.class)
    ResponseEntity<?> erroNoSQL (SQLException e){
        return ResponseEntity.badRequest().body(new ErrorFormatation("SQLException", e.getMessage()));
    }
    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<?> erroCredenciais(Exception e){
        return ResponseEntity.badRequest().body(new ErrorFormatation("Login", "Credenciais inválidas"));
    }
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<?> acessDeined(Exception e){
        return ResponseEntity.status(403).body(new ErrorFormatation("error", e.getMessage()));
    }


    public record ErrorFormatation(
            String field,
            String message
    ) {
        public ErrorFormatation(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
