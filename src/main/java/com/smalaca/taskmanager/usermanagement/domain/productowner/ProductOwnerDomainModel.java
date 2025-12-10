package com.smalaca.taskmanager.usermanagement.domain.productowner;

import com.smalaca.taskamanager.model.entities.ProductOwner;
import com.smalaca.taskamanager.model.entities.Project;

import java.util.List;
import java.util.stream.Collectors;

class ProductOwnerDomainModel {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private String phonePrefix;
    private String phoneNumber;
    private String emailAddress;
    private final List<Long> projectIds;
    private final boolean hasPhoneNumber;
    private final boolean hasEmailAddress;

    ProductOwnerDomainModel(ProductOwner productOwner) {
        id = productOwner.getId();
        firstName = productOwner.getFirstName();
        lastName = productOwner.getLastName();
        hasPhoneNumber = productOwner.getPhoneNumber() != null;
        if (hasPhoneNumber) {
            phonePrefix = productOwner.getPhoneNumber().getPrefix();
            phoneNumber = productOwner.getPhoneNumber().getNumber();
        }
        hasEmailAddress = productOwner.getEmailAddress() != null;

        if (hasEmailAddress) {
            emailAddress = productOwner.getEmailAddress().getEmailAddress();
        }
        projectIds = productOwner.getProjects().stream().map(Project::getId).collect(Collectors.toList());
    }

    public ProductOwnerView asView() {
        ProductOwnerView view = new ProductOwnerView();
        view.setId(id);
        view.setFirstName(firstName);
        view.setLastName(lastName);

        if (hasPhoneNumber) {
            view.setPhonePrefix(phonePrefix);
            view.setPhoneNumber(phoneNumber);
        }

        if (hasEmailAddress) {
            view.setEmailAddress(emailAddress);
        }

        view.setProjectIds(projectIds);

        return view;

    }
}
