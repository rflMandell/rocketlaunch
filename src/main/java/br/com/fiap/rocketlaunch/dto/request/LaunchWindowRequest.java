package br.com.fiap.rocketlaunch.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record LaunchWindowRequest(

        @NotNull(message = "Início da janela é obrigatório")
        @Future(message = "Início deve ser no futuro")
        LocalDateTime windowStart,

        @NotNull(message = "Fim da janela é obrigatório")
        @Future(message = "Fim deve ser no futuro")
        LocalDateTime windowEnd,

        String notes
) {}