package com.smalaca.acl.legacycode;

import com.smalaca.taskamanager.dto.ProjectDto;
import com.smalaca.taskamanager.repository.ProjectRepository;
import com.smalaca.taskmanager.projectmanagement.business.project.*;
import com.smalaca.taskmanager.projectmanagement.presentation.api.ProjectManagementClient;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class AclProjectManagementClient {
    private final ProjectManagementClient projectManagementClient;

    private AclProjectManagementClient(ProjectManagementClient projectManagementClient) {
        this.projectManagementClient = projectManagementClient;
    }

    public static AclProjectManagementClient create(ProjectRepository projectRepository) {
        ProjectManagementClient projectManagementClient = new ProjectManagementClient(new ProjectService(projectRepository));
        return new AclProjectManagementClient(projectManagementClient);
    }

    public List<ProjectDto> findAllProjects() {
        return asDtos(projectManagementClient.findAllProjects());
    }

    private List<ProjectDto> asDtos(List<ProjectView> projectsViews) {
        return projectsViews.stream()
                .map(this::asDto)
                .collect(toList());
    }

    public CreateProjectResponse createProject(ProjectDto projectDto) {
        CreateProjectCommand command = new CreateProjectCommand(projectDto.getName());
        return projectManagementClient.createProject(command);
    }

    public ProjectDto updateProject(Long id, ProjectDto projectDto) {
        UpdateProjectCommand command = new UpdateProjectCommand(id, projectDto.getProjectStatus());
        return asDto(projectManagementClient.updateProject(command));
    }

    private ProjectDto asDto(ProjectView projectView) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(projectView.getId());
        projectDto.setName(projectView.getName());
        projectDto.setProjectStatus(projectView.getProjectStatus());
        projectDto.setProductOwnerId(projectView.getProductOwnerId());
        projectDto.setTeamIds(projectView.getTeamIds());
        return projectDto;
    }
}
