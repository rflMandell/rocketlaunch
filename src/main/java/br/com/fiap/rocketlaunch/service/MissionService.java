package br.com.fiap.rocketlaunch.service;

import br.com.fiap.rocketlaunch.domain.entity.Mission;
import br.com.fiap.rocketlaunch.domain.enums.MissionStatus;
import br.com.fiap.rocketlaunch.dto.request.MissionRequest;
import br.com.fiap.rocketlaunch.dto.response.MissionResponse;
import br.com.fiap.rocketlaunch.exception.BusinessException;
import br.com.fiap.rocketlaunch.exception.ResourceNotFoundException;
import br.com.fiap.rocketlaunch.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;

    @Transactional
    public MissionResponse create(MissionRequest request) {
        if (missionRepository.existsByName(request.name())) {
            throw new BusinessException(
                    "Já existe uma missão com o nome '" + request.name() + "'."
            );
        }

        Mission mission = Mission.builder()
                .name(request.name())
                .destination(request.destination())
                .objective(request.objective())
                .build();

        return MissionResponse.from(missionRepository.save(mission));
    }

    @Transactional(readOnly = true)
    public Page<MissionResponse> findAll(Pageable pageable) {
        return missionRepository.findAll(pageable)
                .map(MissionResponse::from);
    }

    @Transactional(readOnly = true)
    public MissionResponse findById(Long id) {
        return MissionResponse.from(findMissionOrThrow(id));
    }

    @Transactional(readOnly = true)
    public Page<MissionResponse> findByStatus(MissionStatus status, Pageable pageable) {
        return missionRepository.findByStatus(status, pageable)
                .map(MissionResponse::from);
    }

    @Transactional
    public MissionResponse cancel(Long id) {
        Mission mission = findMissionOrThrow(id);

        // só pode cancelar missões planejadas
        if (mission.getStatus() != MissionStatus.PLANNED) {
            throw new BusinessException(
                    "Apenas missões com status PLANNED podem ser canceladas. " +
                            "Status atual: " + mission.getStatus()
            );
        }

        mission.setStatus(MissionStatus.CANCELLED);
        return MissionResponse.from(missionRepository.save(mission));
    }

    public Mission findMissionOrThrow(Long id) {
        return missionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Missão não encontrada com id: " + id
                ));
    }
}