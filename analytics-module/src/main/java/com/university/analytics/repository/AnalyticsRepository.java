package com.university.analytics.repository;

import com.university.analytics.entity.AnalyticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface AnalyticsRepository extends JpaRepository<AnalyticsEntity, Long> {
    Optional<AnalyticsEntity> findByQueryText(String queryText);

    @Query("SELECT a.queryText FROM AnalyticsEntity a ORDER BY a.count DESC")
    List<String> findTopQueries();
}
