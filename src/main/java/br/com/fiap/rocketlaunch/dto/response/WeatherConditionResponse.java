package br.com.fiap.rocketlaunch.dto.response;

import br.com.fiap.rocketlaunch.domain.entity.WeatherCondition;

import java.time.LocalDateTime;

public record WeatherConditionResponse(
        Long id,
        Double windSpeedKmh,
        Double temperatureCelsius,
        Double visibilityKm,
        Double precipitationMm,
        boolean safeForLaunch,
        LocalDateTime recordedAt
) {
    public static WeatherConditionResponse from(WeatherCondition weather) {
        return new WeatherConditionResponse(
                weather.getId(),
                weather.getWindSpeedKmh(),
                weather.getTemperatureCelsius(),
                weather.getVisibilityKm(),
                weather.getPrecipitationMm(),
                weather.isSafeForLaunch(),
                weather.getRecordedAt()
        );
    }
}