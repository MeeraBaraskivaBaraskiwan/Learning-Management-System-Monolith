// src/main/java/com/example/project/Schedule/ScheduleService.java
package com.example.project.Schedule;

import java.util.List;

public interface ScheduleService {
    List<ScheduleEntryDTO> getScheduleForStudent(Long studentId);
}
