package com.security360.security360_backend.service;

import com.security360.security360_backend.entity.Camera;
import com.security360.security360_backend.repository.CameraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CameraService {

    private final CameraRepository cameraRepository;

    public CameraService(CameraRepository cameraRepository) {
        this.cameraRepository = cameraRepository;
    }

    @Transactional(readOnly = true)
    public List<Camera> getAllCameras() {
        return cameraRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Camera getCamera(Long id) {

        return cameraRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Camera not found with id: " + id
                        )
                );
    }

    public Camera createCamera(Camera camera) {

        if (camera.getCameraName() == null ||
                camera.getCameraName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Camera name is required"
            );
        }

        if (camera.getLocation() == null ||
                camera.getLocation().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Location is required"
            );
        }

        if (camera.getCameraId() == null ||
                camera.getCameraId().trim().isEmpty()) {

            camera.setCameraId(generateCameraId());
        }

        if (cameraRepository.existsByCameraId(camera.getCameraId())) {
            camera.setCameraId(generateCameraId());
        }

        if (camera.getIpAddress() == null ||
                camera.getIpAddress().isBlank()) {

            camera.setIpAddress("—");
        }

        if (camera.getProtocol() == null ||
                camera.getProtocol().isBlank()) {

            camera.setProtocol("RTSP");
        }

        if (camera.getStatus() == null ||
                camera.getStatus().isBlank()) {

            camera.setStatus("Offline");
        }

        if (camera.getRecording() == null ||
                camera.getRecording().isBlank()) {

            camera.setRecording("Stopped");
        }

        if (camera.getAiDetection() == null ||
                camera.getAiDetection().isBlank()) {

            camera.setAiDetection("Disabled");
        }

        if (camera.getAlerts() == null) {
            camera.setAlerts(0);
        }

        return cameraRepository.save(camera);
    }

    public Camera updateCamera(
            Long id,
            Camera request
    ) {

        Camera camera = getCamera(id);

        if (request.getCameraName() != null &&
                !request.getCameraName().isBlank()) {

            camera.setCameraName(
                    request.getCameraName().trim()
            );
        }

        if (request.getLocation() != null &&
                !request.getLocation().isBlank()) {

            camera.setLocation(
                    request.getLocation().trim()
            );
        }

        if (request.getIpAddress() != null) {
            camera.setIpAddress(
                    request.getIpAddress()
            );
        }

        if (request.getProtocol() != null) {
            camera.setProtocol(
                    request.getProtocol()
            );
        }

        if (request.getStatus() != null) {
            camera.setStatus(
                    request.getStatus()
            );
        }

        if (request.getRecording() != null) {
            camera.setRecording(
                    request.getRecording()
            );
        }

        if (request.getAiDetection() != null) {
            camera.setAiDetection(
                    request.getAiDetection()
            );
        }

        if (request.getAlerts() != null) {
            camera.setAlerts(
                    request.getAlerts()
            );
        }

        return cameraRepository.save(camera);
    }

    public Camera saveSnapshot(
            Long id,
            String snapshot
    ) {

        if (snapshot == null ||
                snapshot.isBlank()) {

            throw new IllegalArgumentException(
                    "Snapshot is required"
            );
        }

        Camera camera = getCamera(id);

        camera.setSnapshot(snapshot);
        camera.setLastSnapshot(
                LocalDateTime.now()
        );

        camera.setStatus("Online");

        return cameraRepository.save(camera);
    }

    public void deleteCamera(Long id) {

        Camera camera = getCamera(id);

        cameraRepository.delete(camera);
    }

    private String generateCameraId() {

        long number = cameraRepository.count() + 1;

        String cameraId;

        do {

            cameraId =
                    String.format(
                            "CAM-%04d",
                            number++
                    );

        } while (
                cameraRepository.existsByCameraId(cameraId)
        );

        return cameraId;
    }
}