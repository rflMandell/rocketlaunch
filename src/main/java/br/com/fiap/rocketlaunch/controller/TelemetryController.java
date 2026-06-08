package br.com.fiap.rocketlaunch.controller;

import br.com.fiap.rocketlaunch.dto.request.TelemetryRequest;
import br.com.fiap.rocketlaunch.dto.response.TelemetryResponse;
import br.com.fiap.rocketlaunch.service.TelemetryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/launches/{launchId}/telemetry")
@RequiredArgsConstructor
public class TelemetryController {

    private final TelemetryService telemetryService;

    @PostMapping
    public ResponseEntity<TelemetryResponse> record(
            @PathVariable Long launchId,
            @Valid @RequestBody TelemetryRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(telemetryService.record(launchId, request));
    }

    @GetMapping
    public ResponseEntity<Page<TelemetryResponse>> findAll(
            @PathVariable Long launchId,
            @PageableDefault(size = 20, sort = "recordedAt") Pageable pageable) {
        return ResponseEntity.ok(telemetryService.findByLaunch(launchId, pageable));
    }

    @GetMapping("/latest")
    public ResponseEntity<TelemetryResponse> findLatest(
            @PathVariable Long launchId) {
        return ResponseEntity.ok(telemetryService.findLatest(launchId));
    }
}