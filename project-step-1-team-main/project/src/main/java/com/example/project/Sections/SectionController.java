package com.example.project.Sections;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/sections")
public class SectionController {
    private final SectionRepository repo;
    private final SectionMapper mapper;
    private final SectionService sectionService; // Add SectionService field

    public SectionController(SectionRepository repo, SectionMapper mapper, SectionService sectionService) {
        this.repo = repo;
        this.mapper = mapper;
        this.sectionService = sectionService; // Initialize SectionService
    }

    @GetMapping("/by-course/{courseId}")
    public List<SectionDTO> forCourse(@PathVariable Long courseId) {
        return repo.findByCourseId(courseId)
                   .stream()
                   .map(mapper::toDTO)
                   .toList();
    }

    @PostMapping
    public ResponseEntity<SectionDTO> create(@RequestBody SectionDTO dto) {
        Section saved = repo.save(mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDTO(saved));
    }

    // Returns List<SectionDTO> for the course (no HATEOAS)
    @GetMapping("/plain/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    public List<SectionDTO> getPlainSectionsByCourse(@PathVariable Long courseId) {
        return sectionService.getPlainSectionsByCourse(courseId);
    }
}
