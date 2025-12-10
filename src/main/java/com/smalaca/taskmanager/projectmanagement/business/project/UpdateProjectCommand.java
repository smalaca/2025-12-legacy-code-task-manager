package com.smalaca.taskmanager.projectmanagement.business.project;

import lombok.Getter;

@Getter
public class UpdateProjectCommand {
    private final Long id;
    private final String projectStatus;

    public UpdateProjectCommand(Long id, String projectStatus) {
        this.id = id;
        this.projectStatus = projectStatus;
    }
}
