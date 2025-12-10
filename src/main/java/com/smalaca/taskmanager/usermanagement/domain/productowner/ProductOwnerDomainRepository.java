package com.smalaca.taskmanager.usermanagement.domain.productowner;

import java.util.Optional;

public interface ProductOwnerDomainRepository {
    Optional<ProductOwnerDomainModel> findById(Long id);
}
