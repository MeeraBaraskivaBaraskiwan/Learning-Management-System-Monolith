package com.example.project.Progress;

import java.time.LocalDateTime;

import com.example.project.Enrollments.Enrollment;
import com.example.project.CourseContents.CourseContent; 

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "progress_tracking")
public class Progress {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @ManyToOne
    @JoinColumn(name = "course_content_id", nullable = true)
    private CourseContent courseContent;

    @Column(nullable = false)
    @Min(value = 1, message = "Progress must be at least 1")
    @Max(value = 100, message = "Progress must be at most 100")
    private double progress; 

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    
    @Min(value = 0, message = "Completed tasks must be at least 0")
    private double completedTasks;

    @Column(nullable = false)
    @Min(value = 1, message = "Total tasks must be at least 1")
    private double totalTasks;

    public Progress(Enrollment enrollment, double progress, String updateReason) {
        this.enrollment = enrollment;
        this.progress = progress;
        this.updatedAt = LocalDateTime.now();
        this.completedTasks = 0; 
        this.totalTasks = 1;      
    }
    
    public Progress(Enrollment enrollment, CourseContent courseContent, double progress, String updateReason){
        this.enrollment = enrollment;
        this.courseContent = courseContent;
        this.progress = progress;
        this.updatedAt = LocalDateTime.now();
        this.completedTasks = 0;
        this.totalTasks = 1;     
    }


}
