package com.university.ingest.repository;

import com.university.ingest.entity.IngestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngestRepository extends JpaRepository<IngestEntity, Long> {
}
