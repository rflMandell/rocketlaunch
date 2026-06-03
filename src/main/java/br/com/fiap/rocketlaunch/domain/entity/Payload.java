package br.com.fiap.rocketlaunch.domain.entity;

import br.com.fiap.rocketlaunch.domain.enums.PayloadType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "payloads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayloadType type;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Double weightKg;

    private String description;

    // Muitas cargas pertencem a um foguete
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rocket_id", nullable = false)
    private Rocket rocket;
}