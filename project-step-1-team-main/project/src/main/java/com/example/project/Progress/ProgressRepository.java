package com.example.project.Progress;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    List<Progress> findByEnrollmentId(Long enrollmentId);
}
