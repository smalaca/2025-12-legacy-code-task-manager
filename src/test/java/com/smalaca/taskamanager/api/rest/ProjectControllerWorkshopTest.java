package com.smalaca.taskamanager.api.rest;

import com.smalaca.taskamanager.dto.ProjectDto;
import com.smalaca.taskamanager.model.entities.Project;
import com.smalaca.taskamanager.repository.ProjectRepository;
import com.smalaca.taskamanager.repository.TeamRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

class ProjectControllerWorkshopTest {
    private final ProjectRepository projectRepository = Mockito.mock(ProjectRepository.class);
    private final TeamRepository teamRepository = Mockito.mock(TeamRepository.class);
    private final ProjectController projectController = new ProjectController(projectRepository, teamRepository);

    @Test
    void shouldReturnHttpStatusBadRequestWhenProjectNameIncorrect() {
        Project saved = new Project();
        saved.setId(13L);
        String incorrectProjectName = "inco";
        BDDMockito.given(projectRepository.findByName(incorrectProjectName)).willReturn(Optional.empty());
        BDDMockito.given(projectRepository.save(Mockito.any())).willReturn(saved);
        UriComponentsBuilder uriComponentsBuilder = fromUriString("/");
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName(incorrectProjectName);

        ResponseEntity<Void> actual = projectController.createProject(projectDto, uriComponentsBuilder);

        Assertions.assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldNotCreateProjectWhenProjectNameIncorrect() {
        String incorrectProjectName = "inco";
        BDDMockito.given(projectRepository.findByName(incorrectProjectName)).willReturn(Optional.empty());
        UriComponentsBuilder uriComponentsBuilder = fromUriString("/");
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName(incorrectProjectName);

        projectController.createProject(projectDto, uriComponentsBuilder);

        BDDMockito.then(projectRepository).should(Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldReturnHttpStatusCreatedWhenProjectNameCorrect() {
        Project saved = new Project();
        saved.setId(13L);
        String correctProjectName = "legacy rearch";
        BDDMockito.given(projectRepository.findByName(correctProjectName)).willReturn(Optional.empty());
        BDDMockito.given(projectRepository.save(Mockito.any())).willReturn(saved);
        UriComponentsBuilder uriComponentsBuilder = fromUriString("/");
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName(correctProjectName);

        ResponseEntity<Void> actual = projectController.createProject(projectDto, uriComponentsBuilder);

        Assertions.assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }

    @Test
    void shouldCreateProjectWhenProjectNameCorrect() {
        Project saved = new Project();
        saved.setId(13L);
        String correctProjectName = "legacy rearch";
        BDDMockito.given(projectRepository.findByName(correctProjectName)).willReturn(Optional.empty());
        BDDMockito.given(projectRepository.save(Mockito.any())).willReturn(saved);
        UriComponentsBuilder uriComponentsBuilder = fromUriString("/");
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName(correctProjectName);

        projectController.createProject(projectDto, uriComponentsBuilder);

        ArgumentCaptor<Project> projectCaptor = ArgumentCaptor.forClass(Project.class);
        BDDMockito.then(projectRepository).should().save(projectCaptor.capture());
        Assertions.assertThat(projectCaptor.getValue().getName()).isEqualTo(correctProjectName);
    }

    @Test
    void shouldReturnHttpStatusBadRequestWhenProjectNameIncorrect_BlockTest() {
        Project saved = new Project();
        saved.setId(13L);
        String incorrectProjectName = "inco";
        BDDMockito.given(projectRepository.save(Mockito.any())).willReturn(saved);
        UriComponentsBuilder uriComponentsBuilder = fromUriString("/");
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName(incorrectProjectName);

        ResponseEntity<Void> actual = projectController.createNewProject(projectDto, uriComponentsBuilder);

        Assertions.assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldNotCreateProjectWhenProjectNameIncorrect_BlockTest() {
        String incorrectProjectName = "inco";
        UriComponentsBuilder uriComponentsBuilder = fromUriString("/");
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName(incorrectProjectName);

        projectController.createNewProject(projectDto, uriComponentsBuilder);

        BDDMockito.then(projectRepository).should(Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldReturnHttpStatusCreatedWhenProjectNameCorrect_BlockTest() {
        Project saved = new Project();
        saved.setId(13L);
        String correctProjectName = "legacy rearch";
        BDDMockito.given(projectRepository.save(Mockito.any())).willReturn(saved);
        UriComponentsBuilder uriComponentsBuilder = fromUriString("/");
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName(correctProjectName);

        ResponseEntity<Void> actual = projectController.createNewProject(projectDto, uriComponentsBuilder);

        Assertions.assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void shouldCreateProjectWhenProjectNameCorrect_BlockTest() {
        Project saved = new Project();
        saved.setId(13L);
        String correctProjectName = "legacy rearch";
        BDDMockito.given(projectRepository.save(Mockito.any())).willReturn(saved);
        UriComponentsBuilder uriComponentsBuilder = fromUriString("/");
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName(correctProjectName);

        projectController.createNewProject(projectDto, uriComponentsBuilder);

        ArgumentCaptor<Project> projectCaptor = ArgumentCaptor.forClass(Project.class);
        BDDMockito.then(projectRepository).should().save(projectCaptor.capture());
        Assertions.assertThat(projectCaptor.getValue().getName()).isEqualTo(correctProjectName);
    }

    @Test
    void shouldNotUpdateNameWhenIncorrect() {
        Project created = new Project();
        String correctProjectName = "legacy rearch";
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName(correctProjectName);

        boolean actual = projectController.updateIfNameIsValid(projectDto, created);

        Assertions.assertThat(actual).isTrue();
        Assertions.assertThat(created.getName()).isEqualTo(correctProjectName);
    }

    @Test
    void shouldNotUpdateNameWhenCorrect() {
        Project created = new Project();
        String correctProjectName = "icr";
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName(correctProjectName);

        boolean actual = projectController.updateIfNameIsValid(projectDto, created);

        Assertions.assertThat(actual).isFalse();
        Assertions.assertThat(created.getName()).isNull();
    }
}