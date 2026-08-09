package com.example.project.Semesters;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "semesters", uniqueConstraints = @UniqueConstraint(columnNames = {"term","year"}))
@Data @NoArgsConstructor @AllArgsConstructor
public class Semester {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String term;       // e.g. "Spring", "Fall"

    @Column(nullable = false)
    private int year;          // e.g. 2025

    public Semester(String term, int year) {
        this.term = term;
        this.year = year;
    }
}
