// src/main/java/com/example/project/Schedule/ScheduleEntryDTO.java
package com.example.project.Schedule;

import java.time.DayOfWeek;
import java.time.LocalTime;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleEntryDTO {
    private Long id;
    private Long sectionId;
    private String courseName;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}
