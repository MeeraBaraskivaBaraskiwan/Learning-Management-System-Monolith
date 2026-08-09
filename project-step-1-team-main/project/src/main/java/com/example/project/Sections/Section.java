package com.example.project.Sections;

import jakarta.persistence.*;
import lombok.*;

import com.example.project.Courses.Course;
import com.example.project.Semesters.Semester;

@Entity
@Table(name = "sections",
       uniqueConstraints = @UniqueConstraint(columnNames = {"course_id","number","semester_id"}))
@Data @NoArgsConstructor @AllArgsConstructor
public class Section {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name="course_id", nullable=false)
    private Course course;

    @Column(name="number", nullable=false)
    private int number;          // e.g. 1, 2

    @Column(nullable=false)
    private String schedule;    // e.g. "MWF 10–11"

    @ManyToOne @JoinColumn(name="semester_id", nullable=false)
    private Semester semester;

    public Section(Course course, int number, String schedule, Semester semester) {
        this.course = course;
        this.number = number;
        this.schedule = schedule;
        this.semester = semester;
    }
}
