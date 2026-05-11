package com.university.accesscontrol.service;

import com.university.accesscontrol.dto.AccessRequestDto;
import com.university.accesscontrol.dto.PolicyRecord;
import com.university.accesscontrol.entity.AccesscontrolEntity;
import com.university.accesscontrol.repository.AccesscontrolRepository;
import com.university.common.entity.UserEntity;
import com.university.common.enums.Role;
import com.university.common.exception.ResourceNotFoundException;
import com.university.common.repository.UserRepository;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccesscontrolServiceTest {
    private final AccesscontrolRepository repository = mock(AccesscontrolRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final AccesscontrolService service = new AccesscontrolService(repository, userRepository);

    @Test
    void defineAccessPolicySavesRoleAndResources() {
        var dto = service.defineAccessPolicy(new PolicyRecord("FACULTY", List.of("doc-1", "doc-2")));

        assertThat(dto.role()).isEqualTo("FACULTY");
        assertThat(dto.allowedResources()).containsExactly("doc-1", "doc-2");
        verify(repository).save(any(AccesscontrolEntity.class));
    }

    @Test
    void validateAccessReturnsTrueWhenRolePolicyContainsResource() {
        UserEntity user = new UserEntity();
        user.setRole(Role.FACULTY);
        AccesscontrolEntity policy = new AccesscontrolEntity();
        policy.setName("FACULTY");
        policy.setPolicyJson("doc-1,doc-2");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.findAll()).thenReturn(List.of(policy));

        assertThat(service.validateAccess(new AccessRequestDto("1", null, "doc-2"))).isTrue();
    }

    @Test
    void assignPermissionsThrowsWhenUserMissing() {
        when(userRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.assignPermissions(new AccessRequestDto("42", "ADMIN", null)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }
}
