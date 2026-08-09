package com.example.project.Assessments.AssessmentsQuizOption;

import com.example.project.Assessments.AssessmentsQuizQuestion.QuizQuestion;

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
@Table(name = "quiz_options")
public class QuizOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quiz_question_id", nullable = false)
    private QuizQuestion question; 

     @Column(nullable = false)
    private String optionText; 

    @Column(nullable = false)
    private boolean isCorrect; 


    public QuizOption(QuizQuestion question, String optionText, boolean isCorrect) {
        this.question = question;
        this.optionText = optionText;
        this.isCorrect = isCorrect;
    }

}

