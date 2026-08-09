package com.example.project.Assessments.AssessmentsAssignmentDetails;

import java.util.ArrayList;
import java.util.List;

import com.example.project.Assessments.AssessmentsAssignmentSubmission.AssignmentSubmission;
import com.example.project.Assessments.Assessments_Assessment.Assessment;
import com.example.project.Files.FileMetadata;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assignment_details")
public class AssignmentDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "assessment_id", nullable = false) 
    private Assessment assessment;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
private List<AssignmentSubmission> assignmentSubmissions = new ArrayList<>();


    @OneToMany(mappedBy = "assignmentDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileMetadata> fileMetadataList = new ArrayList<>();
     

    @Column(nullable = false)
    private Double totalScore;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private boolean published = false; 

    public AssignmentDetails(Assessment assessment, Double totalScore, String notes, boolean published) {
        this.assessment = assessment;
        this.totalScore = totalScore;
        this.notes = notes;
        this.published = published;
    }


   

}

