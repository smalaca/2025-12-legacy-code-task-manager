package com.smalaca.taskmanager.projectmanagement.presentation.api;

import com.smalaca.taskmanager.projectmanagement.business.project.*;

import java.util.List;

public class ProjectManagementClient {
    private final ProjectService projectService;

    public ProjectManagementClient(ProjectService projectService) {
        this.projectService = projectService;
    }

    public List<ProjectView> findAllProjects() {
        return projectService.findAll();
    }

    public CreateProjectResponse createProject(CreateProjectCommand command) {
        return projectService.createProject(command);
    }

    public ProjectView updateProject(UpdateProjectCommand command) {
        return projectService.updateProject(command);
    }
}
