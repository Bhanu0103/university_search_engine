package com.university.queryoptimization.repository;

import com.university.queryoptimization.entity.QueryoptimizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueryoptimizationRepository extends JpaRepository<QueryoptimizationEntity, Long> {
}
