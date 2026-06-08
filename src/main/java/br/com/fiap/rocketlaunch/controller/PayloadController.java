package br.com.fiap.rocketlaunch.controller;

import br.com.fiap.rocketlaunch.dto.request.PayloadRequest;
import br.com.fiap.rocketlaunch.dto.response.PayloadResponse;
import br.com.fiap.rocketlaunch.service.PayloadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payloads")
@RequiredArgsConstructor
public class PayloadController {

    private final PayloadService payloadService;

    @PostMapping
    public ResponseEntity<PayloadResponse> create(
            @Valid @RequestBody PayloadRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(payloadService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayloadResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(payloadService.findById(id));
    }

    @GetMapping("/rocket/{rocketId}")
    public ResponseEntity<List<PayloadResponse>> findByRocket(
            @PathVariable Long rocketId) {
        return ResponseEntity.ok(payloadService.findByRocket(rocketId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        payloadService.delete(id);
        return ResponseEntity.noContent().build();
    }
}