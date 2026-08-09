package com.example.project.Assessments.AssessmentsQuizDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.project.Assessments.AssessmentsQuizQuestion.QuizQuestion;
import com.example.project.Assessments.AssessmentsQuizSubmission.QuizSubmission;
import com.example.project.Assessments.Assessments_Assessment.Assessment;
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
@Table(name = "quiz_details")
public class QuizDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "assessment_id", nullable = false, unique = true)
    private Assessment assessment; 

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
private List<QuizSubmission> quizSubmissions = new ArrayList<>();


    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizQuestion> questions = new ArrayList<>(); 

    private LocalDateTime openTime; 
    private LocalDateTime closingTime;
    private Integer timeLimitMinutes; 

    private boolean published = false; 

    @Column(nullable = false)
    private Double totalScore;


    
    public QuizDetails(Assessment assessment, LocalDateTime openTime, LocalDateTime closingTime,
                   Integer timeLimitMinutes, boolean published, Double totalScore) {
    this.assessment = assessment;
    this.openTime = openTime;
    this.closingTime = closingTime;
    this.timeLimitMinutes = timeLimitMinutes;
    this.published = published;
    this.totalScore = totalScore;
}


}

