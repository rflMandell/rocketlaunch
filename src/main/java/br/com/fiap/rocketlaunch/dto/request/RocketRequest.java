package br.com.fiap.rocketlaunch.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RocketRequest(

        @NotBlank(message = "Nome é obrigatório")
        String name,

        @NotBlank(message = "Fabricante é obrigatório")
        String manufacturer,

        @NotNull(message = "Empuxo é obrigatório")
        @Positive(message = "Empuxo deve ser positivo")
        Double thrustKN,

        @NotNull(message = "Capacidade máxima de carga é obrigatória")
        @Positive(message = "Capacidade deve ser positiva")
        Double maxPayloadKg,

        @NotNull(message = "Altura é obrigatória")
        @Positive(message = "Altura deve ser positiva")
        Double heightMeters,

        @NotNull(message = "Número de estágios é obrigatório")
        @Positive(message = "Estágios deve ser positivo")
        Integer stages,

        @NotNull(message = "Capacidade de combustível é obrigatória")
        @Positive(message = "Capacidade de combustível deve ser positiva")
        Double fuelCapacityLiters
) {}