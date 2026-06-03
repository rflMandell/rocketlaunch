package br.com.fiap.rocketlaunch.domain.entity;

import br.com.fiap.rocketlaunch.domain.enums.MissionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "missions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String destination;

    @Column(columnDefinition = "TEXT")
    private String objective;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Uma missão pode ter vários lançamentos
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Launch> launches = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = MissionStatus.PLANNED;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // missão só pode iniciar se estiver PLANNED
    public boolean canStart() {
        return this.status == MissionStatus.PLANNED;
    }
}