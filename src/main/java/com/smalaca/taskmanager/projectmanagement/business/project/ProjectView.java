package com.smalaca.taskmanager.projectmanagement.business.project;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ProjectView {
    private Long id;
    private String name;
    private String projectStatus;
    private Long productOwnerId;
    private List<Long> teamIds = new ArrayList<>();
}
