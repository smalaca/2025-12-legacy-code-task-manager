package com.smalaca.taskmanager.usermanagement.domain.productowner;

import com.smalaca.taskamanager.exception.ProductOwnerNotFoundException;
import com.smalaca.taskamanager.model.entities.ProductOwner;
import com.smalaca.taskamanager.model.entities.Project;
import com.smalaca.taskamanager.repository.ProductOwnerRepository;

import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class ProductOwnerDomainService {
    private final ProductOwnerRepository productOwnerRepository;

    public ProductOwnerDomainService(ProductOwnerRepository productOwnerRepository) {
        this.productOwnerRepository = productOwnerRepository;
    }

    public ProductOwnerView findById(Long id) {
        ProductOwner productOwner = getProductOwnerById(id);
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

    private ProductOwner getProductOwnerById(Long id) {
        Optional<ProductOwner> productOwner = productOwnerRepository.findById(id);

        if (productOwner.isEmpty()) {
            throw new ProductOwnerNotFoundException();
        }

        return productOwner.get();
    }
}
