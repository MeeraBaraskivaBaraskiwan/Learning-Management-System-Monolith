package com.example.project.Profiles.InstructorProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InstructorProfileRepository extends JpaRepository<InstructorProfile, Long> {
    Optional<InstructorProfile> findByInstructorId(Long instructorId);
    Optional<InstructorProfile> findByProfile_Id(Long profileId);
}