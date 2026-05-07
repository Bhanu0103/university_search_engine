package com.university.personalization.service;

import com.university.common.entity.UserEntity;
import com.university.common.enums.Role;
import com.university.common.repository.UserRepository;
import com.university.document.entity.DocumentEntity;
import com.university.document.repository.DocumentRepository;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PersonalizationServiceTest {
    private final DocumentRepository documentRepository = mock(DocumentRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final PersonalizationService service = new PersonalizationService(documentRepository, userRepository);

    @Test
    void studentPersonalizationReturnsTopRankedDocuments() {
        DocumentEntity top = document("AI", 5.0);
        DocumentEntity low = document("Medicine", 15.0);
        when(documentRepository.findAll()).thenReturn(List.of(top, low));

        assertThat(service.getPersonalizedResults("STUDENT")).containsExactly(top);
    }

    @Test
    void recommendContentUsesUsersRole() {
        UserEntity user = user(Role.RESEARCHER);
        DocumentEntity research = document("Research", 20.0);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(documentRepository.findByUniversityNameContainingIgnoreCaseOrContentContainingIgnoreCase("Research", "Research"))
                .thenReturn(List.of(research));

        assertThat(service.recommendContent("1")).containsExactly(research);
    }

    @Test
    void trackUserSearchValidatesClickedDocument() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user(Role.STUDENT)));
        when(documentRepository.findById(9L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.trackUserSearch("1", "{\"clickedDocumentId\":\"9\"}"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Document not found");
    }

    private UserEntity user(Role role) {
        UserEntity user = new UserEntity();
        user.setRole(role);
        return user;
    }

    private DocumentEntity document(String title, Double ranking) {
        DocumentEntity document = new DocumentEntity();
        document.setTitle(title);
        document.setUniversityName("University");
        document.setContent(title + " content");
        document.setRanking(ranking);
        return document;
    }
}
