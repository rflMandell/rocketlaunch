package br.com.fiap.rocketlaunch.service;

import br.com.fiap.rocketlaunch.domain.entity.Failure;
import br.com.fiap.rocketlaunch.domain.entity.Launch;
import br.com.fiap.rocketlaunch.domain.enums.LaunchStatus;
import br.com.fiap.rocketlaunch.domain.enums.MissionStatus;
import br.com.fiap.rocketlaunch.domain.enums.RocketStatus;
import br.com.fiap.rocketlaunch.dto.request.FailureRequest;
import br.com.fiap.rocketlaunch.dto.response.FailureResponse;
import br.com.fiap.rocketlaunch.exception.BusinessException;
import br.com.fiap.rocketlaunch.exception.ResourceNotFoundException;
import br.com.fiap.rocketlaunch.repository.FailureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FailureService {

    private final FailureRepository failureRepository;
    private final LaunchService launchService;

    @Transactional
    public FailureResponse report(Long launchId, FailureRequest request) {
        Launch launch = launchService.findLaunchOrThrow(launchId);

        // só registra falha em lançamentos ativos
        if (launch.getStatus() != LaunchStatus.IN_FLIGHT
                && launch.getStatus() != LaunchStatus.SCHEDULED) {
            throw new BusinessException(
                    "Falhas só podem ser registradas em lançamentos SCHEDULED ou IN_FLIGHT. " +
                            "Status atual: " + launch.getStatus()
            );
        }

        Failure failure = Failure.builder()
                .component(request.component())
                .description(request.description())
                .severity(request.severity())
                .launch(launch)
                .build();

        Failure saved = failureRepository.save(failure);

        // falha CRITICAL aborta o lançamento automaticamente
        if (saved.isCritical()) {
            launch.setStatus(LaunchStatus.ABORTED);
            launch.setCompletedAt(LocalDateTime.now());
            launch.getMission().setStatus(MissionStatus.FAILED);
            launch.getRocket().setStatus(RocketStatus.MAINTENANCE);
            launchService.findLaunchOrThrow(launchId); // força refresh no contexto
        }

        return FailureResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<FailureResponse> findByLaunch(Long launchId) {
        launchService.findLaunchOrThrow(launchId);
        return failureRepository.findByLaunchId(launchId)
                .stream()
                .map(FailureResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public FailureResponse findById(Long id) {
        return FailureResponse.from(
                failureRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Falha não encontrada com id: " + id
                        ))
        );
    }
}