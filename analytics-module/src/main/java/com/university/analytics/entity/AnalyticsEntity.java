package com.university.analytics.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "search_analytics")
@Data
public class AnalyticsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String queryText;

    private Integer count = 0;

    public void incrementCount() {
        this.count++;
    }
}
