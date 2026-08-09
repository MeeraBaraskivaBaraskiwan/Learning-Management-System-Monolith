package com.example.project.Instructors;

import java.util.ArrayList;
import java.util.List;

import com.example.project.CourseInstructors.CourseInstructor;
import com.example.project.Users.User;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "instructors", uniqueConstraints = {
    @UniqueConstraint(columnNames = "instructorId")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(unique = true, nullable = false)
    private String instructorId;

    @Column(nullable = false)
    private String faculty;

    @Column(nullable = false)
    private String department;

    
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseInstructor> coursesTaught = new ArrayList<>();

    public Instructor(User user, String instructorId, String faculty, String department) {
        this.user = user;
        this.instructorId = instructorId;
        this.faculty = faculty;
        this.department = department;
    }

}

