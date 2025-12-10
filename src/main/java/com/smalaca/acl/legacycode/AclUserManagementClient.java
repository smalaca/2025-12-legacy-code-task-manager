package com.smalaca.acl.legacycode;

import com.smalaca.taskamanager.dto.ProductOwnerDto;
import com.smalaca.taskmanager.usermanagement.domain.productowner.ProductOwnerView;
import com.smalaca.taskmanager.usermanagement.ports.primiary.api.UserManagementClient;

public class AclUserManagementClient {
    private final UserManagementClient userManagementClient;

    public AclUserManagementClient(UserManagementClient userManagementClient) {
        this.userManagementClient = userManagementClient;
    }

    public ProductOwnerDto findProductOwnerById(Long id) {
        ProductOwnerView view = userManagementClient.findProductOwnerById(id);
        return asProductOwnerDto(view);
    }

    private ProductOwnerDto asProductOwnerDto(ProductOwnerView view) {
        ProductOwnerDto productOwnerDto = new ProductOwnerDto();
        productOwnerDto.setId(view.getId());
        productOwnerDto.setFirstName(view.getFirstName());
        productOwnerDto.setLastName(view.getLastName());
        productOwnerDto.setEmailAddress(view.getEmailAddress());
        productOwnerDto.setPhonePrefix(view.getPhonePrefix());
        productOwnerDto.setPhoneNumber(view.getPhoneNumber());
        productOwnerDto.setProjectIds(view.getProjectIds());
        return productOwnerDto;
    }
}
