package br.com.fiap.rocketlaunch.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "weather_conditions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Double windSpeedKmh;

    @NotNull
    @Column(nullable = false)
    private Double temperatureCelsius;

    @NotNull
    @Column(nullable = false)
    private Double visibilityKm;

    @NotNull
    @Column(nullable = false)
    private Double precipitationMm;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime recordedAt;

    @PrePersist
    public void prePersist() {
        this.recordedAt = LocalDateTime.now();
    }

    // vento acima de 65 km/h bloqueia o lançamento
    public boolean isSafeForLaunch() {
        return this.windSpeedKmh <= 65.0
                && this.visibilityKm >= 5.0
                && this.precipitationMm < 10.0;
    }
}