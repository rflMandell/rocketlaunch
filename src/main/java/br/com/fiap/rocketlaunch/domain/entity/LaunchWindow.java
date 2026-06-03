package br.com.fiap.rocketlaunch.domain.entity;

import br.com.fiap.rocketlaunch.domain.enums.WindowStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "launch_windows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaunchWindow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime windowStart;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime windowEnd;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WindowStatus status;

    private String notes;

    // uma janela pode ser associada a vários lançamentos e vice-versa
    @ManyToMany(mappedBy = "launchWindows")
    @Builder.Default
    private List<Launch> launches = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.status = WindowStatus.OPEN;
    }

    // verifica se a janela está ativa no momento atual
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return this.status == WindowStatus.OPEN
                && now.isAfter(windowStart)
                && now.isBefore(windowEnd);
    }
}