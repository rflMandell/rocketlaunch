package br.com.fiap.rocketlaunch.dto.response;

import br.com.fiap.rocketlaunch.domain.entity.Mission;
import br.com.fiap.rocketlaunch.domain.enums.MissionStatus;

import java.time.LocalDateTime;

public record MissionResponse(
        Long id,
        String name,
        String destination,
        String objective,
        MissionStatus status,
        LocalDateTime createdAt
) {
    public static MissionResponse from(Mission mission) {
        return new MissionResponse(
                mission.getId(),
                mission.getName(),
                mission.getDestination(),
                mission.getObjective(),
                mission.getStatus(),
                mission.getCreatedAt()
        );
    }
}