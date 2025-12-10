package com.smalaca.taskmanager.usermanagement.domain.productowner;

import java.util.Optional;

public class ProductOwnerDomainService {
    private final ProductOwnerDomainRepository productOwnerDomainRepository;

    public ProductOwnerDomainService(ProductOwnerDomainRepository productOwnerDomainRepository) {
        this.productOwnerDomainRepository = productOwnerDomainRepository;
    }

    public ProductOwnerView findById(Long id) {
        ProductOwnerDomainModel productOwner = getProductOwnerById(id);

        return productOwner.asView();
    }

    private ProductOwnerDomainModel getProductOwnerById(Long id) {
        Optional<ProductOwnerDomainModel> productOwner = productOwnerDomainRepository.findById(id);

        if (productOwner.isEmpty()) {
            throw new ProductOwnerNotFoundException();
        }

        return productOwner.get();
    }
}
