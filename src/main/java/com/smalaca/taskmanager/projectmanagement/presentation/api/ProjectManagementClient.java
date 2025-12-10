package com.smalaca.taskmanager.projectmanagement.presentation.api;

import com.smalaca.taskamanager.dto.ProjectDto;
import com.smalaca.taskmanager.projectmanagement.business.ProjectService;

import java.util.List;

public class ProjectManagementClient {
    private final ProjectService projectService;

    ProjectManagementClient(ProjectService projectService) {
        this.projectService = projectService;
    }

    public List<ProjectDto> findAllProjects() {
        return projectService.findAll();
    }
}
