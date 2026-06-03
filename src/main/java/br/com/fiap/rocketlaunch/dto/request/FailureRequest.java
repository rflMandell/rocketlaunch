package br.com.fiap.rocketlaunch.dto.request;

import br.com.fiap.rocketlaunch.domain.enums.FailureSeverity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FailureRequest(

        @NotBlank(message = "Componente é obrigatório")
        String component,

        @NotBlank(message = "Descrição é obrigatória")
        String description,

        @NotNull(message = "Severidade é obrigatória")
        FailureSeverity severity
) {}