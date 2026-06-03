package br.com.fiap.rocketlaunch.repository;

import br.com.fiap.rocketlaunch.domain.entity.Telemetry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TelemetryRepository extends JpaRepository<Telemetry, Long> {

    Page<Telemetry> findByLaunchId(Long launchId, Pageable pageable);

    // Último registro de telemetria de um lançamento
    @Query("""
            SELECT t FROM Telemetry t
            WHERE t.launch.id = :launchId
            ORDER BY t.recordedAt DESC
            LIMIT 1
            """)
    Optional<Telemetry> findLatestByLaunchId(Long launchId);
}