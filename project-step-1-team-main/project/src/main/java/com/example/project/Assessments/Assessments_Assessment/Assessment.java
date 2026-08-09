package com.example.project.Assessments.Assessments_Assessment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.example.project.Assessments.AssessmentsAssessmentGrade.AssessmentGrade;
import com.example.project.Assessments.AssessmentsAssignmentDetails.AssignmentDetails;
import com.example.project.Assessments.AssessmentsQuizDetails.QuizDetails;
import com.example.project.Courses.Course;
import com.example.project.Instructors.Instructor;
import com.example.project.Sections.Section;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "assessments")
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Enumerated(EnumType.STRING)
    private AssessmentType type; 

    @OneToOne(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true)
     private AssignmentDetails assignmentDetails;

    @OneToOne(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true)
      private QuizDetails quizDetails;


    private String title; 
    private String description; 

    @CreationTimestamp
    private LocalDateTime createdAt; 

    private LocalDateTime dueDate; 

    @OneToMany(mappedBy = "assessment")
private List<AssessmentGrade> submissions = new ArrayList<>();


public Assessment(Course course,
                      Instructor instructor,
                      Section section,
                      AssessmentType type,
                      String title,
                      String description,
                      LocalDateTime dueDate) {
        this.course = course;
        this.instructor = instructor;
        this.section = section;
        this.type = type;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }
}
