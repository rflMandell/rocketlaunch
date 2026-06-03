package br.com.fiap.rocketlaunch.dto.response;

import br.com.fiap.rocketlaunch.domain.entity.Telemetry;

import java.time.LocalDateTime;

public record TelemetryResponse(
        Long id,
        Double altitudeKm,
        Double velocityKmh,
        Double accelerationMs2,
        Double fuelLevelPercent,
        Double temperatureCelsius,
        Long launchId,
        LocalDateTime recordedAt
) {
    public static TelemetryResponse from(Telemetry telemetry) {
        return new TelemetryResponse(
                telemetry.getId(),
                telemetry.getAltitudeKm(),
                telemetry.getVelocityKmh(),
                telemetry.getAccelerationMs2(),
                telemetry.getFuelLevelPercent(),
                telemetry.getTemperatureCelsius(),
                telemetry.getLaunch().getId(),
                telemetry.getRecordedAt()
        );
    }
}