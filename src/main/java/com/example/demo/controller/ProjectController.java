package com.example.demo.controller;

import com.example.demo.entity.Project;
import com.example.demo.services.ProjectService;
import com.example.demo.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Project>>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Success", projects)
        );
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Project>> addProject(@RequestParam(value = "name") String name,
                                                           @RequestParam(value = "description", required = false) String description,
                                                           @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        try {
            Project project = new Project();
            project.setName(name);
            project.setDescription(description);
            if (imageFile != null ) {
                String mimeType = imageFile.getContentType();
                if (mimeType == null || !mimeType.startsWith("image/")) {
                    throw new IllegalArgumentException("Invalid file type. Only image files are allowed.");
                }

                String base = "data:" + mimeType + ";base64,";
                System.out.println(base);
                String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());
                String encode = base + base64Image;
                project.setImage(encode);
            }

            Project savedProject = projectService.saveProject(project);

            return ResponseEntity.status(201).body(
                    new ApiResponse<>(201, "Success", savedProject)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(500, "Failure", null)
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Project>> getProjectById(@PathVariable UUID id) {
        Optional<Project> project = projectService.getProjectById(id);
        if (project.isPresent()) {
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Success", project.get())
            );
        }
        return ResponseEntity.status(404).body(
                new ApiResponse<>(404, "Project not found", null)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Project>> updateProject(
            @PathVariable UUID id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        Optional<Project> existingProjectOpt = projectService.getProjectById(id);
        if (existingProjectOpt.isPresent()) {
            Project existingProject = existingProjectOpt.get();

            if (name != null) {
                existingProject.setName(name);
            }

            // Update description if provided
            if (description != null) {
                existingProject.setDescription(description);
            }

            // Update image if provided
            if (imageFile != null) {
                try {
                    System.out.println("image is here");
                    String mimeType = imageFile.getContentType();
                    if (mimeType == null || !mimeType.startsWith("image/")) {
                        return ResponseEntity.badRequest().body(
                                new ApiResponse<>(400, "Invalid image type", null)
                        );
                    }

                    String base = "data:" + mimeType + ";base64,";
                    System.out.println(base);
                    String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());
                    existingProject.setImage(base + base64Image);
                } catch (IOException e) {
                    return ResponseEntity.status(500).body(
                            new ApiResponse<>(500, "Error processing image file", null)
                    );
                }
            }

            Project updatedProject = projectService.saveProject(existingProject);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Success", updatedProject)
            );
        } else {
            return ResponseEntity.status(404).body(
                    new ApiResponse<>(404, "Project not found", null)
            );
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable UUID id) {
        try {
            projectService.deleteProject(id);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Success", null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(404).body(
                    new ApiResponse<>(404, "Failure", null)
            );
        }
    }
}
