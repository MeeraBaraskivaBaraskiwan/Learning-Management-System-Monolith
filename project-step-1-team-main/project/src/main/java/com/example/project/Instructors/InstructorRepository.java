package com.example.project.Instructors;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long>,
            InstructorRepositoryCustom    {
    Optional<Instructor> findByInstructorId(String instructorId);

    @EntityGraph(attributePaths = {"user"})
    Page<Instructor> findAll(Pageable pageable);


    Optional<Instructor> findByUser_Id(Long userId); 


    
}
