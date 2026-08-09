package com.example.project.Instructors;


public interface InstructorRepositoryCustom {
    long countStudentsForInstructor(Long instructorId);
    long countAssessmentsForInstructor(Long instructorId);
    double calcPassRate(Long instructorId);   // 0‑100
    double calcImpact(Long instructorId);     // any metric you like
}