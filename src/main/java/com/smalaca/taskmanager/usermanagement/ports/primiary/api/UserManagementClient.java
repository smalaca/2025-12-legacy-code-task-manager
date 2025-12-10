package com.smalaca.taskmanager.usermanagement.ports.primiary.api;

import com.smalaca.taskamanager.repository.ProductOwnerRepository;
import com.smalaca.taskmanager.usermanagement.domain.productowner.ProductOwnerDomainRepository;
import com.smalaca.taskmanager.usermanagement.domain.productowner.ProductOwnerDomainService;
import com.smalaca.taskmanager.usermanagement.domain.productowner.ProductOwnerView;

public class UserManagementClient {
    private final ProductOwnerDomainService productOwnerDomainService;

    public UserManagementClient(ProductOwnerRepository productOwnerRepository, ProductOwnerDomainRepository productOwnerDomainRepository) {
        productOwnerDomainService = new ProductOwnerDomainService(productOwnerRepository, productOwnerDomainRepository);
    }

    public ProductOwnerView findProductOwnerById(Long id) {
        return productOwnerDomainService.findById(id);
    }
}
