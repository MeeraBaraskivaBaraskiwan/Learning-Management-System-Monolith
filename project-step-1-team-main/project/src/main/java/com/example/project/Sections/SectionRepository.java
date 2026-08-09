package com.example.project.Sections;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section,Long> {
    List<Section> findByCourseId(Long courseId);
}
