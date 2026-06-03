package br.com.fiap.rocketlaunch.dto.response;

import br.com.fiap.rocketlaunch.domain.entity.Launch;
import br.com.fiap.rocketlaunch.domain.enums.LaunchStatus;

import java.time.LocalDateTime;

public record LaunchResponse(
        Long id,
        LaunchStatus status,
        LocalDateTime scheduledAt,
        LocalDateTime launchedAt,
        LocalDateTime completedAt,
        Long missionId,
        String missionName,
        Long rocketId,
        String rocketName,
        WeatherConditionResponse weatherCondition
) {
    public static LaunchResponse from(Launch launch) {
        return new LaunchResponse(
                launch.getId(),
                launch.getStatus(),
                launch.getScheduledAt(),
                launch.getLaunchedAt(),
                launch.getCompletedAt(),
                launch.getMission().getId(),
                launch.getMission().getName(),
                launch.getRocket().getId(),
                launch.getRocket().getName(),
                launch.getWeatherCondition() != null
                        ? WeatherConditionResponse.from(launch.getWeatherCondition())
                        : null
        );
    }
}