package com.example.project.Courses;


import java.util.ArrayList;
import java.util.List;

import com.example.project.CourseInstructors.CourseInstructor;
import com.example.project.Enrollments.Enrollment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



    @Entity
    @Table(name = "courses", uniqueConstraints = {
    @UniqueConstraint(columnNames = "code") // Ensures uniqueness at DB level
    })
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Course {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    
        @Column(nullable = false)
        private String name;
        @Column(unique = true, nullable = false)
        private String code; 
        
        @Column(nullable = false)
        private String description;

        @Column(nullable = false)
        private int credits;
    
         @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
         private List<Enrollment> enrollments = new ArrayList<>();

         @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
         private List<CourseInstructor> courseInstructors = new ArrayList<>();

         public Course(String name, String code, String description, int credits) {
            this.name = name;
            this.code = code;
            this.description = description;
            this.credits = credits;
        }

    }