package com.example.ztpai.controller;

import com.example.ztpai.model.Project;
import com.example.ztpai.model.User;
import com.example.ztpai.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/myprojects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/showallprojects")
    public List<Project> showAllProjects(){
        return projectService.showAllProjects();
    }

    @PostMapping
    public ResponseEntity register(@RequestBody Project project){
        projectService.addProject(project);
        return new ResponseEntity(HttpStatus.OK);
    }
}
