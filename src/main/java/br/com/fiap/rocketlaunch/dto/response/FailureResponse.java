package br.com.fiap.rocketlaunch.dto.response;

import br.com.fiap.rocketlaunch.domain.entity.Failure;
import br.com.fiap.rocketlaunch.domain.enums.FailureSeverity;

import java.time.LocalDateTime;

public record FailureResponse(
        Long id,
        String component,
        String description,
        FailureSeverity severity,
        boolean isCritical,
        Long launchId,
        LocalDateTime occurredAt
) {
    public static FailureResponse from(Failure failure) {
        return new FailureResponse(
                failure.getId(),
                failure.getComponent(),
                failure.getDescription(),
                failure.getSeverity(),
                failure.isCritical(),
                failure.getLaunch().getId(),
                failure.getOccurredAt()
        );
    }
}