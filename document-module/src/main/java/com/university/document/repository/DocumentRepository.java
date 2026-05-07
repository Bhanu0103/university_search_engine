package com.university.document.repository;

import com.university.document.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
    List<DocumentEntity> findByUniversityNameContainingIgnoreCase(String universityName);
    List<DocumentEntity> findByContentContainingIgnoreCase(String keyword);
    List<DocumentEntity> findByUniversityNameContainingIgnoreCaseOrContentContainingIgnoreCase(String universityName, String content);
    List<DocumentEntity> findByDepartmentContainingIgnoreCase(String department);
    List<DocumentEntity> findByLocationContainingIgnoreCase(String location);
    List<DocumentEntity> findByUniversityNameStartingWithIgnoreCase(String prefix);
}
