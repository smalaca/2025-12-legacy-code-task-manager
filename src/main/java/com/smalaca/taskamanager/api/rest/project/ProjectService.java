package com.smalaca.taskamanager.api.rest.project;

import com.smalaca.taskamanager.dto.ProjectDto;
import com.smalaca.taskamanager.repository.ProjectRepository;

import java.util.ArrayList;
import java.util.List;

public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<ProjectDto> findAllProject() {
        List<ProjectDto> projectsDtos = new ArrayList<>();

        projectRepository.findAll()
                .forEach(project -> projectsDtos.add(project.asDto()));

        return projectsDtos;
    }
}
