package com.example.project.Profiles.StudentProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    Optional<StudentProfile> findByStudentId(Long studentId);
    Optional<StudentProfile> findByProfile_Id(Long profileId);
}