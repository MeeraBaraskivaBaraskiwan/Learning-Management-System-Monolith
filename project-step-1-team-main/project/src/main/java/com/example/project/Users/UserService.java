package com.example.project.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

public interface UserService {
    PagedModel<EntityModel<UserDTO>> getAllUsers(Pageable pageable);

    EntityModel<UserDTO> getUserById(Long id);

    ResponseEntity<?> newUser(UserDTO newUserDTO);

    ResponseEntity<?> updateUser(Long id, UserDTO updatedUserDTO);

    ResponseEntity<?> deleteUser(Long id);
}