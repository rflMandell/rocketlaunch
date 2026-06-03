package br.com.fiap.rocketlaunch.dto.response;

import br.com.fiap.rocketlaunch.domain.entity.Rocket;
import br.com.fiap.rocketlaunch.domain.enums.RocketStatus;

import java.time.LocalDateTime;

public record RocketResponse(
        Long id,
        String name,
        String manufacturer,
        RocketStatus status,
        RocketSpecResponse spec,
        LocalDateTime createdAt
) {
    public static RocketResponse from(Rocket rocket) {
        return new RocketResponse(
                rocket.getId(),
                rocket.getName(),
                rocket.getManufacturer(),
                rocket.getStatus(),
                RocketSpecResponse.from(rocket.getSpec()),
                rocket.getCreatedAt()
        );
    }
}