package com.example.demo.controller;

import com.example.demo.entity.Skills;
import com.example.demo.services.SkillService;
import com.example.demo.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Base64;

@RestController
@RequestMapping("/api/skills")
@CrossOrigin(origins = "http://localhost:3000")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Skills>>> getAllSkills() {
        List<Skills> skills = skillService.getAllSkills();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(HttpStatus.OK.value(), "Success", skills));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Skills>> getSkillById(@PathVariable UUID id) {
        Optional<Skills> skill = skillService.getSkillById(id);
        if (skill.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(HttpStatus.OK.value(), "Success", skill.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Failure", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Skills>> createSkill(@RequestParam("name") String name,
                                                           @RequestParam("image") MultipartFile image) throws IOException {
        Skills skill = new Skills();
        skill.setName(name);

        String mimeType = image.getContentType();
        if (mimeType == null || !mimeType.startsWith("image/")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Failure: Invalid file type", null));
        }

        String base = "data:" + mimeType + ";base64,";
        String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
        skill.setImage(base + base64Image);

        Skills savedSkill = skillService.createSkill(skill);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(HttpStatus.CREATED.value(), "Success", savedSkill));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Skills>> updateSkill(@PathVariable UUID id,
                                                           @RequestParam(value = "name", required = false) String name,
                                                           @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        Optional<Skills> existingSkillOpt = skillService.getSkillById(id);

        if (existingSkillOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Failure: Skill not found", null));
        }

        Skills existingSkill = existingSkillOpt.get();

        if (name != null) {
            existingSkill.setName(name);
        }

        if (image != null) {
            String mimeType = image.getContentType();
            if (mimeType == null || !mimeType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Failure: Invalid file type", null));
            }

            String base = "data:" + mimeType + ";base64,";
            String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
            existingSkill.setImage(base + base64Image);
        }

        Skills updatedSkill = skillService.updateSkill(id, existingSkill).orElse(null);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(HttpStatus.OK.value(), "Success", updatedSkill));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSkill(@PathVariable UUID id) {
        boolean deleted = skillService.deleteSkill(id);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Success", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Failure: Skill not found", null));
        }
    }
}
