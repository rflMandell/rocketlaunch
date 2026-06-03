package br.com.fiap.rocketlaunch.repository;

import br.com.fiap.rocketlaunch.domain.entity.Mission;
import br.com.fiap.rocketlaunch.domain.enums.MissionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    Optional<Mission> findByName(String name);

    boolean existsByName(String name);

    Page<Mission> findByStatus(MissionStatus status, Pageable pageable);
}