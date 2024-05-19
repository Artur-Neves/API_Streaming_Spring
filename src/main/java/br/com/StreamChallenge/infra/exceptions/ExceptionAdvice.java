package br.com.StreamChallenge.infra.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
        return ResponseEntity.badRequest().body(erros.stream().map(errorFormatation::new));
    }
    @ExceptionHandler(SQLException.class)
    ResponseEntity<?> erroNoSQL (SQLException e){
        return ResponseEntity.badRequest().body(new errorFormatation("SQLException", e.getMessage()));
    }


    public record errorFormatation(
            String field,
            String message
    ) {
        public errorFormatation(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
