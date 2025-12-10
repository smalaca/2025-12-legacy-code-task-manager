package com.smalaca.taskmanager.projectmanagement.business.project;

import com.smalaca.taskamanager.dto.ProjectDto;
import com.smalaca.taskamanager.exception.ProjectNotFoundException;
import com.smalaca.taskamanager.model.entities.Project;
import com.smalaca.taskamanager.model.enums.ProjectStatus;
import com.smalaca.taskamanager.repository.ProjectRepository;

import java.util.ArrayList;
import java.util.List;

public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<ProjectView> findAll() {
        List<ProjectView> projectsDtos = new ArrayList<>();

        projectRepository.findAll()
                .forEach(project -> projectsDtos.add(asView(project.asDto())));

        return projectsDtos;
    }

    private ProjectView asView(ProjectDto dto) {
        ProjectView projectView = new ProjectView();
        projectView.setId(dto.getId());
        projectView.setName(dto.getName());
        projectView.setProjectStatus(dto.getProjectStatus());
        projectView.setProductOwnerId(dto.getProductOwnerId());
        projectView.setTeamIds(dto.getTeamIds());
        return projectView;
    }

    public CreateProjectResponse createProject(ProjectDto projectDto) {
        boolean exists = projectRepository.findByName(projectDto.getName()).isPresent();
        CreateProjectResponse response = new CreateProjectResponse();
        response.setExists(exists);

        if (!response.isExists()) {
            Project project = new Project();

            boolean isValidName = projectDto.getName().length() >= 5;
            response.setValidName(isValidName);


            if (response.isValidName()) {
                project.setName(projectDto.getName());

                Project saved = projectRepository.save(project);

                response.setProjectId(saved.getId());
            }
        }

        return response;
    }

    public ProjectDto updateProject(Long id, ProjectDto projectDto) {
        Project project = getProjectById(id);
        project.setProjectStatus(ProjectStatus.valueOf(projectDto.getProjectStatus()));

        Project updated = projectRepository.save(project);

        ProjectDto response = new ProjectDto();
        response.setId(updated.getId());
        response.setName(updated.getName());
        response.setProjectStatus(updated.getProjectStatus().name());

        if (updated.getProductOwner() != null) {
            response.setProductOwnerId(updated.getProductOwner().getId());
        }
        return response;
    }

    private Project getProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
    }
}
