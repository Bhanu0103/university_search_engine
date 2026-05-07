package com.university.filter.service;

import com.university.document.entity.DocumentEntity;
import com.university.document.repository.DocumentRepository;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilterServiceTest {
    private final DocumentRepository documentRepository = mock(DocumentRepository.class);
    private final FilterService service = new FilterService(documentRepository);

    @Test
    void applyFiltersMatchesQueryDepartmentAndLocation() {
        DocumentEntity match = document("Engineering", "IIT", "AI content", "Chennai", "CSE");
        DocumentEntity miss = document("Medicine", "Health", "Biology", "Delhi", "Medical");
        when(documentRepository.findAll()).thenReturn(List.of(match, miss));

        assertThat(service.applyFilters("AI", "CSE", "Chennai")).containsExactly(match);
    }

    @Test
    void getFacetCountsCountsRequestedFieldsForMatchingDocuments() {
        when(documentRepository.findAll()).thenReturn(List.of(
                document("Engineering", "IIT Madras", "AI", "Chennai", "CSE"),
                document("Engineering Lab", "Anna University", "Robotics", "Chennai", "CSE")));

        assertThat(service.getFacetCounts("engineering", List.of("department", "location", "university")))
                .containsEntry("department:CSE", 2)
                .containsEntry("location:Chennai", 2)
                .containsEntry("university:IIT Madras", 1);
    }

    @Test
    void combineFiltersReturnsEmptyWhenNoFiltersProvided() {
        assertThat(service.combineFilters(List.of())).isEmpty();
    }

    private DocumentEntity document(String title, String university, String content, String location, String department) {
        DocumentEntity document = new DocumentEntity();
        document.setTitle(title);
        document.setUniversityName(university);
        document.setContent(content);
        document.setLocation(location);
        document.setDepartment(department);
        return document;
    }
}
