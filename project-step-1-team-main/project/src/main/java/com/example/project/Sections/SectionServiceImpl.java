package com.example.project.Sections;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;
    private final SectionMapper sectionMapper;

    // Constructor for dependency injection
    public SectionServiceImpl(SectionRepository sectionRepository, SectionMapper sectionMapper) {
        this.sectionRepository = sectionRepository;
        this.sectionMapper = sectionMapper;
    }

    @Override
    public List<SectionDTO> getPlainSectionsByCourse(Long courseId) {
        return sectionRepository.findByCourseId(courseId)
            .stream()
            .map(sectionMapper::toDTO)
            .collect(Collectors.toList());
    }
}
