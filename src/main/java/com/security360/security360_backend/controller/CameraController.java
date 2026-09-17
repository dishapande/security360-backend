package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.Camera;
import com.security360.security360_backend.service.CameraService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cameras")
@CrossOrigin(
        origins = {
                "http://localhost:8080",
                "http://localhost:8081"
        }
)
public class CameraController {

    private final CameraService cameraService;

    public CameraController(
            CameraService cameraService
    ) {

        this.cameraService = cameraService;
    }

    @GetMapping
    public ResponseEntity<List<Camera>> getAllCameras() {

        return ResponseEntity.ok(
                cameraService.getAllCameras()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Camera> getCamera(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                cameraService.getCamera(id)
        );
    }

    @PostMapping
    public ResponseEntity<Camera> createCamera(
            @RequestBody Camera camera
    ) {

        return ResponseEntity.ok(
                cameraService.createCamera(camera)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Camera> updateCamera(
            @PathVariable Long id,
            @RequestBody Camera camera
    ) {

        return ResponseEntity.ok(
                cameraService.updateCamera(
                        id,
                        camera
                )
        );
    }

    @PostMapping("/{id}/snapshot")
    public ResponseEntity<Camera> saveSnapshot(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {

        String snapshot = body.get("snapshot");

        return ResponseEntity.ok(
                cameraService.saveSnapshot(
                        id,
                        snapshot
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCamera(
            @PathVariable Long id
    ) {

        cameraService.deleteCamera(id);

        return ResponseEntity.noContent().build();
    }
}