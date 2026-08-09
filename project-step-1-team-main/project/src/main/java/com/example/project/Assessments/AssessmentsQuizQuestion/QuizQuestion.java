package com.example.project.Assessments.AssessmentsQuizQuestion;

import java.util.ArrayList;
import java.util.List;

import com.example.project.Assessments.AssessmentsQuizDetails.QuizDetails;
import com.example.project.Assessments.AssessmentsQuizOption.QuizOption;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quiz_questions")
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
private QuizDetails quiz;

@OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
private List<QuizOption> options = new ArrayList<>();


   @Column(nullable = false)
   private int questionNumber; 

    @Column(nullable = false)
    private Double score;

    
    private String questionText; 

    @Enumerated(EnumType.STRING)
    private QuestionType questionType; 

    private boolean isAutoGraded; 
@Column(name = "correct_option_id")
private Long correctOptionId;


    public QuizQuestion(QuizDetails quiz, int questionNumber, Double score, String questionText, 
                    QuestionType questionType, boolean isAutoGraded) {
    this.quiz = quiz;
    this.questionNumber = questionNumber;
    this.score = score;
    this.questionText = questionText;
    this.questionType = questionType;
    this.isAutoGraded = isAutoGraded;
}






   
}
