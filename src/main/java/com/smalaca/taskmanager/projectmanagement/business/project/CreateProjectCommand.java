package com.smalaca.taskmanager.projectmanagement.business.project;

import lombok.Getter;

@Getter
public class CreateProjectCommand {
    private final String name;

    public CreateProjectCommand(String name) {
        this.name = name;
    }
}
