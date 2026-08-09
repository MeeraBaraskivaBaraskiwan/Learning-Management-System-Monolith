package com.example.project.Instructors;
import java.util.List;
public record DashboardStatsDTO(
    long   totalCourses,
    long   totalStudents,
    long   totalAssessments,     // replaces “products” metric
    double passed,               // %
    double impact,               // arbitrary “engagement” or avg grade
    List<PieSlice>   courseReport,
    List<SubmissionRow> recentSubmissions
) {
    public record PieSlice(String label, long value) {}
    public record SubmissionRow(
        Long   id,
        String title,
        String course,
        String lesson,
        String date,
        double points,
        String status,
        String type
    ) {}
}