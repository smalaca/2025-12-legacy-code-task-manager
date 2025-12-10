package com.smalaca.taskmanager.projectmanagement.presentation.api;

import com.smalaca.taskamanager.dto.ProjectDto;
import com.smalaca.taskmanager.projectmanagement.business.project.CreateProjectResponse;
import com.smalaca.taskmanager.projectmanagement.business.project.ProjectService;
import com.smalaca.taskmanager.projectmanagement.business.project.ProjectView;

import java.util.List;

public class ProjectManagementClient {
    private final ProjectService projectService;

    public ProjectManagementClient(ProjectService projectService) {
        this.projectService = projectService;
    }

    public List<ProjectView> findAllProjects() {
        return projectService.findAll();
    }

    public CreateProjectResponse createProject(ProjectDto projectDto) {
        return projectService.createProject(projectDto);
    }

    public ProjectDto updateProject(Long id, ProjectDto projectDto) {
        return projectService.updateProject(id, projectDto);
    }
}
