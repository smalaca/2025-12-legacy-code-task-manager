package com.smalaca.taskmanager.usermanagement.ports.primiary.api;

import com.smalaca.taskamanager.repository.ProductOwnerRepository;
import com.smalaca.taskmanager.usermanagement.domain.productowner.ProductOwnerDomainService;
import com.smalaca.taskmanager.usermanagement.domain.productowner.ProductOwnerView;

public class UserManagementClient {
    private final ProductOwnerDomainService productOwnerDomainService;

    public UserManagementClient(ProductOwnerRepository productOwnerRepository) {
        productOwnerDomainService = new ProductOwnerDomainService(productOwnerRepository);
    }

    public ProductOwnerView findProductOwnerById(Long id) {
        return productOwnerDomainService.findById(id);
    }
}
