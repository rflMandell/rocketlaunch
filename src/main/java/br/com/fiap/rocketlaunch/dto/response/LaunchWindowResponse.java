package br.com.fiap.rocketlaunch.dto.response;

import br.com.fiap.rocketlaunch.domain.entity.LaunchWindow;
import br.com.fiap.rocketlaunch.domain.enums.WindowStatus;

import java.time.LocalDateTime;

public record LaunchWindowResponse(
        Long id,
        LocalDateTime windowStart,
        LocalDateTime windowEnd,
        WindowStatus status,
        String notes,
        boolean isActive
) {
    public static LaunchWindowResponse from(LaunchWindow window) {
        return new LaunchWindowResponse(
                window.getId(),
                window.getWindowStart(),
                window.getWindowEnd(),
                window.getStatus(),
                window.getNotes(),
                window.isActive()
        );
    }
}