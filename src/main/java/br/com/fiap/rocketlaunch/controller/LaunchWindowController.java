package br.com.fiap.rocketlaunch.controller;

import br.com.fiap.rocketlaunch.dto.request.LaunchWindowRequest;
import br.com.fiap.rocketlaunch.dto.response.LaunchWindowResponse;
import br.com.fiap.rocketlaunch.service.LaunchWindowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/launch-windows")
@RequiredArgsConstructor
public class LaunchWindowController {

    private final LaunchWindowService launchWindowService;

    @PostMapping
    public ResponseEntity<LaunchWindowResponse> create(
            @Valid @RequestBody LaunchWindowRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(launchWindowService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<LaunchWindowResponse>> findAll() {
        return ResponseEntity.ok(launchWindowService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaunchWindowResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(launchWindowService.findById(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<LaunchWindowResponse>> findActive() {
        return ResponseEntity.ok(launchWindowService.findActive());
    }

    @PatchMapping("/expire")
    public ResponseEntity<String> expireOldWindows() {
        int count = launchWindowService.expireOldWindows();
        return ResponseEntity.ok(count + " janela(s) expirada(s) com sucesso.");
    }
}