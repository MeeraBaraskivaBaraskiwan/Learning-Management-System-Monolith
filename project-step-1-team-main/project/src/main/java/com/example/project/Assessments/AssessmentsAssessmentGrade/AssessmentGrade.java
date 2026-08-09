package com.example.project.Assessments.AssessmentsAssessmentGrade;

import com.example.project.Assessments.AssessmentsAssignmentSubmission.AssignmentSubmission;
import com.example.project.Assessments.AssessmentsQuizSubmission.QuizSubmission;
import com.example.project.Assessments.Assessments_Assessment.Assessment;
import com.example.project.Students.Student;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assessment_grades", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"assessment_id", "student_id"}) 
})
public class AssessmentGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment; 

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student; 
    @OneToOne
    @JoinColumn(name = "quiz_submission_id", nullable = true)
    private QuizSubmission quizSubmission; 
    @OneToOne
    @JoinColumn(name = "assignment_submission_id", nullable = true)
    private AssignmentSubmission assignmentSubmission; 

    private Double autoGradedScore; 

    private Double finalScore; 

    private String gradingComments;
    
    private boolean fullyGraded = false; 


    @Column(
    name = "created_at",
    nullable = false,
    updatable = false,
    columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP"
)
@CreationTimestamp
private LocalDateTime createdAt;



    public AssessmentGrade(
    Assessment assessment,
    Student student,
    QuizSubmission quizSubmission,
    AssignmentSubmission assignmentSubmission,
    Double autoGradedScore,
    Double finalScore,
    String gradingComments,
    boolean fullyGraded
) {
    this.assessment = assessment;
    this.student = student;
    this.quizSubmission = quizSubmission;
    this.assignmentSubmission = assignmentSubmission;
    this.autoGradedScore = autoGradedScore;
    this.finalScore = finalScore;
    this.gradingComments = gradingComments;
    this.fullyGraded = fullyGraded;
}

}



