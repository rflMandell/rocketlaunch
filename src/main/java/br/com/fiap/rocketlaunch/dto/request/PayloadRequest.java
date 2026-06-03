package br.com.fiap.rocketlaunch.dto.request;

import br.com.fiap.rocketlaunch.domain.enums.PayloadType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PayloadRequest(

        @NotBlank(message = "Nome é obrigatório")
        String name,

        @NotNull(message = "Tipo é obrigatório")
        PayloadType type,

        @NotNull(message = "Peso é obrigatório")
        @Positive(message = "Peso deve ser positivo")
        Double weightKg,

        String description,

        @NotNull(message = "ID do foguete é obrigatório")
        Long rocketId
) {}