package br.com.fiap.rocketlaunch.dto.request;

import jakarta.validation.constraints.NotNull;

public record LaunchRequest(

        @NotNull(message = "ID da missão é obrigatório")
        Long missionId,

        @NotNull(message = "ID do foguete é obrigatório")
        Long rocketId,

        @NotNull(message = "Condição climática é obrigatória")
        WeatherConditionRequest weatherCondition
) {}