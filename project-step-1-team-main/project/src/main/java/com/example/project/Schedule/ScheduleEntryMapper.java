// src/main/java/com/example/project/Schedule/ScheduleEntryMapper.java
package com.example.project.Schedule;

import org.springframework.stereotype.Component;

@Component
public class ScheduleEntryMapper {
    public ScheduleEntryDTO toDTO(ScheduleEntry e) {
        var dto = new ScheduleEntryDTO();
        dto.setId(e.getId());
        dto.setSectionId(e.getSection().getId());
        dto.setCourseName(e.getSection().getCourse().getName()); // ← Add this
        dto.setDayOfWeek(e.getDayOfWeek());
        dto.setStartTime(e.getStartTime());
        dto.setEndTime(e.getEndTime());
        return dto;
    }
}
