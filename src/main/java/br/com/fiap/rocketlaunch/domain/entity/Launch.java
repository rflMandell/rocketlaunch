package br.com.fiap.rocketlaunch.domain.entity;

import br.com.fiap.rocketlaunch.domain.enums.LaunchStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "launches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Launch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LaunchStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime scheduledAt;

    private LocalDateTime launchedAt;

    private LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rocket_id", nullable = false)
    private Rocket rocket;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "weather_condition_id")
    private WeatherCondition weatherCondition;

    // tabela intermediária gerada automaticamente
    @ManyToMany
    @JoinTable(
            name = "launch_windows_launches",
            joinColumns = @JoinColumn(name = "launch_id"),
            inverseJoinColumns = @JoinColumn(name = "window_id")
    )
    @Builder.Default
    private List<LaunchWindow> launchWindows = new ArrayList<>();

    @OneToMany(mappedBy = "launch", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Telemetry> telemetries = new ArrayList<>();

    @OneToMany(mappedBy = "launch", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Failure> failures = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.scheduledAt = LocalDateTime.now();
        this.status = LaunchStatus.SCHEDULED;
    }

    // só aceita telemetria se estiver voando
    public boolean isInFlight() {
        return this.status == LaunchStatus.IN_FLIGHT;
    }

    // pode ser abortado se ainda não finalizou
    public boolean canBeAborted() {
        return this.status == LaunchStatus.SCHEDULED
                || this.status == LaunchStatus.IN_FLIGHT;
    }
}