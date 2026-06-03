package br.com.fiap.rocketlaunch.repository;

import br.com.fiap.rocketlaunch.domain.entity.Launch;
import br.com.fiap.rocketlaunch.domain.enums.LaunchStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LaunchRepository extends JpaRepository<Launch, Long> {

    Page<Launch> findByStatus(LaunchStatus status, Pageable pageable);

    List<Launch> findByMissionId(Long missionId);

    List<Launch> findByRocketId(Long rocketId);

    // Verifica se há lançamento ativo para um foguete
    @Query("""
            SELECT COUNT(l) > 0 FROM Launch l
            WHERE l.rocket.id = :rocketId
            AND l.status IN ('SCHEDULED', 'IN_FLIGHT')
            """)
    boolean existsActiveByRocketId(Long rocketId);
}