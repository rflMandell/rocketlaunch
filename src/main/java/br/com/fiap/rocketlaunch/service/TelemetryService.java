package br.com.fiap.rocketlaunch.service;

import br.com.fiap.rocketlaunch.domain.entity.Launch;
import br.com.fiap.rocketlaunch.domain.entity.Telemetry;
import br.com.fiap.rocketlaunch.dto.request.TelemetryRequest;
import br.com.fiap.rocketlaunch.dto.response.TelemetryResponse;
import br.com.fiap.rocketlaunch.exception.BusinessException;
import br.com.fiap.rocketlaunch.exception.ResourceNotFoundException;
import br.com.fiap.rocketlaunch.repository.TelemetryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TelemetryService {

    private final TelemetryRepository telemetryRepository;
    private final LaunchService launchService;

    @Transactional
    public TelemetryResponse record(Long launchId, TelemetryRequest request) {
        Launch launch = launchService.findLaunchOrThrow(launchId);

        // só registra telemetria se o lançamento estiver em voo
        if (!launch.isInFlight()) {
            throw new BusinessException(
                    "Telemetria só pode ser registrada durante um lançamento IN_FLIGHT. " +
                            "Status atual: " + launch.getStatus()
            );
        }

        // centraliza a criação do objeto Telemetry
        Telemetry telemetry = buildTelemetry(request, launch);

        return TelemetryResponse.from(telemetryRepository.save(telemetry));
    }

    @Transactional(readOnly = true)
    public Page<TelemetryResponse> findByLaunch(Long launchId, Pageable pageable) {
        launchService.findLaunchOrThrow(launchId);
        return telemetryRepository.findByLaunchId(launchId, pageable)
                .map(TelemetryResponse::from);
    }

    @Transactional(readOnly = true)
    public TelemetryResponse findLatest(Long launchId) {
        launchService.findLaunchOrThrow(launchId);
        return telemetryRepository.findLatestByLaunchId(launchId)
                .map(TelemetryResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Nenhum registro de telemetria encontrado para o lançamento: "
                                + launchId
                ));
    }

    // isola a construção do objeto
    private Telemetry buildTelemetry(TelemetryRequest request, Launch launch) {
        return Telemetry.builder()
                .altitudeKm(request.altitudeKm())
                .velocityKmh(request.velocityKmh())
                .accelerationMs2(request.accelerationMs2())
                .fuelLevelPercent(request.fuelLevelPercent())
                .temperatureCelsius(request.temperatureCelsius())
                .launch(launch)
                .build();
    }
}