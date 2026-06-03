package br.com.fiap.rocketlaunch.repository;

import br.com.fiap.rocketlaunch.domain.entity.WeatherCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WeatherConditionRepository extends JpaRepository<WeatherCondition, Long> {

    // Busca condições climáticas seguras para lançamento
    @Query("""
            SELECT w FROM WeatherCondition w
            WHERE w.windSpeedKmh <= 65
            AND w.visibilityKm >= 5
            AND w.precipitationMm < 10
            """)
    List<WeatherCondition> findSafeConditions();
}