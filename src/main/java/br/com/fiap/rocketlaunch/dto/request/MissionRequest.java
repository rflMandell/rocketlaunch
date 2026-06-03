package br.com.fiap.rocketlaunch.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MissionRequest(

        @NotBlank(message = "Nome é obrigatório")
        String name,

        @NotBlank(message = "Destino é obrigatório")
        String destination,

        String objective
) {}