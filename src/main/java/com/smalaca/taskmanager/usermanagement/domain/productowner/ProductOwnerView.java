package com.smalaca.taskmanager.usermanagement.domain.productowner;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ProductOwnerView {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String phonePrefix;
    private String emailAddress;
    private List<Long> projectIds = new ArrayList<>();
}
