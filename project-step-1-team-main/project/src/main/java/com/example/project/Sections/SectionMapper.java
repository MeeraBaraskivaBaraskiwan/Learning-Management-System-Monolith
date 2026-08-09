package com.example.project.Sections;

import org.springframework.stereotype.Component;

import com.example.project.Courses.CourseRepository;
import com.example.project.Semesters.SemesterMapper;
import com.example.project.Semesters.SemesterRepository;

@Component
public class SectionMapper {
    private final CourseRepository courseRepo;
    private final SemesterRepository semRepo;
    private final SemesterMapper semesterMapper;

    public SectionMapper(CourseRepository courseRepo,
                       SemesterRepository semRepo,
                       SemesterMapper semesterMapper) {
    this.courseRepo = courseRepo;
    this.semRepo    = semRepo;
    this.semesterMapper = semesterMapper;
  }

    public SectionDTO toDTO(Section s) {
        if (s == null) return null;
        SectionDTO dto = new SectionDTO();
        dto.setId(s.getId());
        dto.setCourseId(s.getCourse().getId());
        dto.setNumber(s.getNumber());
        dto.setSchedule(s.getSchedule());
        dto.setSemesterId(s.getSemester().getId());
        dto.setSemester(semesterMapper.toDTO(s.getSemester()));
        return dto;
    }

    public Section toEntity(SectionDTO dto) {
        var course   = courseRepo.findById(dto.getCourseId()).orElseThrow();
        var semester = semRepo.findById(dto.getSemesterId()).orElseThrow();
        return new Section(course, dto.getNumber(), dto.getSchedule(), semester);
    }
}
