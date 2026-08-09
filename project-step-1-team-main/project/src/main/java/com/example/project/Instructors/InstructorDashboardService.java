package com.example.project.Instructors;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.project.Users.DirectoryUserDTO;
import com.example.project.Users.UserRepository;
import com.example.project.CourseContents.CourseContentDTO;
import com.example.project.Instructors.DashboardStatsDTO;
import com.example.project.Students.StudentDTO;
import java.io.IOException;

// import com.example.project.Assessments.AssessmentSummaryDTO; // Add correct import
// import com.example.project.Gradebook.GradebookRowDTO;       // Add correct import

public interface InstructorDashboardService {
    DashboardStatsDTO getDashboard(Long instructorId);
    List<StudentDTO> getStudents(Long instructorId);
    List<AssessmentSummaryDTO> getAssessments(Long instructorId);
    List<DirectoryUserDTO> getUsers(Long instructorId);
    List<GradebookRowDTO> getGradebook(Long instructorId);
    List<CourseContentDTO> getContentForInstructor(Long id);

    /**
     * Upload a new piece of course content (entity + file metadata).
     *
     * @param instructorId  the current instructor’s ID
     * @param file          the binary file to store
     * @param dto           carries courseId, sectionId + title
     */
    CourseContentDTO saveContent(
        Long instructorId,
        MultipartFile file,
        CourseContentDTO dto
    )throws IOException;
}
