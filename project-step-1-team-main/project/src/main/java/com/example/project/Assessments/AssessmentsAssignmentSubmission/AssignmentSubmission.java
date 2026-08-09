package com.example.project.Assessments.AssessmentsAssignmentSubmission;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.project.Assessments.AssessmentsAssignmentDetails.AssignmentDetails;
import com.example.project.Files.FileMetadata;
import com.example.project.Students.Student;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assignment_submissions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"assignment_id", "student_id"}) //  One submission per student per assignment
})
public class AssignmentSubmission {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne 
    @JoinColumn(name = "assignment_id", nullable = false)
    private AssignmentDetails assignment;

    @ManyToOne 
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

   @OneToMany(mappedBy = "assignmentSubmission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileMetadata> fileMetadataList = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    @Column( columnDefinition = "TEXT")
    private String feedback = "";
 



public AssignmentSubmission(AssignmentDetails assignment, Student student) {
    this.assignment = assignment;
    this.student = student;
    this.feedback = "";
    this.submittedAt = LocalDateTime.now(); 
}

}
