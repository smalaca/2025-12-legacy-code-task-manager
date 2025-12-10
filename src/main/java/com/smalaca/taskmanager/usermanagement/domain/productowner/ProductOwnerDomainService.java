package com.smalaca.taskmanager.usermanagement.domain.productowner;

import com.smalaca.taskamanager.model.entities.ProductOwner;
import com.smalaca.taskamanager.model.entities.Project;
import com.smalaca.taskamanager.repository.ProductOwnerRepository;

import java.util.Optional;
import java.util.stream.Collectors;

public class ProductOwnerDomainService {
    private final ProductOwnerRepository productOwnerRepository;

    public ProductOwnerDomainService(ProductOwnerRepository productOwnerRepository) {
        this.productOwnerRepository = productOwnerRepository;
    }

    public ProductOwnerView findById(Long id) {
        ProductOwner legacy = getProductOwnerById(id);
        String phonePrefix = null;
        String phoneNumber = null;
        String emailAddress = null;
        if (legacy.getPhoneNumber() != null) {
            phonePrefix = legacy.getPhoneNumber().getPrefix();
            phoneNumber = legacy.getPhoneNumber().getNumber();
        }

        if (legacy.getEmailAddress() != null) {
            emailAddress = legacy.getEmailAddress().getEmailAddress();
        }


        ProductOwnerDomainModel productOwner = new ProductOwnerDomainModel(
                legacy.getId(), legacy.getFirstName(), legacy.getLastName(), legacy.getProjects().stream().map(Project::getId).collect(Collectors.toList()),
                phoneNumber, phonePrefix, emailAddress);

        return productOwner.asView();
    }

    private ProductOwner getProductOwnerById(Long id) {
        Optional<ProductOwner> productOwner = productOwnerRepository.findById(id);

        if (productOwner.isEmpty()) {
            throw new ProductOwnerNotFoundException();
        }

        return productOwner.get();
    }
}
