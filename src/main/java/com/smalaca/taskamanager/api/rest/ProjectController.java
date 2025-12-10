package com.smalaca.taskamanager.api.rest;

import com.smalaca.taskamanager.dto.ProjectDto;
import com.smalaca.taskamanager.exception.ProjectNotFoundException;
import com.smalaca.taskamanager.exception.TeamNotFoundException;
import com.smalaca.taskamanager.model.entities.Project;
import com.smalaca.taskamanager.model.entities.Team;
import com.smalaca.taskamanager.repository.ProjectRepository;
import com.smalaca.taskamanager.repository.TeamRepository;
import com.smalaca.taskmanager.projectmanagement.business.project.CreateProjectResponse;
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
        CreateProjectResponse response = projectManagementClient.createProject(projectDto);

        if (response.isExists()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (!response.isValidName()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponentsBuilder.path("/project/{id}").buildAndExpand(response.getProjectId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable("id") Long id, @RequestBody ProjectDto projectDto) {
        try {
            ProjectDto response = projectManagementClient.updateProject(id, projectDto);
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
