package com.example.project.CourseInstructors;

import com.example.project.Courses.Course;
import com.example.project.Instructors.Instructor;
import com.example.project.Sections.Section;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course_instructors", 
       uniqueConstraints = @UniqueConstraint(
         columnNames = {"instructor_id","course_id","section_id"}))
public class CourseInstructor {
  @Id @GeneratedValue Long id;

  @ManyToOne @JoinColumn(name="instructor_id", nullable=false)
  private Instructor instructor;

  @ManyToOne @JoinColumn(name="course_id",     nullable=false)
  private Course course;

  // ← remove semester, sectionNumber

  @ManyToOne 
  @JoinColumn(name="section_id", nullable=false)
  private Section section;

  public CourseInstructor(Instructor instructor,
                          Course course,
                          Section section) {
    this.instructor = instructor;
    this.course     = course;
    this.section    = section;
  }
}
