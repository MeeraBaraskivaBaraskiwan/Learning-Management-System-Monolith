package com.example.project.Notifications.UserPrefrence;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import com.example.project.Users.User;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity
@Table(name = "user_preferences")

public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private boolean emailEnabled = true;  
    private boolean smsEnabled = false;   
    private String phoneNumber;           
}

