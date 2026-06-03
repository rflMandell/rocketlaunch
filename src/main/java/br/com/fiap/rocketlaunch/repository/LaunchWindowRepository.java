package br.com.fiap.rocketlaunch.repository;

import br.com.fiap.rocketlaunch.domain.entity.LaunchWindow;
import br.com.fiap.rocketlaunch.domain.enums.WindowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface LaunchWindowRepository extends JpaRepository<LaunchWindow, Long> {

    List<LaunchWindow> findByStatus(WindowStatus status);

    @Query("""
            SELECT w FROM LaunchWindow w
            WHERE w.status = 'OPEN'
            AND w.windowStart <= :now
            AND w.windowEnd >= :now
            """)
    List<LaunchWindow> findActiveWindows(LocalDateTime now);

    @Query("""
            SELECT w FROM LaunchWindow w
            WHERE w.status = 'OPEN'
            AND w.windowEnd < :now
            """)
    List<LaunchWindow> findExpiredWindows(LocalDateTime now);
}