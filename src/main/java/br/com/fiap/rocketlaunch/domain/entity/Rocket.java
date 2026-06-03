package br.com.fiap.rocketlaunch.domain.entity;

import br.com.fiap.rocketlaunch.domain.embedded.RocketSpec;
import br.com.fiap.rocketlaunch.domain.enums.RocketStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rockets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rocket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String manufacturer;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RocketStatus status;

    @Embedded
    @NotNull
    private RocketSpec spec;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Um foguete pode carregar múltiplas cargas úteis
    @OneToMany(mappedBy = "rocket", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Payload> payloads = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = RocketStatus.AVAILABLE;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // verifica se o foguete está disponível
    public boolean isAvailable() {
        return this.status == RocketStatus.AVAILABLE;
    }

    // verifica se o peso total dos payloads não excede a capacidade
    public boolean canCarryPayload(Double additionalWeightKg) {
        double currentWeight = payloads.stream()
                .mapToDouble(Payload::getWeightKg)
                .sum();
        return (currentWeight + additionalWeightKg) <= spec.getMaxPayloadKg();
    }
}