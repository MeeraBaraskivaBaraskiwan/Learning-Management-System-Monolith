package com.example.project.Enrollments;

import java.util.ArrayList;
import java.util.List;

import com.example.project.Courses.Course;
import com.example.project.Progress.Progress;
import com.example.project.Sections.Section;
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
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "enrollments", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "course_id", "semester", "sectionNumber"})
   })


    public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

@ManyToOne
@JoinColumn(name = "section_id", nullable = false)
private Section section;




    @Column(nullable = false)
    private boolean completed = false; 

    @Column(nullable = false)
    private double currentProgress = 0.0;

    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Progress> progressRecords  = new ArrayList<>();

    public void markAsCompleted() {
        this.completed = true;
    }


public Enrollment(Student student, Section section) {
        this.student = student;
        this.section = section;
        this.course  = section.getCourse();
        this.completed      = false;
        this.currentProgress = 0.0;
    }
}