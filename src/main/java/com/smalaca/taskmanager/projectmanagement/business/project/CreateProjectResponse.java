package com.smalaca.taskmanager.projectmanagement.business.project;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateProjectResponse {
    private boolean exists;
    private boolean validName;
    private Long projectId;
}
