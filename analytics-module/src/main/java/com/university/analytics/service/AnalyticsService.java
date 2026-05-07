package com.university.analytics.service;

import com.university.analytics.entity.AnalyticsEntity;
import com.university.analytics.repository.AnalyticsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {
    private final AnalyticsRepository repository;

    public AnalyticsService(AnalyticsRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void recordQuery(String query) {
        if (query == null || query.isBlank()) return;
        
        AnalyticsEntity entity = repository.findByQueryText(query.toLowerCase())
                .orElse(new AnalyticsEntity());
        
        if (entity.getQueryText() == null) {
            entity.setQueryText(query.toLowerCase());
            entity.setCount(1);
        } else {
            entity.incrementCount();
        }
        
        repository.save(entity);
    }

    public List<String> getTopTrending(int limit) {
        return repository.findTopQueries().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
}
