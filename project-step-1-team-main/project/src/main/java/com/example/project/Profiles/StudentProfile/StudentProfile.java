package com.example.project.Profiles.StudentProfile;

import com.example.project.Profiles.InstructorProfile.InstructorProfile;
import com.example.project.Profiles.Profile.Profile;
import com.example.project.Students.Student;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Table(name = "student_profiles")
@Data

@NoArgsConstructor
@AllArgsConstructor
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "profile_id", nullable = false, unique = true)
    private Profile profile;


    @OneToOne
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    private Student student;


    private String admittedYear;

    private String major;

    private String minor;

    private String currentSemester;

    private String degreeProgram; 

    private String studentLevel;

    private String tawjihiStream;

    
    private String probationStatus;

    private String department;


    @ManyToOne
    @JoinColumn(name = "advisor_id", nullable = false)
     private InstructorProfile advisor;

}
