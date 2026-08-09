// src/main/java/com/example/project/Instructors/GradebookRowDTO.java
package com.example.project.Instructors;

import java.math.BigDecimal;

public class GradebookRowDTO {
    private final Long    submissionId;
    private final String  studentName;
    private final String  courseName;
    private final String  assessmentTitle;
    private final Double  score;
    private final Double  maxScore;
    private final Boolean released;

    public GradebookRowDTO(
        Long        submissionId,
        String      firstName,
        String      lastName,
        String      courseName,
        String      assessmentTitle,
        BigDecimal  score,
        BigDecimal  maxScore,
        Boolean     released
    ) {
        this.submissionId    = submissionId;
        this.studentName     = firstName + " " + lastName;
        this.courseName      = courseName;
        this.assessmentTitle = assessmentTitle;
        this.score           = score     != null ? score.doubleValue()    : 0.0;
        this.maxScore        = maxScore  != null ? maxScore.doubleValue() : 0.0;
        this.released        = released  != null ? released              : false;
    }

    // … getters …
    public Long   getSubmissionId()    { return submissionId; }
    public String getStudentName()     { return studentName;    }
    public String getCourseName()      { return courseName;     }
    public String getAssessmentTitle() { return assessmentTitle;}
    public Double getScore()           { return score;          }
    public Double getMaxScore()        { return maxScore;       }
    public Boolean getReleased()       { return released;       }
}
