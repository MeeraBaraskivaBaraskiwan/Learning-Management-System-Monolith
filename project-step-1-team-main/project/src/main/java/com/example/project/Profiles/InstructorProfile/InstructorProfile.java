package com.example.project.Profiles.InstructorProfile;

import com.example.project.Instructors.Instructor;
import com.example.project.Profiles.Profile.Profile;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Table(name = "instructor_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "profile_id", nullable = false, unique = true)
    private Profile profile;

    @OneToOne
    @JoinColumn(name = "instructor_id", nullable = false, unique = true)
    private Instructor instructor;

    private String faculty;

    private String department;

    private String academicRank; 
}


