// src/main/java/com/example/project/Schedule/ScheduleServiceImpl.java
package com.example.project.Schedule;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.Enrollments.Enrollment;
import com.example.project.Enrollments.EnrollmentRepository;

@Transactional(readOnly = true)
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final EnrollmentRepository       enrollRepo;
    private final ScheduleEntryRepository    schedRepo;
    private final ScheduleEntryMapper        mapper;

    public ScheduleServiceImpl(EnrollmentRepository enrollRepo,
                               ScheduleEntryRepository schedRepo,
                               ScheduleEntryMapper mapper) {
        this.enrollRepo = enrollRepo;
        this.schedRepo  = schedRepo;
        this.mapper     = mapper;
    }

    @Override
    public List<ScheduleEntryDTO> getScheduleForStudent(Long studentUserId) {
        // 1) fetch all this student's enrollments
 List<Enrollment> enrolls = enrollRepo.findAllByStudent_User_Id(studentUserId);

        // 2) extract each section’s ID
        List<Long> sectionIds = enrolls.stream()
                                       .map(e -> e.getSection().getId())
                                       .toList();

        // 3) load and map schedule entries
        return schedRepo.findAllBySectionIdIn(sectionIds)
                        .stream()
                        .map(mapper::toDTO)
                        .collect(Collectors.toList());
    }
}
