package br.com.fiap.rocketlaunch.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record WeatherConditionRequest(

        @NotNull(message = "Velocidade do vento é obrigatória")
        @PositiveOrZero(message = "Velocidade do vento não pode ser negativa")
        Double windSpeedKmh,

        @NotNull(message = "Temperatura é obrigatória")
        Double temperatureCelsius,

        @NotNull(message = "Visibilidade é obrigatória")
        @PositiveOrZero(message = "Visibilidade não pode ser negativa")
        Double visibilityKm,

        @NotNull(message = "Precipitação é obrigatória")
        @PositiveOrZero(message = "Precipitação não pode ser negativa")
        Double precipitationMm
) {}