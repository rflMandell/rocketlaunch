package br.com.fiap.rocketlaunch.dto.response;

import br.com.fiap.rocketlaunch.domain.embedded.RocketSpec;

public record RocketSpecResponse(
        Double thrustKN,
        Double maxPayloadKg,
        Double heightMeters,
        Integer stages,
        Double fuelCapacityLiters
) {
    public static RocketSpecResponse from(RocketSpec spec) {
        return new RocketSpecResponse(
                spec.getThrustKN(),
                spec.getMaxPayloadKg(),
                spec.getHeightMeters(),
                spec.getStages(),
                spec.getFuelCapacityLiters()
        );
    }
}