package com.smalaca.taskamanager.api.rest;

import com.smalaca.acl.legacycode.AclUserManagementClient;
import com.smalaca.acl.usermanagement.AclProductOwnerDomainRepository;
import com.smalaca.taskamanager.dto.ProductOwnerDto;
import com.smalaca.taskamanager.exception.ProductOwnerNotFoundException;
import com.smalaca.taskamanager.model.embedded.EmailAddress;
import com.smalaca.taskamanager.model.embedded.PhoneNumber;
import com.smalaca.taskamanager.model.entities.ProductOwner;
import com.smalaca.taskamanager.model.entities.Project;
import com.smalaca.taskamanager.repository.ProductOwnerRepository;
import com.smalaca.taskamanager.repository.ProjectRepository;
import com.smalaca.taskmanager.usermanagement.ports.primiary.api.UserManagementClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/product-owner")
@SuppressWarnings("checkstyle:ClassFanOutComplexity")
public class ProductOwnerController {
    private final ProductOwnerRepository productOwnerRepository;
    private final ProjectRepository projectRepository;
    private final AclUserManagementClient aclUserManagementClient;

    public ProductOwnerController(ProductOwnerRepository productOwnerRepository, ProjectRepository projectRepository) {
        this.productOwnerRepository = productOwnerRepository;
        this.projectRepository = projectRepository;
        aclUserManagementClient = new AclUserManagementClient(
                new UserManagementClient(new AclProductOwnerDomainRepository(productOwnerRepository)));
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<ProductOwnerDto> findById(@PathVariable Long id) {
        try {
            ProductOwnerDto dto = aclUserManagementClient.findProductOwnerById(id);

            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (ProductOwnerNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody ProductOwnerDto dto, UriComponentsBuilder uriComponentsBuilder) {
        if (productOwnerRepository.findByFirstNameAndLastName(dto.getFirstName(), dto.getLastName()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            ProductOwner productOwner = new ProductOwner();
            productOwner.setFirstName(dto.getFirstName());
            productOwner.setLastName(dto.getLastName());
            ProductOwner saved = productOwnerRepository.save(productOwner);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uriComponentsBuilder.path("/product-owner/{id}").buildAndExpand(saved.getId()).toUri());
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductOwnerDto> update(@PathVariable Long id, @RequestBody ProductOwnerDto dto) {
        ProductOwner productOwner;

        try {
            productOwner = getProductOwnerById(id);
        } catch (ProductOwnerNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (dto.getPhoneNumber() != null) {
            PhoneNumber phoneNumber = new PhoneNumber();
            phoneNumber.setPrefix(dto.getPhonePrefix());
            phoneNumber.setNumber(dto.getPhoneNumber());
            productOwner.setPhoneNumber(phoneNumber);
        }

        if (dto.getEmailAddress() != null) {
            EmailAddress emailAddress = new EmailAddress();
            emailAddress.setEmailAddress(dto.getEmailAddress());
            productOwner.setEmailAddress(emailAddress);
        }

        ProductOwner updated = productOwnerRepository.save(productOwner);

        ProductOwnerDto updatedDto = new ProductOwnerDto();
        updatedDto.setId(updated.getId());
        updatedDto.setFirstName(updated.getFirstName());
        updatedDto.setLastName(updated.getLastName());

        if (updated.getPhoneNumber() != null) {
            updatedDto.setPhonePrefix(updated.getPhoneNumber().getPrefix());
            updatedDto.setPhoneNumber(updated.getPhoneNumber().getNumber());
        }

        if (updated.getEmailAddress() != null) {
            updatedDto.setEmailAddress(updated.getEmailAddress().getEmailAddress());
        }

        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @PutMapping("/{id}/projects/{projectId}")
    @Transactional
    public ResponseEntity<Void> addProject(@PathVariable Long id, @PathVariable Long projectId) {
        try {
            ProductOwner productOwner = getProductOwnerById(id);
            Optional<Project> found = projectRepository.findById(projectId);

            if (found.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Project project = found.get();
            project.setProductOwner(productOwner);
            productOwner.addProject(project);

            projectRepository.save(project);
            productOwnerRepository.save(productOwner);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (ProductOwnerNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/projects/{projectId}")
    @Transactional
    public ResponseEntity<Void> removeProject(@PathVariable Long id, @PathVariable Long projectId) {
        try {
            ProductOwner productOwner = getProductOwnerById(id);
            Optional<Project> found = projectRepository.findById(projectId);

            if (found.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Project project = found.get();
            project.setProductOwner(null);
            productOwner.removeProject(project);

            projectRepository.save(project);
            productOwnerRepository.save(productOwner);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (ProductOwnerNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ProductOwner productOwner;

        try {
            productOwner = getProductOwnerById(id);
        } catch (ProductOwnerNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        productOwnerRepository.delete(productOwner);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ProductOwner getProductOwnerById(Long id) {
        Optional<ProductOwner> productOwner = productOwnerRepository.findById(id);

        if (productOwner.isEmpty()) {
            throw new ProductOwnerNotFoundException();
        }

        return productOwner.get();
    }
}
