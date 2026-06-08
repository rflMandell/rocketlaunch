package br.com.fiap.rocketlaunch.controller;

import br.com.fiap.rocketlaunch.domain.enums.RocketStatus;
import br.com.fiap.rocketlaunch.dto.request.RocketRequest;
import br.com.fiap.rocketlaunch.dto.response.RocketResponse;
import br.com.fiap.rocketlaunch.service.RocketService;
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
@RequestMapping("/api/rockets")
@RequiredArgsConstructor
public class RocketController {

    private final RocketService rocketService;

    @PostMapping
    public ResponseEntity<RocketResponse> create(
            @Valid @RequestBody RocketRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(rocketService.create(request));
    }

    @GetMapping
    public ResponseEntity<Page<RocketResponse>> findAll(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(rocketService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RocketResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(rocketService.findById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RocketResponse>> findByStatus(
            @PathVariable RocketStatus status) {
        return ResponseEntity.ok(rocketService.findByStatus(status));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RocketResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam RocketStatus status) {
        return ResponseEntity.ok(rocketService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rocketService.delete(id);
        return ResponseEntity.noContent().build();
    }
}