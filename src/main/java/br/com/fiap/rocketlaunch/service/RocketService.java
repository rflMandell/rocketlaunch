package br.com.fiap.rocketlaunch.service;

import br.com.fiap.rocketlaunch.domain.embedded.RocketSpec;
import br.com.fiap.rocketlaunch.domain.entity.Rocket;
import br.com.fiap.rocketlaunch.domain.enums.RocketStatus;
import br.com.fiap.rocketlaunch.dto.request.RocketRequest;
import br.com.fiap.rocketlaunch.dto.response.RocketResponse;
import br.com.fiap.rocketlaunch.exception.BusinessException;
import br.com.fiap.rocketlaunch.exception.ResourceNotFoundException;
import br.com.fiap.rocketlaunch.repository.LaunchRepository;
import br.com.fiap.rocketlaunch.repository.RocketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RocketService {

    private final RocketRepository rocketRepository;
    private final LaunchRepository launchRepository;

    @Transactional
    public RocketResponse create(RocketRequest request) {
        if (rocketRepository.existsByName(request.name())) {
            throw new BusinessException(
                    "Já existe um foguete com o nome '" + request.name() + "'."
            );
        }

        RocketSpec spec = new RocketSpec(
                request.thrustKN(),
                request.maxPayloadKg(),
                request.heightMeters(),
                request.stages(),
                request.fuelCapacityLiters()
        );

        Rocket rocket = Rocket.builder()
                .name(request.name())
                .manufacturer(request.manufacturer())
                .spec(spec)
                .build();

        return RocketResponse.from(rocketRepository.save(rocket));
    }

    @Transactional(readOnly = true)
    public Page<RocketResponse> findAll(Pageable pageable) {
        return rocketRepository.findAll(pageable)
                .map(RocketResponse::from);
    }

    @Transactional(readOnly = true)
    public RocketResponse findById(Long id) {
        return RocketResponse.from(findRocketOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<RocketResponse> findByStatus(RocketStatus status) {
        return rocketRepository.findByStatus(status)
                .stream()
                .map(RocketResponse::from)
                .toList();
    }

    @Transactional
    public RocketResponse updateStatus(Long id, RocketStatus newStatus) {
        Rocket rocket = findRocketOrThrow(id);

        // foguete em missão ativa não pode ir para manutenção diretamente
        if (launchRepository.existsActiveByRocketId(id)
                && newStatus == RocketStatus.MAINTENANCE) {
            throw new BusinessException(
                    "Foguete possui lançamento ativo. " +
                            "Finalize o lançamento antes de enviar para manutenção."
            );
        }

        rocket.setStatus(newStatus);
        return RocketResponse.from(rocketRepository.save(rocket));
    }

    @Transactional
    public void delete(Long id) {
        Rocket rocket = findRocketOrThrow(id);

        if (launchRepository.existsActiveByRocketId(id)) {
            throw new BusinessException(
                    "Foguete possui lançamento ativo e não pode ser removido."
            );
        }

        rocketRepository.delete(rocket);
    }

    public Rocket findRocketOrThrow(Long id) {
        return rocketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Foguete não encontrado com id: " + id
                ));
    }
}