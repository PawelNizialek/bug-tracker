package com.example.ztpai.service;

import com.example.ztpai.dto.ProjectRequest;
import com.example.ztpai.dto.ProjectResponse;
import com.example.ztpai.exception.ZtpaiAppException;
import com.example.ztpai.model.Project;
import com.example.ztpai.project.Status;
import com.example.ztpai.repository.ProjectRepository;
import com.example.ztpai.repository.TicketRepository;
import com.example.ztpai.ticket.TicketStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TicketRepository ticketRepository;

    public ProjectService(ProjectRepository projectRepository, TicketRepository ticketRepository) {
        this.projectRepository = projectRepository;
        this.ticketRepository = ticketRepository;
    }
    public List<ProjectResponse> findAllProjects() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectResponse> projectResponses = new ArrayList<>();
        for(Project project: projects){
            ProjectResponse projectResponse = ProjectResponse.builder()
                    .description(project.getDescription())
                    .name(project.getName())
                    .projectId(project.getProjectId())
                    .status(project.getStatus().toString())
                    .build();
            projectResponses.add(projectResponse);
        }
        return projectResponses;
    }
    public ProjectResponse addProject(ProjectRequest projectRequest){
        Project project = Project.builder()
                .description(projectRequest.getDescription())
                .name(projectRequest.getName())
                .status(Status.PROGRESS)
                .startDate(LocalDateTime.now())
                .build();
        projectRepository.save(project);
        return null;
    }
    public void deleteProject(Long projectId){
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ZtpaiAppException("project not found")
        );
        for (int i = 0; i < project.getTickets().size(); i++) {
            ticketRepository.delete(project.getTickets().get(i));
        }
        projectRepository.delete(project);
    }
    public ProjectResponse updateProject(ProjectRequest projectRequest){
        Project storedProject = projectRepository.findById(projectRequest.getProjectId()).orElseThrow(
                () -> new ZtpaiAppException("project not found")
        );
        storedProject.setName(projectRequest.getName());
        storedProject.setDescription(projectRequest.getDescription());
        if(storedProject.getStatus() == Status.PROGRESS)
            projectRepository.save(storedProject);
        return new ProjectResponse(storedProject.getProjectId(), storedProject.getName(),
                storedProject.getDescription(), storedProject.getStatus().toString(),
                storedProject.getStartDate(), storedProject.getEndDate());
    }
    public void closeProject(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ZtpaiAppException("project not found")
        );
        project.setStatus(Status.DONE);
        project.setEndDate(LocalDateTime.now());
        for (int i = 0; i < project.getTickets().size(); i++) {
            project.getTickets().get(i).setStatus(TicketStatus.CLOSE);
        }
        projectRepository.save(project);
    }
}
