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

    ProductOwnerDomainModel(ProductOwner productOwner) {
        id = productOwner.getId();
        firstName = productOwner.getFirstName();
        lastName = productOwner.getLastName();

        if (productOwner.getPhoneNumber() != null) {
            phonePrefix = productOwner.getPhoneNumber().getPrefix();
            phoneNumber = productOwner.getPhoneNumber().getNumber();
        }

        if (productOwner.getEmailAddress() != null) {
            emailAddress = productOwner.getEmailAddress().getEmailAddress();
        }

        projectIds = productOwner.getProjects().stream().map(Project::getId).collect(Collectors.toList());
    }

    public ProductOwnerView asView() {
        ProductOwnerView view = new ProductOwnerView();
        view.setId(id);
        view.setFirstName(firstName);
        view.setLastName(lastName);

        if (hasPhoneNumber()) {
            view.setPhonePrefix(phonePrefix);
            view.setPhoneNumber(phoneNumber);
        }

        if (hasEmailAddress()) {
            view.setEmailAddress(emailAddress);
        }

        view.setProjectIds(projectIds);

        return view;

    }

    private boolean hasEmailAddress() {
        return emailAddress != null;
    }

    private boolean hasPhoneNumber() {
        return phoneNumber != null;
    }
}
