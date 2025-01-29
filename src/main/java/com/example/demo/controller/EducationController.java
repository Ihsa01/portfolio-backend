package com.example.demo.controller;

import com.example.demo.entity.Education;
import com.example.demo.services.EducationService;
import com.example.demo.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/education")
@CrossOrigin(origins = "http://localhost:3000")
public class EducationController {

    @Autowired
    private EducationService educationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Education>>> getAllEducation() {
        List<Education> educationList = educationService.getAllEducation();
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", educationList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Education>> getEducationById(@PathVariable UUID id) {
        Optional<Education> education = educationService.getEducationById(id);
        if (education.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", education.get()));
        } else {
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Failure", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Education>> createEducation(@RequestParam("std") String std,
                                                                  @RequestParam("school") String school) {
        Education education = new Education();
        education.setStd(std);
        education.setSchool(school);

        Education savedEducation = educationService.createEducation(education);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED.value(), "Success", savedEducation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Education>> updateEducation(@PathVariable UUID id,
                                                                  @RequestParam(value = "std", required = false) String std,
                                                                  @RequestParam(value = "school", required = false) String school) {
        Optional<Education> existingEducationOpt = educationService.getEducationById(id);

        if (existingEducationOpt.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Failure", null));
        }

        Education existingEducation = existingEducationOpt.get();

        if (std != null) {
            existingEducation.setStd(std);
        }

        if (school != null) {
            existingEducation.setSchool(school);
        }

        Education updatedEducation = educationService.updateEducation(id, existingEducation).orElse(null);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", updatedEducation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEducation(@PathVariable UUID id) {
        boolean deleted = educationService.deleteEducation(id);
        if (deleted) {
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Success", null));
        } else {
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Failure", null));
        }
    }
}
