package com.university.document.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Lob;

@Entity
@Table(name = "university_documents")
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String universityName;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String location;

    private Double ranking;

    private String department;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getUniversityName() { return universityName; }
    public void setUniversityName(String universityName) { this.universityName = universityName; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Double getRanking() { return ranking; }
    public void setRanking(Double ranking) { this.ranking = ranking; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}
