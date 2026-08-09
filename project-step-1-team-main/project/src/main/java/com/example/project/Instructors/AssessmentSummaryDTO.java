package com.example.project.Instructors;

import java.time.LocalDateTime;
import java.sql.Timestamp;

public class AssessmentSummaryDTO {
    private final Long            id;
    private final String          title;
    private final String          courseCode;
    private final String          type;
    private final LocalDateTime   dueDate;
    private final Long            submissions;
    private final Double          averageGrade;

    /** 
     * The “public API” constructor your service and JSON mapper will use.
     */
    public AssessmentSummaryDTO(
        Long id,
        String title,
        String courseCode,
        String type,
        LocalDateTime dueDate,
        Long submissions,
        Double averageGrade
    ) {
        this.id            = id;
        this.title         = title;
        this.courseCode    = courseCode;
        this.type          = type;
        this.dueDate       = dueDate;
        this.submissions   = submissions;
        this.averageGrade  = averageGrade;
    }

    /**
     * Hibernate will call *this* when you do
     *   SELECT new com...AssessmentSummaryDTO(
     *      a.id, a.title, a.course.code, a.type.name(),
     *      a.dueDate, COUNT(g), AVG(g.finalScore)
     *   )
     *
     * – a.dueDate  → java.sql.Timestamp
     * – COUNT(g)   → Long
     * – AVG(...)   → Double
     */
    public AssessmentSummaryDTO(
        Long      id,
        String    title,
        String    courseCode,
        String    type,
        Timestamp dueDate,
        Long      submissions,
        Double    averageGrade
    ) {
        this(
            id,
            title,
            courseCode,
            type,
            // convert Timestamp → LocalDateTime
            dueDate != null
              ? dueDate.toLocalDateTime()
              : null,
            // guard null → 0L
            submissions  != null
              ? submissions
              : 0L,
            // guard null → 0.0
            averageGrade != null
              ? averageGrade
              : 0.0
        );
    }

    // ── Getters ────────────────────────────────────────────────────────────────
    public Long          getId()           { return id; }
    public String        getTitle()        { return title; }
    public String        getCourseCode()   { return courseCode; }
    public String        getType()         { return type; }
    public LocalDateTime getDueDate()      { return dueDate; }
    public Long          getSubmissions()  { return submissions; }
    public Double        getAverageGrade() { return averageGrade; }
}
