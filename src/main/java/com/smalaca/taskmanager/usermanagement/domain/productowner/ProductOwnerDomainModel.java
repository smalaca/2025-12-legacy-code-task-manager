package com.smalaca.taskmanager.usermanagement.domain.productowner;

import com.smalaca.taskamanager.model.entities.ProductOwner;
import com.smalaca.taskamanager.model.entities.Project;

import static java.util.stream.Collectors.toList;

class ProductOwnerDomainModel {
    private final ProductOwner productOwner;

    ProductOwnerDomainModel(ProductOwner productOwner) {
        this.productOwner = productOwner;
    }

    public ProductOwnerView asView() {
        ProductOwnerView dto = new ProductOwnerView();
        dto.setId(productOwner.getId());
        dto.setFirstName(productOwner.getFirstName());
        dto.setLastName(productOwner.getLastName());

        if (productOwner.getPhoneNumber() != null) {
            dto.setPhonePrefix(productOwner.getPhoneNumber().getPrefix());
            dto.setPhoneNumber(productOwner.getPhoneNumber().getNumber());
        }

        if (productOwner.getEmailAddress() != null) {
            dto.setEmailAddress(productOwner.getEmailAddress().getEmailAddress());
        }

        dto.setProjectIds(productOwner.getProjects().stream().map(Project::getId).collect(toList()));
        return dto;

    }
}
