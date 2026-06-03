package br.com.fiap.rocketlaunch.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record TelemetryRequest(

        @NotNull(message = "Altitude é obrigatória")
        @PositiveOrZero
        Double altitudeKm,

        @NotNull(message = "Velocidade é obrigatória")
        @PositiveOrZero
        Double velocityKmh,

        @NotNull(message = "Aceleração é obrigatória")
        Double accelerationMs2,

        @NotNull(message = "Nível de combustível é obrigatório")
        @DecimalMin(value = "0.0", message = "Combustível não pode ser negativo")
        @DecimalMax(value = "100.0", message = "Combustível não pode ultrapassar 100%")
        Double fuelLevelPercent,

        @NotNull(message = "Temperatura é obrigatória")
        Double temperatureCelsius
) {}