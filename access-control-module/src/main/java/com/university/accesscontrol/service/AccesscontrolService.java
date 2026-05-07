package com.university.accesscontrol.service;
import com.university.accesscontrol.repository.AccesscontrolRepository;
import com.university.accesscontrol.entity.AccesscontrolEntity;
import com.university.accesscontrol.dto.*;
import org.springframework.stereotype.Service;
@Service
public class AccesscontrolService {
    private final AccesscontrolRepository repository;
    private final com.university.common.repository.UserRepository userRepository;
    public AccesscontrolService(AccesscontrolRepository repository, com.university.common.repository.UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }
        // Define a new access policy
        public com.university.accesscontrol.dto.AccessPolicyDto defineAccessPolicy(com.university.accesscontrol.dto.PolicyRecord record) {
            // For simplicity, store role and allowedResources as a JSON string in AccesscontrolEntity
            com.university.accesscontrol.entity.AccesscontrolEntity entity = new com.university.accesscontrol.entity.AccesscontrolEntity();
            entity.setName(record.role()); // role name
            // Convert allowedResources list to a comma‑separated string (could be a JSON column in a real DB)
            entity.setPolicyJson(String.join(",", record.allowedResources()));
            repository.save(entity);
            return new com.university.accesscontrol.dto.AccessPolicyDto(record.role(), record.allowedResources());
        }

        // Assign permissions (roles) to a user
        public void assignPermissions(com.university.accesscontrol.dto.AccessRequestDto request) {
            var userOpt = userRepository.findById(Long.valueOf(request.userId()));
            if (userOpt.isEmpty()) {
                throw new RuntimeException("User not found: " + request.userId());
            }
            var user = userOpt.get();
            // Simple role assignment – override existing role
            user.setRole(com.university.common.enums.Role.valueOf(request.role().toUpperCase()));
            userRepository.save(user);
        }

        // Validate that a user has permission to access a resource for an action
        public boolean validateAccess(com.university.accesscontrol.dto.AccessRequestDto request) {
            var userOpt = userRepository.findById(Long.valueOf(request.userId()));
            if (userOpt.isEmpty()) return false;
            var user = userOpt.get();
            // Find the policy for the user's role
            var policies = repository.findAll();
            for (var policy : policies) {
                if (policy.getName().equalsIgnoreCase(user.getRole().name())) {
                    // policy.policyJson holds a comma‑separated list of allowed resources
                    var allowed = java.util.Arrays.stream(policy.getPolicyJson().split(","))
                            .map(String::trim)
                            .collect(java.util.stream.Collectors.toSet());
                    return allowed.contains(request.resourceId());
                }
            }
            return false;
        }

        // Revoke a role from a user (set to STUDENT by default)
        public void revokeAccess(com.university.accesscontrol.dto.AccessRequestDto request) {
            var userOpt = userRepository.findById(Long.valueOf(request.userId()));
            if (userOpt.isPresent()) {
                var user = userOpt.get();
                user.setRole(com.university.common.enums.Role.STUDENT);
                userRepository.save(user);
            }
        }
}
