package br.com.fiap.rocketlaunch.controller;

import br.com.fiap.rocketlaunch.dto.request.FailureRequest;
import br.com.fiap.rocketlaunch.dto.response.FailureResponse;
import br.com.fiap.rocketlaunch.service.FailureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/launches/{launchId}/failures")
@RequiredArgsConstructor
public class FailureController {

    private final FailureService failureService;

    @PostMapping
    public ResponseEntity<FailureResponse> report(
            @PathVariable Long launchId,
            @Valid @RequestBody FailureRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(failureService.report(launchId, request));
    }

    @GetMapping
    public ResponseEntity<List<FailureResponse>> findByLaunch(
            @PathVariable Long launchId) {
        return ResponseEntity.ok(failureService.findByLaunch(launchId));
    }

    @GetMapping("/{failureId}")
    public ResponseEntity<FailureResponse> findById(
            @PathVariable Long launchId,
            @PathVariable Long failureId) {
        return ResponseEntity.ok(failureService.findById(failureId));
    }
}