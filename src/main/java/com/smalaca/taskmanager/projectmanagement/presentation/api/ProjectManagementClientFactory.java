package com.smalaca.taskmanager.projectmanagement.presentation.api;

import com.smalaca.taskamanager.repository.ProjectRepository;
import com.smalaca.taskmanager.projectmanagement.business.project.ProjectService;

public class ProjectManagementClientFactory {
    public ProjectManagementClient create(ProjectRepository projectRepository) {
        return new ProjectManagementClient(new ProjectService(projectRepository));
    }
}
