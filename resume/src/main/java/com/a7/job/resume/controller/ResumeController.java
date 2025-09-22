package com.a7.job.resume.controller;

import com.a7.job.resume.dto.ResumeDto;
import com.a7.job.resume.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/resume")
public class ResumeController {
    private final ResumeService resumeService;

    @Autowired
    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResumeDto> uploadResume(@RequestPart("resume") MultipartFile resumeDoc) {
        Optional<ResumeDto> resumeOpt = resumeService.parseResume(resumeDoc);
        if (resumeOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        ResumeDto resumeDto = resumeService.createResume(resumeOpt.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(resumeDto);
    }

    @GetMapping("/download/{resumeId}")
    public ResponseEntity<ResumeDto> downloadResume(@PathVariable String id) {
        return ResponseEntity.ok(new ResumeDto());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResumeDto> getUserResume(@PathVariable String userId) {
        return ResponseEntity.ok(new ResumeDto());
    }

    @PutMapping("/update/{resumeId}")
    public ResponseEntity<ResumeDto> updateResume(@PathVariable String resumeId, @RequestBody String resume) {
        return ResponseEntity.ok(new ResumeDto());
    }

    @DeleteMapping("/delete/{resumeId}")
    public ResponseEntity<String> deleteResume(@PathVariable String resumeId) {
        return ResponseEntity.ok("Resume deleted for ID: " + resumeId);
    }

    @GetMapping("/skills/{skillName}")
    public ResponseEntity<ResumeDto> searchResumesBySkill(@PathVariable String skillName) {
        return ResponseEntity.ok(new ResumeDto());
    }
}
