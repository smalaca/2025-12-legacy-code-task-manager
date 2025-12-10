package com.smalaca.acl.usermanagement;

import com.smalaca.taskamanager.model.entities.ProductOwner;
import com.smalaca.taskamanager.model.entities.Project;
import com.smalaca.taskamanager.repository.ProductOwnerRepository;
import com.smalaca.taskmanager.usermanagement.domain.productowner.ProductOwnerDomainModel;
import com.smalaca.taskmanager.usermanagement.domain.productowner.ProductOwnerDomainRepository;

import java.util.Optional;
import java.util.stream.Collectors;

public class AclProductOwnerDomainRepository implements ProductOwnerDomainRepository {
    private final ProductOwnerRepository productOwnerRepository;

    public AclProductOwnerDomainRepository(ProductOwnerRepository productOwnerRepository) {
        this.productOwnerRepository = productOwnerRepository;
    }

    @Override
    public Optional<ProductOwnerDomainModel> findById(Long id) {
        return productOwnerRepository.findById(id).map(this::asProductOwnerDomainModel);
    }

    private ProductOwnerDomainModel asProductOwnerDomainModel(ProductOwner legacy) {
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

        return new ProductOwnerDomainModel(
                legacy.getId(), legacy.getFirstName(), legacy.getLastName(), legacy.getProjects().stream().map(Project::getId).collect(Collectors.toList()),
                phoneNumber, phonePrefix, emailAddress);
    }
}
