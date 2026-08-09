package com.example.project;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.project.Assessments.AssessmentsAssignmentDetails.AssignmentDetails;
import com.example.project.Assessments.AssessmentsAssignmentDetails.AssignmentDetailsRepository;
import com.example.project.Assessments.AssessmentsAssignmentSubmission.AssignmentSubmission;
import com.example.project.Assessments.AssessmentsAssignmentSubmission.AssignmentSubmissionRepository;
import com.example.project.Assessments.AssessmentsQuizDetails.QuizDetails;
import com.example.project.Assessments.AssessmentsQuizDetails.QuizDetailsRepository;
import com.example.project.Assessments.AssessmentsQuizOption.QuizOption;
import com.example.project.Assessments.AssessmentsQuizOption.QuizOptionRepository;
import com.example.project.Assessments.AssessmentsQuizQuestion.QuestionType;
import com.example.project.Assessments.AssessmentsQuizQuestion.QuizQuestion;
import com.example.project.Assessments.AssessmentsQuizQuestion.QuizQuestionRepository;
import com.example.project.Assessments.AssessmentsQuizSubmission.QuizSubmission;
import com.example.project.Assessments.AssessmentsQuizSubmission.QuizSubmissionRepository;
import com.example.project.Assessments.Assessments_Assessment.Assessment;
import com.example.project.Assessments.Assessments_Assessment.AssessmentRepository;
import com.example.project.Assessments.Assessments_Assessment.AssessmentType;
import com.example.project.Assessments.SubmissionStatus;
import com.example.project.Courses.Course;
import com.example.project.Courses.CourseRepository;
import com.example.project.Enrollments.Enrollment;
import com.example.project.Enrollments.EnrollmentRepository;
import com.example.project.Instructors.Instructor;
import com.example.project.Instructors.InstructorRepository;
import com.example.project.Profiles.Profile.Profile;
import com.example.project.Profiles.Profile.ProfileRepository;
import com.example.project.Profiles.InstructorProfile.InstructorProfile;
import com.example.project.Profiles.InstructorProfile.InstructorProfileRepository;
import com.example.project.Profiles.StudentProfile.StudentProfile;
import com.example.project.Profiles.StudentProfile.StudentProfileRepository;
import com.example.project.Progress.Progress;
import com.example.project.Progress.ProgressRepository;
import com.example.project.Sections.Section;
import com.example.project.Sections.SectionRepository;
import com.example.project.Semesters.Semester;
import com.example.project.Semesters.SemesterRepository;
import com.example.project.Students.Student;
import com.example.project.Students.StudentRepository;
import com.example.project.Users.Role;
import com.example.project.Users.RoleRepository;
import com.example.project.Users.User;
import com.example.project.Users.UserRepository;
import com.example.project.Assessments.AssessmentsAssessmentGrade.AssessmentGrade;
import com.example.project.Assessments.AssessmentsAssessmentGrade.AssessmentGradeRepository;
import com.example.project.CourseContents.CourseContent;
import com.example.project.CourseContents.CourseContentRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            RoleRepository roleRepository,
            InstructorRepository instructorRepository,
            CourseRepository courseRepository,
            AssessmentRepository assessmentRepository,
            AssignmentDetailsRepository assignmentDetailsRepository,
            AssignmentSubmissionRepository assignmentSubmissionRepository,
            QuizDetailsRepository quizDetailsRepository,
            QuizQuestionRepository quizQuestionRepository,
            QuizOptionRepository quizOptionRepository,
            QuizSubmissionRepository quizSubmissionRepository,
            StudentRepository studentRepository,
            ProfileRepository profileRepository,                
            InstructorProfileRepository instructorProfileRepository, 
            StudentProfileRepository studentProfileRepository,
            AssessmentGradeRepository gradeRepository,
            CourseContentRepository courseContentRepository,
            SectionRepository sectionRepository,
            EnrollmentRepository enrollmentRepository,
            ProgressRepository progressRepository,
             PasswordEncoder passwordEncoder,
             SemesterRepository semesterRepository) {


         return args -> {
                    if (userRepository.count() > 0) {
                        log.info("🔁 Skipping database seeding because data already exists.");
                        return;
                    }
            // 1️⃣ Create roles
            Role adminRole = new Role("ADMIN");
            Role instructorRole = new Role("INSTRUCTOR");
            Role studentRole = new Role("STUDENT");
            roleRepository.save(adminRole);
            roleRepository.save(instructorRole);
            roleRepository.save(studentRole);

            // 2️⃣ Create admin user (NO PASSWORD ENCODING YET)
            User adminUser = new User("admin@example.com", "Admin", "User", passwordEncoder.encode("admin123"), adminRole);
            userRepository.save(adminUser);

            // 3️⃣ Create instructor users
            User instructorUser1 = new User("jane.doe@example.com", "Jane", "Doe",  passwordEncoder.encode("password123"), instructorRole);
            User instructorUser2 = new User("john.doe@example.com", "John", "Doe", passwordEncoder.encode("password123"), instructorRole);
            User instructorUser3 = new User("sophia.anderson@example.com", "Sophia", "Anderson", passwordEncoder.encode("password123"), instructorRole);
            User instructorUser4 = new User("michael.scott@example.com", "Michael", "Scott", passwordEncoder.encode("password123"), instructorRole);
            userRepository.save(instructorUser1);
            userRepository.save(instructorUser2);
            userRepository.save(instructorUser3);
            userRepository.save(instructorUser4);

            // 4️⃣ Create student users
            User studentUser1 = new User("student1@example.com", "Alice", "Smith", passwordEncoder.encode("student123"), studentRole);
            User studentUser2 = new User("student2@example.com", "Bob", "Brown", passwordEncoder.encode("student123"), studentRole);
            userRepository.save(studentUser1);
            userRepository.save(studentUser2);

            // 5️⃣ Create instructors
            Instructor instructor1 = new Instructor(instructorUser1, "INSTR001", "Engineering Faculty", "Computer Science");
            Instructor instructor2 = new Instructor(instructorUser2, "INSTR002", "Engineering Faculty", "Information Technology");
            Instructor instructor3 = new Instructor(instructorUser3, "INSTR003", "Mathematics Faculty", "Statistics");
            Instructor instructor4 = new Instructor(instructorUser4, "INSTR004", "Business Faculty", "Management");
            instructorRepository.save(instructor1);
            instructorRepository.save(instructor2);
            instructorRepository.save(instructor3);
            instructorRepository.save(instructor4);

            // 6️⃣ Create Student from studentUser1
        Student student1 = new Student( studentUser1, "STU001", "Computer Science" );
        Student student2 = new Student(studentUser2, "STU002", "Information Technology");

        studentRepository.save(student1);
        studentRepository.save(student2);


            // 7️⃣ Create courses
            Course course1 = new Course("Software Engineering", "SE101", "Introduction to Software Engineering", 3);
            Course course2 = new Course("Database Systems", "DB202", "Advanced Database Concepts", 4);
            courseRepository.save(course1);
            courseRepository.save(course2);


    // ─── 1) Seed Spring 2025 semester ───
    Semester spring2025 = semesterRepository
        .findByTermAndYear("Spring", 2025)
        .orElseGet(() -> semesterRepository.save(new Semester("Spring", 2025)));

    // ─── 2) Create & save sections ───
    Section section1 = new Section(
        course1,
        1,
        "Mon/Wed/Fri 10:00–11:00",
        spring2025
    );
    Section section2 = new Section(
        course2,
        1,
        "Tue/Thu 13:00–14:30",
        spring2025
    );
    sectionRepository.save(section1);
    sectionRepository.save(section2);


            // 8️⃣ Create assessments
Assessment assessment1 = new Assessment(
    course1,
    instructor1,
    section1,                     
    AssessmentType.QUIZ,
    "Midterm Quiz",
    "Midterm exam covering all topics",
    LocalDateTime.now().plusDays(1)
);

Assessment assessment2 = new Assessment(
    course2,
    instructor2,
    section2,
    AssessmentType.ASSIGNMENT,
    "Final Project",
    "Group project for the final grade",
    LocalDateTime.now().plusDays(3)
);

Assessment assessment3 = new Assessment(
    course1,
    instructor1,
    section1,
    AssessmentType.QUIZ,
    "Practice Quiz",
    "A quiz with no quiz details",
    LocalDateTime.now().plusDays(7)
);

Assessment assessment4 = new Assessment(
    course1,
    instructor1,
    section1,
    AssessmentType.QUIZ,
    "Extra Quiz",
    "An extra practice quiz",
    LocalDateTime.now().plusDays(7)
);

Assessment assessment5 = new Assessment(
    course2,
    instructor2,
    section2,
    AssessmentType.ASSIGNMENT,
    "Mini Project",
    "Solo project for the final grade",
    LocalDateTime.now().plusDays(3)
);

Assessment assessment6 = new Assessment(
    course1,
    instructor1,
    section1,
    AssessmentType.ASSIGNMENT,
    "Research Paper",
    "Write a detailed research paper on microservices architecture.",
    LocalDateTime.now().plusDays(10)
);

Assessment assessment7 = new Assessment(
    course1,
    instructor1,
    section1,
    AssessmentType.ASSIGNMENT,
    "New Case Study",
    "A case study for testing grade creation",
    LocalDateTime.now().plusDays(5)
);
            assessmentRepository.save(assessment1);
            assessmentRepository.save(assessment2);
            assessmentRepository.save(assessment3);
            assessmentRepository.save(assessment4);
            assessmentRepository.save(assessment5);
            assessmentRepository.save(assessment6);
            assessmentRepository.save(assessment7);


            // 9️⃣ Create AssignmentDetails for assessment2 
        AssignmentDetails assignmentDetails = new AssignmentDetails( assessment2, 100.0, "This is the final assignment for the course.",false  );
        AssignmentDetails assignmentDetails2 = new AssignmentDetails(assessment6, 50.0, "This is a research paper assignment with no submissions yet.",false);
        AssignmentDetails assignmentDetails3 = new AssignmentDetails(assessment7,100.0, "Analyze a real-world software system.", false);
        assignmentDetailsRepository.save(assignmentDetails);
        assignmentDetailsRepository.save(assignmentDetails2);
        assignmentDetailsRepository.save(assignmentDetails3);

            // 🔟  Create AssignmentSubmission for student1
        AssignmentSubmission submission = new AssignmentSubmission(assignmentDetails, student1);
        AssignmentSubmission submission2 = new AssignmentSubmission(assignmentDetails, student2);
        submission2.setFeedback("Well done, but review DB normalization.");
        AssignmentSubmission submission3 = new AssignmentSubmission(assignmentDetails3, student2);
        submission3.setFeedback("Detailed analysis. Great effort.");
        assignmentSubmissionRepository.save(submission);
        assignmentSubmissionRepository.save(submission2);
        assignmentSubmissionRepository.save(submission3);


            // 1️⃣1️⃣ Create QuizDetails for assessment1 (Midterm Quiz)
            QuizDetails quizDetails = new QuizDetails(assessment1,LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(3),30, true,20.0);
            quizDetailsRepository.save(quizDetails);

            // 1️⃣2️⃣ Create QuizQuestion with options
            QuizQuestion question1 = new QuizQuestion(quizDetails,1, 10.0,"What is the capital of France?",QuestionType.MULTIPLE_CHOICE,true);
            quizQuestionRepository.save(question1);

            // 1️⃣3️⃣ Create QuizOptions for question1
            QuizOption option1 = new QuizOption(question1, "Paris", true);
            QuizOption option2 = new QuizOption(question1, "London", false);
            QuizOption option3 = new QuizOption(question1, "Rome", false);
            QuizOption option4 = new QuizOption(question1, "Berlin", false);
            quizOptionRepository.save(option1);
            quizOptionRepository.save(option2);
            quizOptionRepository.save(option3);
            quizOptionRepository.save(option4);

            // 1️⃣4️⃣ Create QuizSubmission for student1
            QuizSubmission quizSubmission = new QuizSubmission( quizDetails, student1,SubmissionStatus.SUBMITTED,LocalDateTime.now().minusMinutes(25), LocalDateTime.now(), "{\"1\": \"Paris\"}", "{\"1\": null}"  );
            quizSubmissionRepository.save(quizSubmission);
            

            // 1️⃣5️⃣ 

  // For Instructor 1 (jane.doe@example.com)
  Profile profileInstructor1 = new Profile();
  profileInstructor1.setUser(instructorUser1);
  profileInstructor1.setFullName("Jane Doe");
  profileInstructor1.setPhoneNumber("+11234567890");
  profileInstructor1.setCountry("USA");
  profileInstructor1.setBirthDate(LocalDate.of(1985, 5, 15));
  // Optionally set bio, profilePictureUrl, nationality, gender, etc.
  profileRepository.save(profileInstructor1);

  // For Instructor 2 (john.doe@example.com)
  Profile profileInstructor2 = new Profile();
  profileInstructor2.setUser(instructorUser2);
  profileInstructor2.setFullName("John Doe");
  profileInstructor2.setPhoneNumber("+11234567891");
  profileInstructor2.setCountry("USA");
  profileInstructor2.setBirthDate(LocalDate.of(1980, 3, 10));
  profileRepository.save(profileInstructor2);

  // For Student 1 (Alice Smith)
  Profile profileStudent1 = new Profile();
  profileStudent1.setUser(studentUser1);
  profileStudent1.setFullName("Alice Smith");
  profileStudent1.setPhoneNumber("+11234567892");
  profileStudent1.setCountry("USA");
  profileStudent1.setBirthDate(LocalDate.of(2000, 1, 1));
  profileRepository.save(profileStudent1);

  // --- NEW: Create InstructorProfiles ---
  InstructorProfile instructorProfile1 = new InstructorProfile();
  instructorProfile1.setProfile(profileInstructor1);
  instructorProfile1.setInstructor(instructor1);
  instructorProfile1.setFaculty("Engineering Faculty");
  instructorProfile1.setDepartment("Computer Science");
  instructorProfile1.setAcademicRank("Associate Professor");
  instructorProfileRepository.save(instructorProfile1);

  InstructorProfile instructorProfile2 = new InstructorProfile();
  instructorProfile2.setProfile(profileInstructor2);
  instructorProfile2.setInstructor(instructor2);
  instructorProfile2.setFaculty("Engineering Faculty");
  instructorProfile2.setDepartment("Information Technology");
  instructorProfile2.setAcademicRank("Professor");
  instructorProfileRepository.save(instructorProfile2);

  // --- NEW: Create StudentProfile for Student 1 ---
  StudentProfile studentProfile1 = new StudentProfile();
  studentProfile1.setProfile(profileStudent1);
  studentProfile1.setStudent(student1);
  studentProfile1.setAdmittedYear("2024");
  studentProfile1.setMajor("Computer Science");
  studentProfile1.setMinor("Mathematics");
  studentProfile1.setCurrentSemester("Fall 2024");
  studentProfile1.setDegreeProgram("Bachelor");
  studentProfile1.setStudentLevel("Undergraduate");
  studentProfile1.setTawjihiStream("Science");
  studentProfile1.setProbationStatus("None");
  studentProfile1.setDepartment("Engineering");
  studentProfile1.setAdvisor(instructorProfile1);
  studentProfileRepository.save(studentProfile1);

   // Log statements for new seeding
   log.info("Preloaded Profiles for instructors and students.");
   log.info("Preloaded InstructorProfile for Jane Doe: " + instructorProfile1);
   log.info("Preloaded StudentProfile for Alice Smith: " + studentProfile1);
  
  
  
  // 1️⃣ Grade for student1's quiz on assessment1 (course: SE101, section: 1)
            AssessmentGrade grade1 = new AssessmentGrade(assessment1, student1, quizSubmission, null,9.5, 10.0, "Excellent work!", true);

// 2️⃣ Grade for student1's assignment on assessment2 (course: DB202, section: 2)
            AssessmentGrade grade2 = new AssessmentGrade(assessment2, student1, null, submission, 18.0, 19.5, "Good job, minor issues", true);

// 3️⃣ Grade for student2's assignment on assessment2 (same course: DB202)
          AssessmentGrade grade3 = new AssessmentGrade(assessment2, student2, null, submission2,17.0, 18.0, "Decent effort", true);

// 4️⃣ Grade for student1 on assessment6 (SE101, section 6, no submission)
          AssessmentGrade grade4 = new AssessmentGrade(assessment6, student1, null, null, 0.0, 0.0, "Missing submission", false);

// 5️⃣ Grade for student2 on assessment5 (DB202, section 2, no submission)
         AssessmentGrade grade5 = new AssessmentGrade(assessment5, student2, null, null,0.0, 0.0, "Not submitted", false);


           gradeRepository.save(grade1);
           gradeRepository.save(grade2);
           gradeRepository.save(grade3);
           gradeRepository.save(grade4);
           gradeRepository.save(grade5);
   

            //  Log preloaded data
            log.info("Preloaded ADMIN: " + adminUser);
            log.info("Preloaded INSTRUCTOR 1: " + instructor1);
            log.info("Preloaded INSTRUCTOR 2: " + instructor2);
            log.info("Preloaded STUDENT 1: " + studentUser1);
            log.info("Preloaded STUDENT 2: " + studentUser2);
            log.info("Preloaded COURSE 1: " + course1);
            log.info("Preloaded COURSE 2: " + course2);
            log.info("Preloaded ASSESSMENT 1: " + assessment1);
            log.info("Preloaded ASSESSMENT 2: " + assessment2);
            log.info("Preloaded ASSESSMENT 2: " + assessment3);
            log.info("Preloaded ASSESSMENT 2: " + assessment4);
            log.info("Preloaded ASSESSMENT 2: " + assessment5);
            log.info("Preloaded ASSESSMENT 6: " + assessment6);
            log.info("Created NEW test assessment: " + assessment7);
            log.info("Preloaded ASSIGNMENT DETAILS: " + assignmentDetails);
            log.info("Preloaded ASSIGNMENT DETAILS (no submissions): " + assignmentDetails2);
            log.info("Created NEW assignment details: " + assignmentDetails3);
            log.info("Preloaded STUDENT: " + student1);
            log.info("Preloaded ASSIGNMENT SUBMISSION: " + submission);
            log.info("Created NEW submission (for student2): " + submission3);
            log.info("Preloaded QUIZ DETAILS: " + quizDetails);
            log.info("Preloaded QUIZ QUESTION: " + question1);
            log.info("Preloaded QUIZ SUBMISSION: " + quizSubmission);


     //  Course Content
       
        CourseContent courseContent1 = new CourseContent( 
        course1,instructor1,"Lecture 1: Introduction to Software Engineering", section1        );
       
CourseContent courseContent2 = new CourseContent(
    course1,
    instructor1,
    "Lecture 2: Agile Methodologies",
    section1
);
CourseContent courseContent3 = new CourseContent(
    course2,
    instructor2,
    "Lecture 1: Relational Databases",
    section2
);
        
        courseContentRepository.save(courseContent1);
        courseContentRepository.save(courseContent2);
        courseContentRepository.save(courseContent3);
        
        log.info("📘 Preloaded COURSE CONTENT 1: " + courseContent1);
        log.info("📘 Preloaded COURSE CONTENT 2: " + courseContent2);
        log.info("📘 Preloaded COURSE CONTENT 3: " + courseContent3);
    

   
            //enrollment and progress
            // Enrollment1: For student1 in course1 ("Fall 2023", section 1) with partial progress
       Enrollment enrollment1 = new Enrollment(student1, section1);            enrollmentRepository.save(enrollment1);
            log.info("Preloaded Enrollment1: " + enrollment1);

            List<CourseContent> modulesCourse1 = courseContentRepository.findByCourseId(course1.getId());
            if (!modulesCourse1.isEmpty()) {
                int completedCount = 0;
                // Mark first two modules as completed for Enrollment1
                for (CourseContent module : modulesCourse1) {
                    if (completedCount < 2) {
                        Progress progress = new Progress(enrollment1, module, 1, "Module completed");
                        progress.setUpdatedAt(LocalDateTime.now());
                        progressRepository.save(progress);
                        enrollment1.getProgressRecords().add(progress);
                        completedCount++;
                    }
                }
                int totalModules = modulesCourse1.size();
                long completedModules = enrollment1.getProgressRecords().stream().filter(p -> p.getCourseContent() != null)
                        .map(p -> p.getCourseContent().getId()).distinct().count();
                double overallProgress = ((double) completedModules / totalModules) * 100;
                enrollment1.setCurrentProgress(overallProgress);
                enrollmentRepository.save(enrollment1);
                log.info("Enrollment1 currentProgress updated to: " + overallProgress);
            }

            // Enrollment2: For student2 in course2 ("Fall 2023", section 1) with full progress
         Enrollment enrollment2 = new Enrollment(student2, section2);
            enrollmentRepository.save(enrollment2);
            log.info("Preloaded Enrollment2: " + enrollment2);

            List<CourseContent> modulesCourse2 = courseContentRepository.findByCourseId(course2.getId());
            if (!modulesCourse2.isEmpty()) {
                // Mark all modules as completed for Enrollment2
                for (CourseContent module : modulesCourse2) {
                    Progress progress = new Progress(enrollment2, module, 1, "Module completed");
                    progress.setUpdatedAt(LocalDateTime.now());
                    progressRepository.save(progress);
                    enrollment2.getProgressRecords().add(progress);
                }
                int totalModules = modulesCourse2.size();
                long completedModules = enrollment2.getProgressRecords().stream().filter(p -> p.getCourseContent() != null)
                        .map(p -> p.getCourseContent().getId()).distinct().count();
                double overallProgress = ((double) completedModules / totalModules) * 100;
                if (overallProgress > 100) {
                    overallProgress = 100;
                }
                enrollment2.setCurrentProgress(overallProgress);
                enrollmentRepository.save(enrollment2);
                log.info("Enrollment2 currentProgress updated to: " + overallProgress);
            }
        };
    }
}