package com.example.project.Assessments.AssessmentsQuizSubmission;

import java.time.LocalDateTime;

import com.example.project.Assessments.SubmissionStatus;
import com.example.project.Assessments.AssessmentsQuizDetails.QuizDetails;
import com.example.project.Students.Student;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quiz_submissions", uniqueConstraints = {
})
public class QuizSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne 
    @JoinColumn(name = "quiz_id", nullable = false)
    private QuizDetails quiz;

 @ManyToOne
@JoinColumn(
    name="student_id",
    nullable=false,
    unique=false    // <- explicitly prevent a unique index
)
private Student student;

      @Enumerated(EnumType.STRING)
private SubmissionStatus submissionStatus = SubmissionStatus.NOT_SUBMITTED;

    private LocalDateTime startedAt; 
    private LocalDateTime submittedAt; 
    private Integer durationMinutes;

 
  @Column(columnDefinition = "JSON") // ✅ MySQL optimized
    private String autoGradedAnswers; //  JSON 


  @Column(columnDefinition = "JSON") // ✅ MySQL optimized
    private String manuallyGradedAnswers; // JSON 

@Column
private Double autoGradedScore;


public QuizSubmission(QuizDetails quiz, Student student, SubmissionStatus submissionStatus,
                      LocalDateTime startedAt, LocalDateTime submittedAt,
                      String autoGradedAnswers, String manuallyGradedAnswers, Double autoGradedScore) {
    this.quiz = quiz;
    this.student = student;
    this.submissionStatus = submissionStatus;
    this.startedAt = startedAt;
    this.submittedAt = LocalDateTime.now();
    this.autoGradedAnswers = autoGradedAnswers;
    this.manuallyGradedAnswers = manuallyGradedAnswers;
    this.durationMinutes = (int) java.time.Duration.between(startedAt, submittedAt).toMinutes();
    this.autoGradedScore = autoGradedScore;
}

public QuizSubmission(QuizDetails quiz, Student student, SubmissionStatus submissionStatus,
                      LocalDateTime startedAt, LocalDateTime submittedAt,
                      String autoGradedAnswers, String manuallyGradedAnswers) {
    this.quiz = quiz;
    this.student = student;
    this.submissionStatus = submissionStatus;
    this.startedAt = startedAt;
    this.submittedAt = LocalDateTime.now();
    this.autoGradedAnswers = autoGradedAnswers;
    this.manuallyGradedAnswers = manuallyGradedAnswers;
    this.durationMinutes = (int) java.time.Duration.between(startedAt, submittedAt).toMinutes();
}

}
