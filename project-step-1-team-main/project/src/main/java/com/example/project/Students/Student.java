package com.example.project.Students;

import java.util.ArrayList;
import java.util.List;

import com.example.project.Enrollments.Enrollment;
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
@Table(name = "students", uniqueConstraints = {
    @UniqueConstraint(columnNames = "studentId")
})
@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(unique = true)
    private String studentId;

    private String major;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();

    public Student(User user, String studentId, String major) {
        this.user = user;
        this.studentId = studentId;
        this.major = major;
    }
}