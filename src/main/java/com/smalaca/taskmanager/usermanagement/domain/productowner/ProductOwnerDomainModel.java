package com.smalaca.taskmanager.usermanagement.domain.productowner;

import java.util.List;

public class ProductOwnerDomainModel {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String phonePrefix;
    private final String phoneNumber;
    private final String emailAddress;
    private final List<Long> projectIds;

    public ProductOwnerDomainModel(
            Long id, String firstName, String lastName, List<Long> projectIds, String phoneNumber, String phonePrefix, String emailAddress) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.projectIds = projectIds;
        this.phoneNumber = phoneNumber;
        this.phonePrefix = phonePrefix;
        this.emailAddress = emailAddress;
    }

    ProductOwnerView asView() {
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
