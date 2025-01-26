package com.example.demo.controller;

import com.example.demo.entity.Project;
import com.example.demo.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<Project>> getProjects() {
        List<Project> projects = projectService.getAllProjects();
        if (projects.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Project> addProject(@RequestBody Project project) {
        try {
            Project savedProject = projectService.saveProject(project);
            return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
