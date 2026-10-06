package org.mehmetkatr.meridian.common.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleConflict(DataIntegrityViolationException ex) {
        Throwable cause = ex.getMostSpecificCause();

        if (cause instanceof SQLException sqlEx) {
            return switch (sqlEx.getErrorCode()) {
                case 1 -> ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "Benzersiz alan zaten kayitli"));
                case 2291 -> ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(Map.of("message", "Iliskili kayit bulunamadi (gecersiz referans)"));
                case 2292 -> ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "Bu kayda bagli kayitlar oldugu icin silinemiyor"));
                case 1400 -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Zorunlu alan bos birakilamaz"));
                default -> ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "Veri butunlugu ihlali"));
            };
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", "Veri butunlugu ihlali"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }
}
