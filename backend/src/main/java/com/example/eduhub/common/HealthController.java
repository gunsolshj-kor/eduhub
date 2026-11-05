package com.example.eduhub.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Health", description = "서버 상태 확인")
@RestController
public class HealthController {

    @Operation(summary = "서버 상태 확인")
    @GetMapping("/api/health")
    public HealthResponse health() {
        return new HealthResponse("UP");
    }

    public record HealthResponse(String status) {}
}
