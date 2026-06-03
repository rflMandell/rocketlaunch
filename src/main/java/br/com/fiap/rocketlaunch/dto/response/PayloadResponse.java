package br.com.fiap.rocketlaunch.dto.response;

import br.com.fiap.rocketlaunch.domain.entity.Payload;
import br.com.fiap.rocketlaunch.domain.enums.PayloadType;

public record PayloadResponse(
        Long id,
        String name,
        PayloadType type,
        Double weightKg,
        String description,
        Long rocketId,
        String rocketName
) {
    public static PayloadResponse from(Payload payload) {
        return new PayloadResponse(
                payload.getId(),
                payload.getName(),
                payload.getType(),
                payload.getWeightKg(),
                payload.getDescription(),
                payload.getRocket().getId(),
                payload.getRocket().getName()
        );
    }
}