package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.JobOpening;
import com.security360.security360_backend.entity.RecruitmentCandidate;
import com.security360.security360_backend.repository.JobOpeningRepository;
import com.security360.security360_backend.repository.RecruitmentCandidateRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/recruitment")
@CrossOrigin(origins = {
        
        "http://localhost:8081"
})
public class RecruitmentController {

    private final JobOpeningRepository jobRepository;
    private final RecruitmentCandidateRepository candidateRepository;

    private final Path uploadDirectory;

    public RecruitmentController(
            JobOpeningRepository jobRepository,
            RecruitmentCandidateRepository candidateRepository,
            @Value("${recruitment.upload-dir:uploads/resumes}") String uploadDir
    ) {

        this.jobRepository = jobRepository;
        this.candidateRepository = candidateRepository;

        this.uploadDirectory =
                Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.uploadDirectory);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not create resume upload directory",
                    e
            );
        }
    }

    // =========================================================
    // JOB OPENINGS
    // =========================================================

    @GetMapping("/jobs")
    public ResponseEntity<?> getAllJobs() {

        return ResponseEntity.ok(
                jobRepository.findAll()
        );
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<?> getJob(
            @PathVariable Long id
    ) {

        Optional<JobOpening> job =
                jobRepository.findById(id);

        if (job.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(job.get());
    }

    @PostMapping("/jobs")
    public ResponseEntity<?> createJob(
            @RequestBody JobOpening job
    ) {

        if (job.title == null ||
                job.title.trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            "Job title is required"
                    ));
        }

        job.jobCode =
                generateJobCode();

        if (job.status == null ||
                job.status.isBlank()) {

            job.status = "Open";
        }

        JobOpening saved =
                jobRepository.save(job);

        return ResponseEntity.ok(saved);
    }

    @PutMapping("/jobs/{id}")
    public ResponseEntity<?> updateJob(
            @PathVariable Long id,
            @RequestBody JobOpening request
    ) {

        Optional<JobOpening> optional =
                jobRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        JobOpening job =
                optional.get();

        job.title = request.title;
        job.department = request.department;
        job.branch = request.branch;
        job.vacancies = request.vacancies;
        job.openingDate = request.openingDate;
        job.closingDate = request.closingDate;

        if (request.status != null) {
            job.status = request.status;
        }

        return ResponseEntity.ok(
                jobRepository.save(job)
        );
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<?> deleteJob(
            @PathVariable Long id
    ) {

        if (!jobRepository.existsById(id)) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        jobRepository.deleteById(id);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Job opening deleted successfully"
                )
        );
    }

    // =========================================================
    // CANDIDATES
    // =========================================================

    @GetMapping("/candidates")
    public ResponseEntity<?> getAllCandidates() {

        return ResponseEntity.ok(
                candidateRepository.findAll()
        );
    }

    @GetMapping("/candidates/{id}")
    public ResponseEntity<?> getCandidate(
            @PathVariable Long id
    ) {

        Optional<RecruitmentCandidate> candidate =
                candidateRepository.findById(id);

        if (candidate.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(
                candidate.get()
        );
    }

    // =========================================================
    // CREATE CANDIDATE
    // =========================================================

    @PostMapping("/candidates")
    public ResponseEntity<?> createCandidate(
            @RequestBody RecruitmentCandidate candidate
    ) {

        if (candidate.name == null ||
                candidate.name.trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            "Candidate name is required"
                    ));
        }

        candidate.applicationId =
                generateApplicationId();

        if (candidate.stage == null ||
                candidate.stage.isBlank()) {

            candidate.stage = "Applied";
        }

        if (candidate.status == null ||
                candidate.status.isBlank()) {

            candidate.status = "Active";
        }

        if (candidate.interviewResult == null) {
            candidate.interviewResult =
                    "Pending";
        }

        if (candidate.selectionStatus == null) {
            candidate.selectionStatus =
                    "Pending";
        }

        if (candidate.offerStatus == null) {
            candidate.offerStatus =
                    "Not Generated";
        }

        RecruitmentCandidate saved =
                candidateRepository.save(candidate);

        return ResponseEntity.ok(saved);
    }

    // =========================================================
    // UPDATE CANDIDATE
    // =========================================================

   @PutMapping("/candidates/{id}")
public ResponseEntity<?> updateCandidate(
        @PathVariable Long id,
        @RequestBody RecruitmentCandidate request
) {

    Optional<RecruitmentCandidate> optional =
            candidateRepository.findById(id);

    if (optional.isEmpty()) {
        return ResponseEntity
                .notFound()
                .build();
    }

    RecruitmentCandidate candidate =
            optional.get();

    // 🟢 FIX: Only overwrite when the incoming value is NOT null
    if (request.name != null) {
        candidate.name = request.name;
    }

    if (request.email != null) {
        candidate.email = request.email;
    }

    if (request.phone != null) {
        candidate.phone = request.phone;
    }

    if (request.role != null) {
        candidate.role = request.role;
    }

    if (request.branch != null) {
        candidate.branch = request.branch;
    }

    // 🟢 FIX: use resumeFileName, not resume
    if (request.resumeFileName != null) {
        candidate.resumeFileName = request.resumeFileName;
    }

    if (request.stage != null) {
        candidate.stage = request.stage;
    }

    if (request.status != null) {
        candidate.status = request.status;
    }

    if (request.interviewDate != null) {
        candidate.interviewDate = request.interviewDate;
    }

    if (request.interviewer != null) {
        candidate.interviewer = request.interviewer;
    }

    if (request.interviewResult != null) {
        candidate.interviewResult = request.interviewResult;
    }

    if (request.selectionStatus != null) {
        candidate.selectionStatus = request.selectionStatus;
    }

    if (request.offerStatus != null) {
        candidate.offerStatus = request.offerStatus;
    }

    if (request.joiningDate != null) {
        candidate.joiningDate = request.joiningDate;
    }

    return ResponseEntity.ok(
            candidateRepository.save(candidate)
    );
}
    // =========================================================
    // DELETE CANDIDATE
    // =========================================================

    @DeleteMapping("/candidates/{id}")
    public ResponseEntity<?> deleteCandidate(
            @PathVariable Long id
    ) {

        Optional<RecruitmentCandidate> candidate =
                candidateRepository.findById(id);

        if (candidate.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        RecruitmentCandidate existing =
                candidate.get();

        if (existing.resumeFileName != null) {

            try {

                Files.deleteIfExists(
                        uploadDirectory.resolve(
                                existing.resumeFileName
                        )
                );

            } catch (IOException ignored) {
            }
        }

        candidateRepository.deleteById(id);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Candidate deleted successfully"
                )
        );
    }

    // =========================================================
    // RESUME UPLOAD
    // =========================================================

    @PostMapping(
            value = "/candidates/{id}/resume",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadResume(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {

        Optional<RecruitmentCandidate> optional =
                candidateRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        if (file == null ||
                file.isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            "Resume file is required"
                    ));
        }

        String originalName =
                file.getOriginalFilename();

        if (originalName == null ||
                originalName.isBlank()) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            "Invalid file name"
                    ));
        }

        String lowerName =
                originalName.toLowerCase();

        if (!(
                lowerName.endsWith(".pdf") ||
                lowerName.endsWith(".doc") ||
                lowerName.endsWith(".docx")
        )) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            "Only PDF, DOC and DOCX files are allowed"
                    ));
        }

        RecruitmentCandidate candidate =
                optional.get();

        String extension =
                getExtension(originalName);

        String storedFileName =
                "resume_" +
                candidate.applicationId +
                "_" +
                UUID.randomUUID() +
                extension;

        Path target =
                uploadDirectory.resolve(
                        storedFileName
                );

        try {

            Files.copy(
                    file.getInputStream(),
                    target,
                    StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException e) {

            return ResponseEntity
                    .internalServerError()
                    .body(Map.of(
                            "message",
                            "Resume upload failed"
                    ));
        }

        candidate.resumeFileName =
                originalName;

        candidate.resumePath =
                target.toString();

        candidateRepository.save(candidate);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Resume uploaded successfully",
                        "fileName",
                        originalName,
                        "applicationId",
                        candidate.applicationId
                )
        );
    }

    // =========================================================
    // DOWNLOAD / VIEW RESUME
    // =========================================================

    @GetMapping("/candidates/{id}/resume")
    public ResponseEntity<?> getResume(
            @PathVariable Long id
    ) {

        Optional<RecruitmentCandidate> optional =
                candidateRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        RecruitmentCandidate candidate =
                optional.get();

        if (candidate.resumeFileName == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        Path file =
                uploadDirectory.resolve(
                        Paths.get(
                                candidate.resumePath
                        ).getFileName()
                );

        if (!Files.exists(file)) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        try {

            Resource resource =
                    new UrlResource(
                            file.toUri()
                    );

            String contentType =
                    Files.probeContentType(file);

            if (contentType == null) {
                contentType =
                        MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(
                            MediaType.parseMediaType(
                                    contentType
                            )
                    )
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" +
                                    candidate.resumeFileName +
                                    "\""
                    )
                    .body(resource);

        } catch (Exception e) {

            return ResponseEntity
                    .internalServerError()
                    .body(Map.of(
                            "message",
                            "Unable to open resume"
                    ));
        }
    }

    // =========================================================
    // STATUS UPDATE
    // =========================================================

    @PutMapping("/candidates/{id}/stage")
    public ResponseEntity<?> updateStage(
            @PathVariable Long id,
            @RequestParam String stage
    ) {

        Optional<RecruitmentCandidate> optional =
                candidateRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        RecruitmentCandidate candidate =
                optional.get();

        candidate.stage = stage;

        if ("Rejected".equalsIgnoreCase(stage)) {

            candidate.status = "Closed";

            candidate.selectionStatus =
                    "Rejected";

        } else if ("Joined".equalsIgnoreCase(stage)) {

            candidate.status = "Joined";

        } else if ("Offer".equalsIgnoreCase(stage)) {

            candidate.status = "Pending";

        } else {

            candidate.status = "Active";
        }

        return ResponseEntity.ok(
                candidateRepository.save(candidate)
        );
    }

    // =========================================================
    // INTERVIEW
    // =========================================================

    @PutMapping("/candidates/{id}/interview")
    public ResponseEntity<?> scheduleInterview(
            @PathVariable Long id,
            @RequestParam String interviewDate,
            @RequestParam String interviewer
    ) {

        Optional<RecruitmentCandidate> optional =
                candidateRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        RecruitmentCandidate candidate =
                optional.get();

        candidate.stage = "Interview";
        candidate.status = "Active";

        candidate.interviewDate =
                LocalDate.parse(interviewDate);

        candidate.interviewer =
                interviewer;

        candidate.interviewResult =
                "Pending";

        return ResponseEntity.ok(
                candidateRepository.save(candidate)
        );
    }

    // =========================================================
    // SELECT CANDIDATE
    // =========================================================

    @PutMapping("/candidates/{id}/select")
    public ResponseEntity<?> selectCandidate(
            @PathVariable Long id
    ) {

        Optional<RecruitmentCandidate> optional =
                candidateRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        RecruitmentCandidate candidate =
                optional.get();

        candidate.stage = "Selected";

        candidate.status = "Active";

        candidate.selectionStatus =
                "Selected";

        candidate.interviewResult =
                "Selected";

        return ResponseEntity.ok(
                candidateRepository.save(candidate)
        );
    }

    // =========================================================
    // REJECT CANDIDATE
    // =========================================================

    @PutMapping("/candidates/{id}/reject")
    public ResponseEntity<?> rejectCandidate(
            @PathVariable Long id
    ) {

        Optional<RecruitmentCandidate> optional =
                candidateRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        RecruitmentCandidate candidate =
                optional.get();

        candidate.stage = "Rejected";

        candidate.status = "Closed";

        candidate.selectionStatus =
                "Rejected";

        candidate.interviewResult =
                "Rejected";

        return ResponseEntity.ok(
                candidateRepository.save(candidate)
        );
    }

    // =========================================================
    // OFFER LETTER
    // =========================================================

    @PutMapping("/candidates/{id}/offer")
    public ResponseEntity<?> generateOffer(
            @PathVariable Long id
    ) {

        Optional<RecruitmentCandidate> optional =
                candidateRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        RecruitmentCandidate candidate =
                optional.get();

        candidate.stage = "Offer";

        candidate.status = "Pending";

        candidate.offerStatus =
                "Sent";

        candidate.offerDate =
                LocalDate.now();

        RecruitmentCandidate saved =
                candidateRepository.save(candidate);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Offer letter generated successfully",
                        "candidate",
                        saved
                )
        );
    }

    // =========================================================
    // OFFER ACCEPT
    // =========================================================

    @PutMapping("/candidates/{id}/offer/accept")
    public ResponseEntity<?> acceptOffer(
            @PathVariable Long id
    ) {

        Optional<RecruitmentCandidate> optional =
                candidateRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        RecruitmentCandidate candidate =
                optional.get();

        candidate.offerStatus =
                "Accepted";

        candidate.status =
                "Active";

        return ResponseEntity.ok(
                candidateRepository.save(candidate)
        );
    }

    // =========================================================
    // JOINING
    // =========================================================

    @PutMapping("/candidates/{id}/joining")
    public ResponseEntity<?> markJoining(
            @PathVariable Long id,
            @RequestParam(required = false)
                    String joiningDate
    ) {

        Optional<RecruitmentCandidate> optional =
                candidateRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        RecruitmentCandidate candidate =
                optional.get();

        candidate.stage = "Joined";

        candidate.status = "Joined";

        candidate.offerStatus =
                "Accepted";

        if (joiningDate == null ||
                joiningDate.isBlank()) {

            candidate.joiningDate =
                    LocalDate.now();

        } else {

            candidate.joiningDate =
                    LocalDate.parse(
                            joiningDate
                    );
        }

        return ResponseEntity.ok(
                candidateRepository.save(candidate)
        );
    }

    // =========================================================
    // DASHBOARD STATISTICS
    // =========================================================

    @GetMapping("/stats")
    public ResponseEntity<?> getStatistics() {

        List<RecruitmentCandidate> candidates =
                candidateRepository.findAll();

        List<JobOpening> jobs =
                jobRepository.findAll();

        long openings =
                jobs.stream()
                        .filter(j ->
                                "Open".equalsIgnoreCase(
                                        j.status
                                )
                        )
                        .count();

        long applicants =
                candidates.size();

        long shortlisted =
                candidates.stream()
                        .filter(c ->
                                "Shortlisted".equalsIgnoreCase(
                                        c.stage
                                ) ||
                                "Interview".equalsIgnoreCase(
                                        c.stage
                                ) ||
                                "Selected".equalsIgnoreCase(
                                        c.stage
                                ) ||
                                "Offer".equalsIgnoreCase(
                                        c.stage
                                )
                        )
                        .count();

        long interviewing =
                candidates.stream()
                        .filter(c ->
                                "Interview".equalsIgnoreCase(
                                        c.stage
                                )
                        )
                        .count();

        long rejected =
                candidates.stream()
                        .filter(c ->
                                "Rejected".equalsIgnoreCase(
                                        c.stage
                                )
                        )
                        .count();

        long offers =
                candidates.stream()
                        .filter(c ->
                                "Sent".equalsIgnoreCase(
                                        c.offerStatus
                                ) ||
                                "Accepted".equalsIgnoreCase(
                                        c.offerStatus
                                )
                        )
                        .count();

        long joined =
                candidates.stream()
                        .filter(c ->
                                "Joined".equalsIgnoreCase(
                                        c.stage
                                )
                        )
                        .count();

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put("openings", openings);
        response.put("applicants", applicants);
        response.put("shortlisted", shortlisted);
        response.put("interviewing", interviewing);
        response.put("rejected", rejected);
        response.put("offers", offers);
        response.put("joined", joined);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // SEARCH CANDIDATE
    // =========================================================

    @GetMapping("/candidates/search")
    public ResponseEntity<?> searchCandidates(
            @RequestParam String keyword
    ) {

        List<RecruitmentCandidate> all =
                candidateRepository.findAll();

        String search =
                keyword.toLowerCase().trim();

        List<RecruitmentCandidate> result =
                all.stream()
                        .filter(c ->
                                contains(c.name, search) ||
                                contains(c.email, search) ||
                                contains(c.phone, search) ||
                                contains(c.role, search) ||
                                contains(c.branch, search) ||
                                contains(c.applicationId, search)
                        )
                        .toList();

        return ResponseEntity.ok(result);
    }

    // =========================================================
    // HELPER METHODS
    // =========================================================

    private String generateJobCode() {

        String code;

        do {

            code =
                    "JOB-" +
                    (1000 +
                            new Random()
                                    .nextInt(9000));

        } while (
                jobRepository.existsByJobCode(code)
        );

        return code;
    }

    private String generateApplicationId() {

        String code;

        do {

            code =
                    "AP-" +
                    (1000 +
                            new Random()
                                    .nextInt(9000));

        } while (
                candidateRepository
                        .existsByApplicationId(code)
        );

        return code;
    }

    private String getExtension(
            String fileName
    ) {

        int index =
                fileName.lastIndexOf(".");

        if (index == -1) {
            return "";
        }

        return fileName.substring(index);
    }

    private boolean contains(
            String value,
            String search
    ) {

        return value != null &&
                value.toLowerCase()
                        .contains(search);
    }
}