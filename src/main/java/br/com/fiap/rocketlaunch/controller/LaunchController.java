package br.com.fiap.rocketlaunch.controller;

import br.com.fiap.rocketlaunch.dto.request.LaunchRequest;
import br.com.fiap.rocketlaunch.dto.response.LaunchResponse;
import br.com.fiap.rocketlaunch.service.LaunchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/launches")
@RequiredArgsConstructor
public class LaunchController {

    private final LaunchService launchService;

    // Agenda um novo lançamento
    @PostMapping
    public ResponseEntity<LaunchResponse> schedule(
            @Valid @RequestBody LaunchRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(launchService.schedule(request));
    }

    @GetMapping
    public ResponseEntity<Page<LaunchResponse>> findAll(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(launchService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaunchResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(launchService.findById(id));
    }

    @GetMapping("/mission/{missionId}")
    public ResponseEntity<List<LaunchResponse>> findByMission(
            @PathVariable Long missionId) {
        return ResponseEntity.ok(launchService.findByMission(missionId));
    }

    // Associa uma janela de lançamento ao lançamento
    @PostMapping("/{id}/windows/{windowId}")
    public ResponseEntity<LaunchResponse> addWindow(
            @PathVariable Long id,
            @PathVariable Long windowId) {
        return ResponseEntity.ok(launchService.addWindow(id, windowId));
    }

    // Executa o lançamento
    @PostMapping("/{id}/execute")
    public ResponseEntity<LaunchResponse> execute(@PathVariable Long id) {
        return ResponseEntity.ok(launchService.executeLaunch(id));
    }

    // Conclui o lançamento com sucesso
    @PostMapping("/{id}/complete")
    public ResponseEntity<LaunchResponse> complete(@PathVariable Long id) {
        return ResponseEntity.ok(launchService.completeLaunch(id));
    }

    // Aborta o lançamento
    @PostMapping("/{id}/abort")
    public ResponseEntity<LaunchResponse> abort(@PathVariable Long id) {
        return ResponseEntity.ok(launchService.abortLaunch(id));
    }
}