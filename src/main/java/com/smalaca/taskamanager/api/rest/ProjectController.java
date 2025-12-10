package com.smalaca.taskamanager.api.rest;

import com.google.common.annotations.VisibleForTesting;
import com.smalaca.taskamanager.dto.ProjectDto;
import com.smalaca.taskamanager.exception.ProjectNotFoundException;
import com.smalaca.taskamanager.exception.TeamNotFoundException;
import com.smalaca.taskamanager.model.entities.Project;
import com.smalaca.taskamanager.model.entities.Team;
import com.smalaca.taskamanager.model.enums.ProjectStatus;
import com.smalaca.taskamanager.repository.ProjectRepository;
import com.smalaca.taskamanager.repository.TeamRepository;
import com.smalaca.taskmanager.projectmanagement.presentation.api.ProjectManagementClient;
import com.smalaca.taskmanager.projectmanagement.presentation.api.ProjectManagementClientFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;
    private final ProjectManagementClient projectManagementClient;

    public ProjectController(ProjectRepository projectRepository, TeamRepository teamRepository) {
        this.projectRepository = projectRepository;
        this.teamRepository = teamRepository;
        projectManagementClient = new ProjectManagementClientFactory().create(projectRepository);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        List<ProjectDto> projectsDtos = projectManagementClient.findAllProjects();

        return new ResponseEntity<>(projectsDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @Transactional
    public ResponseEntity<ProjectDto> getProject(@PathVariable("id") Long id) {
        try {
            Project project = getProjectById(id);

            ProjectDto projectDto = new ProjectDto();
            projectDto.setId(project.getId());
            projectDto.setName(project.getName());
            projectDto.setProjectStatus(project.getProjectStatus().name());

            if (project.getProductOwner() != null) {
                projectDto.setProductOwnerId(project.getProductOwner().getId());
            }

            List<Long> ids = project
                    .getTeams()
                    .stream()
                    .map(Team::getId)
                    .collect(Collectors.toList());

            projectDto.setTeamIds(ids);

            return new ResponseEntity<>(projectDto, HttpStatus.OK);
        } catch (ProjectNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Void> createProject(@RequestBody ProjectDto projectDto, UriComponentsBuilder uriComponentsBuilder) {
        if (exists(projectDto)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            return createNewProject(projectDto, uriComponentsBuilder);
        }
    }

    @VisibleForTesting
    ResponseEntity<Void> createNewProject(ProjectDto projectDto, UriComponentsBuilder uriComponentsBuilder) {
        Project project = new Project();

        boolean isValidName = updateIfNameIsValid(projectDto, project);

        if (!isValidName) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Project saved = projectRepository.save(project);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponentsBuilder.path("/project/{id}").buildAndExpand(saved.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @VisibleForTesting
    boolean updateIfNameIsValid(ProjectDto projectDto, Project project) {
        boolean isValidName = projectDto.getName().length() >= 5;

        if (isValidName) {
            project.setName(projectDto.getName());
        }

        return isValidName;
    }

    private boolean exists(ProjectDto projectDto) {
        return !projectRepository.findByName(projectDto.getName()).isEmpty();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable("id") Long id, @RequestBody ProjectDto projectDto) {
        try {
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

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ProjectNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable("id") Long id) {
        try {
            projectRepository.delete(getProjectById(id));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ProjectNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{projectId}/teams/{teamId}")
    @Transactional
    public ResponseEntity<Void> addTeam(@PathVariable Long projectId, @PathVariable Long teamId) {
        try {
            Project project = getProjectById(projectId);

            try {
                Team team = getTeamById(teamId);

                project.addTeam(team);
                team.setProject(project);

                projectRepository.save(project);
                teamRepository.save(team);

                return new ResponseEntity<>(HttpStatus.OK);
            } catch (TeamNotFoundException exception) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (ProjectNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{projectId}/teams/{teamId}")
    @Transactional
    public ResponseEntity<Void> removeTeam(@PathVariable Long projectId, @PathVariable Long teamId) {
        try {
            Project project = getProjectById(projectId);

            try {
                Team team = getTeamById(teamId);

                project.removeTeam(team);
                team.setProject(null);

                projectRepository.save(project);
                teamRepository.save(team);

                return new ResponseEntity<>(HttpStatus.OK);
            } catch (TeamNotFoundException exception) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (ProjectNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private Project getProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
    }

    private Team getTeamById(Long id) {
        Optional<Team> team = teamRepository.findById(id);

        if (team.isEmpty()) {
            throw new TeamNotFoundException();
        }

        return team.get();
    }
}
