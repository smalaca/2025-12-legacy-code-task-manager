package com.smalaca.taskmanager.projectmanagement.presentation.api;

import com.smalaca.taskamanager.dto.ProjectDto;
import com.smalaca.taskmanager.projectmanagement.business.project.CreateProjectResponse;
import com.smalaca.taskmanager.projectmanagement.business.project.ProjectService;
import com.smalaca.taskmanager.projectmanagement.business.project.ProjectView;
import com.smalaca.taskmanager.projectmanagement.business.project.UpdateProjectCommand;

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

    public ProjectView updateProject(UpdateProjectCommand command) {
        return projectService.updateProject(command);
    }
}
