package com.example.project.Sections;

import java.util.List;

public interface SectionService {

    List<SectionDTO> getPlainSectionsByCourse(Long courseId);

}
