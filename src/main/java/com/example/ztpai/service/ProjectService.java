package com.example.ztpai.service;

import com.example.ztpai.model.Project;
import com.example.ztpai.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> showAllProjects(){
        return projectRepository.findAll();
    }

    public void addProject(Project project){
        projectRepository.save(project);
    }
}
