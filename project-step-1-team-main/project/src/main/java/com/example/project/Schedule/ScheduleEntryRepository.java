// src/main/java/com/example/project/Schedule/ScheduleEntryRepository.java
package com.example.project.Schedule;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleEntryRepository extends JpaRepository<ScheduleEntry, Long> {
    List<ScheduleEntry> findAllBySectionIdIn(List<Long> sectionIds);
}
