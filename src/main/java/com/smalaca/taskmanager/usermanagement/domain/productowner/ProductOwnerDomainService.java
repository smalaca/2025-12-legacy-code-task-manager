package com.smalaca.taskmanager.usermanagement.domain.productowner;

import com.smalaca.taskamanager.model.entities.ProductOwner;
import com.smalaca.taskamanager.repository.ProductOwnerRepository;

import java.util.Optional;

public class ProductOwnerDomainService {
    private final ProductOwnerRepository productOwnerRepository;

    public ProductOwnerDomainService(ProductOwnerRepository productOwnerRepository) {
        this.productOwnerRepository = productOwnerRepository;
    }

    public ProductOwnerView findById(Long id) {
        ProductOwner legacy = getProductOwnerById(id);
        ProductOwnerDomainModel productOwner = new ProductOwnerDomainModel(legacy);

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
