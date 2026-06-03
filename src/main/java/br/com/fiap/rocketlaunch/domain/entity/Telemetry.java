package br.com.fiap.rocketlaunch.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "telemetries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Telemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Double altitudeKm;

    @NotNull
    @Column(nullable = false)
    private Double velocityKmh;

    @NotNull
    @Column(nullable = false)
    private Double accelerationMs2;

    @NotNull
    @Column(nullable = false)
    private Double fuelLevelPercent;

    @NotNull
    @Column(nullable = false)
    private Double temperatureCelsius;

    @Column(nullable = false, updatable = false)
    private LocalDateTime recordedAt;

    // Muitas telemetrias pertencem a um lançamento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "launch_id", nullable = false)
    private Launch launch;

    @PrePersist
    public void prePersist() {
        this.recordedAt = LocalDateTime.now();
    }
}