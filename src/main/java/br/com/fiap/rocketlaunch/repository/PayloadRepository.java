package br.com.fiap.rocketlaunch.repository;

import br.com.fiap.rocketlaunch.domain.entity.Payload;
import br.com.fiap.rocketlaunch.domain.enums.PayloadType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PayloadRepository extends JpaRepository<Payload, Long> {

    List<Payload> findByRocketId(Long rocketId);

    List<Payload> findByType(PayloadType type);

    // Soma o peso total de cargas de um foguete
    @Query("SELECT COALESCE(SUM(p.weightKg), 0) FROM Payload p WHERE p.rocket.id = :rocketId")
    Double sumWeightByRocketId(Long rocketId);
}