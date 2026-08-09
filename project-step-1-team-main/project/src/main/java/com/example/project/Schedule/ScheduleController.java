// src/main/java/com/example/project/Schedule/ScheduleController.java
package com.example.project.Schedule;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService service;

    public ScheduleController(ScheduleService service) {
        this.service = service;
    }

    /**
     * GET /schedule/student/{studentId}
     * Returns all schedule entries for the given student.
     */
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR','STUDENT')")
    @GetMapping("/student/{studentId}")
    public List<ScheduleEntryDTO> forStudent(@PathVariable Long studentId) {
        return service.getScheduleForStudent(studentId);
    }
}
