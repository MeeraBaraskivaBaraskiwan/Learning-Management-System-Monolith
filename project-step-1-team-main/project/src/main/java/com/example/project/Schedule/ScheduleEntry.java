// src/main/java/com/example/project/Schedule/ScheduleEntry.java
package com.example.project.Schedule;

import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.persistence.*;

import com.example.project.Courses.Course;
import com.example.project.Sections.Section;
import lombok.*;

@Entity
@Table(name = "schedule_entries")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ScheduleEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @ManyToOne
@JoinColumn(name = "course_id", nullable = false)
private Course course;

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
}
