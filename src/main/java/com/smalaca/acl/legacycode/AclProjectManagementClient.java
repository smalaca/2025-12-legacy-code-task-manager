package com.smalaca.acl.legacycode;

import com.smalaca.taskamanager.dto.ProjectDto;
import com.smalaca.taskmanager.projectmanagement.business.project.ProjectView;
import com.smalaca.taskmanager.projectmanagement.presentation.api.ProjectManagementClient;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class AclProjectManagementClient {
    private final ProjectManagementClient projectManagementClient;

    public AclProjectManagementClient(ProjectManagementClient projectManagementClient) {
        this.projectManagementClient = projectManagementClient;
    }

    public List<ProjectDto> findAllProjects() {
        return asDtos(projectManagementClient.findAllProjects());
    }

    private List<ProjectDto> asDtos(List<ProjectView> projectsViews) {
        return projectsViews.stream()
                .map(this::asDto)
                .collect(toList());
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
