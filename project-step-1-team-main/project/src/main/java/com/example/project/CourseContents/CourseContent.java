package com.example.project.CourseContents;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.project.Courses.Course;
import com.example.project.Files.FileMetadata;
import com.example.project.Instructors.Instructor;
import com.example.project.Sections.Section;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course_contents", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"course_id", "instructor_id", "title","section_id"})
})
public class CourseContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course; 

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor; 

     @ManyToOne @JoinColumn(name = "section_id")
     private Section section;

    @OneToMany(mappedBy = "courseContent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileMetadata> fileMetadataList = new ArrayList<>();


    @Column(nullable = false)
    private String title; 
    
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadDate = LocalDateTime.now(); 

    public CourseContent(Course course, Instructor instructor, String title,Section section) {
        this.course = course;
        this.instructor = instructor;
        this.title = title;
        this.section = section;
        this.uploadDate = LocalDateTime.now();
    }
}
