package com.university.abusedetection.repository;

import com.university.abusedetection.entity.AbusedetectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbusedetectionRepository extends JpaRepository<AbusedetectionEntity, Long> {
}
