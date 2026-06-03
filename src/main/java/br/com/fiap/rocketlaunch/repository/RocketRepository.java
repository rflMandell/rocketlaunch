package br.com.fiap.rocketlaunch.repository;

import br.com.fiap.rocketlaunch.domain.entity.Rocket;
import br.com.fiap.rocketlaunch.domain.enums.RocketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RocketRepository extends JpaRepository<Rocket, Long> {

    Optional<Rocket> findByName(String name);

    List<Rocket> findByStatus(RocketStatus status);

    Page<Rocket> findByStatus(RocketStatus status, Pageable pageable);

    boolean existsByName(String name);

    // Busca foguetes com capacidade suficiente para um determinado peso
    @Query("SELECT r FROM Rocket r WHERE r.spec.maxPayloadKg >= :minCapacity")
    List<Rocket> findByMinPayloadCapacity(Double minCapacity);
}