package br.com.fiap.rocketlaunch.repository;

import br.com.fiap.rocketlaunch.domain.entity.Failure;
import br.com.fiap.rocketlaunch.domain.enums.FailureSeverity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FailureRepository extends JpaRepository<Failure, Long> {

    List<Failure> findByLaunchId(Long launchId);

    List<Failure> findBySeverity(FailureSeverity severity);

    // Verifica se existe falha crítica em um lançamento
    @Query("""
            SELECT COUNT(f) > 0 FROM Failure f
            WHERE f.launch.id = :launchId
            AND f.severity = 'CRITICAL'
            """)
    boolean existsCriticalByLaunchId(Long launchId);
}