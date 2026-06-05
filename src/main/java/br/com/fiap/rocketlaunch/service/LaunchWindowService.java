package br.com.fiap.rocketlaunch.service;

import br.com.fiap.rocketlaunch.domain.entity.LaunchWindow;
import br.com.fiap.rocketlaunch.domain.enums.WindowStatus;
import br.com.fiap.rocketlaunch.dto.request.LaunchWindowRequest;
import br.com.fiap.rocketlaunch.dto.response.LaunchWindowResponse;
import br.com.fiap.rocketlaunch.exception.BusinessException;
import br.com.fiap.rocketlaunch.exception.ResourceNotFoundException;
import br.com.fiap.rocketlaunch.repository.LaunchWindowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LaunchWindowService {

    private final LaunchWindowRepository launchWindowRepository;

    @Transactional
    public LaunchWindowResponse create(LaunchWindowRequest request) {
        // fim da janela deve ser depois do início
        if (!request.windowEnd().isAfter(request.windowStart())) {
            throw new BusinessException(
                    "O fim da janela de lançamento deve ser posterior ao início."
            );
        }

        LaunchWindow window = LaunchWindow.builder()
                .windowStart(request.windowStart())
                .windowEnd(request.windowEnd())
                .notes(request.notes())
                .build();

        return LaunchWindowResponse.from(launchWindowRepository.save(window));
    }

    @Transactional(readOnly = true)
    public List<LaunchWindowResponse> findAll() {
        return launchWindowRepository.findAll()
                .stream()
                .map(LaunchWindowResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LaunchWindowResponse> findActive() {
        return launchWindowRepository.findActiveWindows(LocalDateTime.now())
                .stream()
                .map(LaunchWindowResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public LaunchWindowResponse findById(Long id) {
        return LaunchWindowResponse.from(findWindowOrThrow(id));
    }

    // Expira janelas que já passaram do prazo, pode ser chamado por um scheduler
    @Transactional
    public int expireOldWindows() {
        List<LaunchWindow> expired =
                launchWindowRepository.findExpiredWindows(LocalDateTime.now());

        expired.forEach(w -> w.setStatus(WindowStatus.EXPIRED));
        launchWindowRepository.saveAll(expired);

        return expired.size();
    }

    public LaunchWindow findWindowOrThrow(Long id) {
        return launchWindowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Janela de lançamento não encontrada com id: " + id
                ));
    }
}