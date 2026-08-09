package com.example.project.Semesters;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/semesters")
public class SemesterController {
    private final SemesterRepository repo;
    private final SemesterMapper mapper;

    public SemesterController(SemesterRepository repo, SemesterMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @GetMapping
    public List<SemesterDTO> all() {
        return repo.findAll()
                   .stream()
                   .map(mapper::toDTO)
                   .toList();
    }

    @PostMapping
    public ResponseEntity<SemesterDTO> create(@RequestBody SemesterDTO dto) {
        Semester saved = repo.save(mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDTO(saved));
    }
}
