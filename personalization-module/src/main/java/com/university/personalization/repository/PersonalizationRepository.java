package com.university.personalization.repository;

import com.university.personalization.entity.PersonalizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalizationRepository extends JpaRepository<PersonalizationEntity, Long> {
}
