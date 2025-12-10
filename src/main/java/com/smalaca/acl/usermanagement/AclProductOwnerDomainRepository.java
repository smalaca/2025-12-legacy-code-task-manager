package com.smalaca.acl.usermanagement;

import com.smalaca.taskamanager.repository.ProductOwnerRepository;
import com.smalaca.taskmanager.usermanagement.domain.productowner.ProductOwnerDomainRepository;

public class AclProductOwnerDomainRepository implements ProductOwnerDomainRepository {
    private final ProductOwnerRepository productOwnerRepository;

    public AclProductOwnerDomainRepository(ProductOwnerRepository productOwnerRepository) {
        this.productOwnerRepository = productOwnerRepository;
    }
}
