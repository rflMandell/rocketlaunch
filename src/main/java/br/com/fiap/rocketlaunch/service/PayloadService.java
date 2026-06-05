package br.com.fiap.rocketlaunch.service;

import br.com.fiap.rocketlaunch.domain.entity.Payload;
import br.com.fiap.rocketlaunch.domain.entity.Rocket;
import br.com.fiap.rocketlaunch.dto.request.PayloadRequest;
import br.com.fiap.rocketlaunch.dto.response.PayloadResponse;
import br.com.fiap.rocketlaunch.exception.BusinessException;
import br.com.fiap.rocketlaunch.exception.ResourceNotFoundException;
import br.com.fiap.rocketlaunch.repository.PayloadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PayloadService {

    private final PayloadRepository payloadRepository;
    private final RocketService rocketService;

    @Transactional
    public PayloadResponse create(PayloadRequest request) {
        Rocket rocket = rocketService.findRocketOrThrow(request.rocketId());

        // verifica se o foguete comporta o peso adicional
        if (!rocket.canCarryPayload(request.weightKg())) {
            Double currentWeight = payloadRepository.sumWeightByRocketId(rocket.getId());
            throw new BusinessException(
                    String.format(
                            "Capacidade de carga excedida. Capacidade máxima: %.1f kg, " +
                                    "Peso atual: %.1f kg, Peso solicitado: %.1f kg.",
                            rocket.getSpec().getMaxPayloadKg(),
                            currentWeight,
                            request.weightKg()
                    )
            );
        }

        Payload payload = Payload.builder()
                .name(request.name())
                .type(request.type())
                .weightKg(request.weightKg())
                .description(request.description())
                .rocket(rocket)
                .build();

        return PayloadResponse.from(payloadRepository.save(payload));
    }

    @Transactional(readOnly = true)
    public List<PayloadResponse> findByRocket(Long rocketId) {
        rocketService.findRocketOrThrow(rocketId); // v se foguete existe
        return payloadRepository.findByRocketId(rocketId)
                .stream()
                .map(PayloadResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PayloadResponse findById(Long id) {
        return PayloadResponse.from(findPayloadOrThrow(id));
    }

    @Transactional
    public void delete(Long id) {
        payloadRepository.delete(findPayloadOrThrow(id));
    }

    private Payload findPayloadOrThrow(Long id) {
        return payloadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Carga útil não encontrada com id: " + id
                ));
    }
}