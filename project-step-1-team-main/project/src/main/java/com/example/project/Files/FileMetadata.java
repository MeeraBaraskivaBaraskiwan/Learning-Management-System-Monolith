package com.example.project.Files;

import com.example.project.Assessments.AssessmentsAssignmentDetails.AssignmentDetails;
import com.example.project.Assessments.AssessmentsAssignmentSubmission.AssignmentSubmission;
import com.example.project.CourseContents.CourseContent;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor
@Entity
@Table(name = "file_metadata") 
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalFilename;  
    
    @Column(nullable = false)
    private String storedFilename;    
    
    @Column(nullable = false)
    private String fileExtension;
    
    @Column(nullable = false)
    private Long fileSize;

    @ManyToOne(optional = true)
    @JoinColumn(name = "assignment_details_id")
    private AssignmentDetails assignmentDetails;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "submission_id")
    private AssignmentSubmission assignmentSubmission;

    @ManyToOne(optional = true)
    @JoinColumn(name = "course_content_id")
    private CourseContent courseContent;
}
