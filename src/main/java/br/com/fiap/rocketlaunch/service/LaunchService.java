package br.com.fiap.rocketlaunch.service;

import br.com.fiap.rocketlaunch.domain.entity.*;
import br.com.fiap.rocketlaunch.domain.enums.*;
import br.com.fiap.rocketlaunch.dto.request.LaunchRequest;
import br.com.fiap.rocketlaunch.dto.response.LaunchResponse;
import br.com.fiap.rocketlaunch.exception.BusinessException;
import br.com.fiap.rocketlaunch.exception.ResourceNotFoundException;
import br.com.fiap.rocketlaunch.repository.LaunchRepository;
import br.com.fiap.rocketlaunch.service.validation.LaunchValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LaunchService {

    private final LaunchRepository launchRepository;
    private final MissionService missionService;
    private final RocketService rocketService;
    private final LaunchWindowService launchWindowService;

    // injeta todas as implementações de LaunchValidator
    private final List<LaunchValidator> validators;

    @Transactional
    public LaunchResponse schedule(LaunchRequest request) {
        Mission mission = missionService.findMissionOrThrow(request.missionId());
        Rocket rocket = rocketService.findRocketOrThrow(request.rocketId());

        // missão deve estar PLANNED para receber um lançamento
        if (!mission.canStart()) {
            throw new BusinessException(
                    "Missão não está disponível para lançamento. " +
                            "Status atual: " + mission.getStatus()
            );
        }

        // foguete não pode ter lançamento ativo
        if (launchRepository.existsActiveByRocketId(rocket.getId())) {
            throw new BusinessException(
                    "Foguete '" + rocket.getName() +
                            "' já possui um lançamento ativo (SCHEDULED ou IN_FLIGHT)."
            );
        }

        // Monta a condição climática a partir do request
        WeatherCondition weather = WeatherCondition.builder()
                .windSpeedKmh(request.weatherCondition().windSpeedKmh())
                .temperatureCelsius(request.weatherCondition().temperatureCelsius())
                .visibilityKm(request.weatherCondition().visibilityKm())
                .precipitationMm(request.weatherCondition().precipitationMm())
                .build();

        // Builder Pattern: monta o Launch
        Launch launch = Launch.builder()
                .mission(mission)
                .rocket(rocket)
                .weatherCondition(weather)
                .build();

        return LaunchResponse.from(launchRepository.save(launch));
    }

    @Transactional
    public LaunchResponse addWindow(Long launchId, Long windowId) {
        Launch launch = findLaunchOrThrow(launchId);
        LaunchWindow window = launchWindowService.findWindowOrThrow(windowId);

        if (window.getStatus() != WindowStatus.OPEN) {
            throw new BusinessException(
                    "Janela de lançamento não está disponível. " +
                            "Status atual: " + window.getStatus()
            );
        }

        if (launch.getLaunchWindows().contains(window)) {
            throw new BusinessException("Janela já associada a este lançamento.");
        }

        launch.getLaunchWindows().add(window);
        return LaunchResponse.from(launchRepository.save(launch));
    }

    @Transactional
    public LaunchResponse executeLaunch(Long launchId) {
        Launch launch = findLaunchOrThrow(launchId);

        if (launch.getStatus() != LaunchStatus.SCHEDULED) {
            throw new BusinessException(
                    "Apenas lançamentos com status SCHEDULED podem ser executados. " +
                            "Status atual: " + launch.getStatus()
            );
        }

        validators.forEach(v -> v.validate(launch));

        launch.setStatus(LaunchStatus.IN_FLIGHT);
        launch.setLaunchedAt(LocalDateTime.now());
        Mission mission = launch.getMission();
        mission.setStatus(MissionStatus.IN_PROGRESS);
        Rocket rocket = launch.getRocket();
        rocket.setStatus(RocketStatus.IN_MISSION);

        // Marca a janela utilizada como USED
        launch.getLaunchWindows()
                .stream()
                .filter(LaunchWindow::isActive)
                .findFirst()
                .ifPresent(w -> w.setStatus(WindowStatus.USED));

        return LaunchResponse.from(launchRepository.save(launch));
    }

    @Transactional
    public LaunchResponse completeLaunch(Long launchId) {
        Launch launch = findLaunchOrThrow(launchId);

        if (launch.getStatus() != LaunchStatus.IN_FLIGHT) {
            throw new BusinessException(
                    "Apenas lançamentos IN_FLIGHT podem ser concluídos. " +
                            "Status atual: " + launch.getStatus()
            );
        }

        launch.setStatus(LaunchStatus.COMPLETED);
        launch.setCompletedAt(LocalDateTime.now());

        launch.getMission().setStatus(MissionStatus.COMPLETED);
        launch.getRocket().setStatus(RocketStatus.AVAILABLE);

        return LaunchResponse.from(launchRepository.save(launch));
    }

    @Transactional
    public LaunchResponse abortLaunch(Long launchId) {
        Launch launch = findLaunchOrThrow(launchId);

        if (!launch.canBeAborted()) {
            throw new BusinessException(
                    "Lançamento não pode ser abortado no status atual: " +
                            launch.getStatus()
            );
        }

        launch.setStatus(LaunchStatus.ABORTED);
        launch.setCompletedAt(LocalDateTime.now());

        launch.getMission().setStatus(MissionStatus.FAILED);
        launch.getRocket().setStatus(RocketStatus.MAINTENANCE);

        return LaunchResponse.from(launchRepository.save(launch));
    }

    @Transactional(readOnly = true)
    public Page<LaunchResponse> findAll(Pageable pageable) {
        return launchRepository.findAll(pageable)
                .map(LaunchResponse::from);
    }

    @Transactional(readOnly = true)
    public LaunchResponse findById(Long id) {
        return LaunchResponse.from(findLaunchOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<LaunchResponse> findByMission(Long missionId) {
        return launchRepository.findByMissionId(missionId)
                .stream()
                .map(LaunchResponse::from)
                .toList();
    }

    public Launch findLaunchOrThrow(Long id) {
        return launchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lançamento não encontrado com id: " + id
                ));
    }
}