package br.com.fiap.rocketlaunch.domain.entity;

import br.com.fiap.rocketlaunch.domain.enums.FailureSeverity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "failures")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Failure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String component;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FailureSeverity severity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime occurredAt;

    // Muitas falhas pertencem a um lançamento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "launch_id", nullable = false)
    private Launch launch;

    @PrePersist
    public void prePersist() {
        this.occurredAt = LocalDateTime.now();
    }

    // falha crítica deve abortar o lançamento
    public boolean isCritical() {
        return this.severity == FailureSeverity.CRITICAL;
    }
}