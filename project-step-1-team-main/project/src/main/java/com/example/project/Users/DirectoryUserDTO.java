// src/main/java/com/example/project/Users/DirectoryUserDTO.java
package com.example.project.Users;

public class DirectoryUserDTO {
    private final Long   id;
    private final String email;
    private final String role;

    public DirectoryUserDTO(Long id, String email, String role) {
        this.id    = id;
        this.email = email;
        this.role  = role;
    }

    public Long getId()       { return id; }
    public String getEmail()  { return email; }
    public String getRole()   { return role; }
}
