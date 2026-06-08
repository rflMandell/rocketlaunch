package br.com.fiap.rocketlaunch.controller;

import br.com.fiap.rocketlaunch.domain.enums.MissionStatus;
import br.com.fiap.rocketlaunch.dto.request.MissionRequest;
import br.com.fiap.rocketlaunch.dto.response.MissionResponse;
import br.com.fiap.rocketlaunch.service.MissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @PostMapping
    public ResponseEntity<MissionResponse> create(
            @Valid @RequestBody MissionRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(missionService.create(request));
    }

    @GetMapping
    public ResponseEntity<Page<MissionResponse>> findAll(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(missionService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MissionResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(missionService.findById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<MissionResponse>> findByStatus(
            @PathVariable MissionStatus status,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(missionService.findByStatus(status, pageable));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<MissionResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(missionService.cancel(id));
    }
}