package com.university.prediction.repository;

import com.university.prediction.entity.PredictionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PredictionRepository extends JpaRepository<PredictionEntity, Long> {
}
