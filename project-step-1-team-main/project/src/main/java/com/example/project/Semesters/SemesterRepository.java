package com.example.project.Semesters;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterRepository extends JpaRepository<Semester,Long> {
    Optional<Semester> findByTermAndYear(String term, int year);
}
