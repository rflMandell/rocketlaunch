package br.com.fiap.rocketlaunch.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        int status,
        String error,
        String message,
        LocalDateTime timestamp,
        List<FieldError> fields   // só aparece em erros de validação
) {
    // Construtor para erros sem lista de campos
    public ErrorResponse(int status, String error, String message) {
        this(status, error, message, LocalDateTime.now(), null);
    }

    // Construtor para erros de validação (com lista de campos)
    public ErrorResponse(int status, String error, String message,
                         List<FieldError> fields) {
        this(status, error, message, LocalDateTime.now(), fields);
    }

    // Registro interno que representa um campo inválido
    public record FieldError(String field, String message) {}
}