-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 21, 2025 at 05:50 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ctrl_learn_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `assessments`
--

CREATE TABLE `assessments` (
  `course_id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `due_date` datetime(6) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `instructor_id` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `type` enum('ASSIGNMENT','QUIZ') DEFAULT NULL,
  `section_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `assessments`
--

INSERT INTO `assessments` (`course_id`, `created_at`, `due_date`, `id`, `instructor_id`, `description`, `title`, `type`, `section_id`) VALUES
(1, '2025-05-13 18:18:58.000000', '2025-05-14 18:18:58.000000', 1, 1, 'Midterm exam covering all topics', 'Midterm Quiz', 'QUIZ', 0),
(2, '2025-05-13 18:18:58.000000', '2025-05-16 18:18:58.000000', 2, 2, 'Group project for the final grade', 'Final Project', 'ASSIGNMENT', 0),
(1, '2025-05-13 18:18:58.000000', '2025-05-20 18:18:58.000000', 3, 1, 'A quiz with no quiz details', 'Practice Quiz', 'QUIZ', 0),
(1, '2025-05-13 18:18:58.000000', '2025-05-20 18:18:58.000000', 4, 1, 'A quiz ', 'Practice Quiz', 'QUIZ', 0),
(2, '2025-05-13 18:18:58.000000', '2025-05-16 18:18:58.000000', 5, 2, 'solo project for the final grade', 'mini Project', 'ASSIGNMENT', 0),
(1, '2025-05-13 18:18:58.000000', '2025-05-23 18:18:58.000000', 6, 1, 'Write a detailed research paper on microservices architecture.', 'Research Paper', 'ASSIGNMENT', 0),
(1, '2025-05-13 18:18:58.000000', '2025-05-18 18:18:58.000000', 7, 1, 'A case study for testing grade creation', 'New Case Study', 'ASSIGNMENT', 0),
(7, '2025-05-15 00:58:30.000000', '2025-06-01 23:59:59.000000', 8, 1, 'Download the Doctor PDF, complete every fields, then upload your filled-out form.', 'Doctor PDF Installation', 'ASSIGNMENT', 1),
(7, '2025-05-15 00:58:30.000000', '2025-06-05 23:59:59.000000', 9, 1, 'Write up your lab report in PDF and submit via this endpoint.', 'Lab Report Submission', 'ASSIGNMENT', 1),
(7, '2025-05-15 19:49:12.000000', '2025-05-22 23:59:59.000000', 10, 1, 'Midterm exam covering all topics', 'Midterm Quiz', 'QUIZ', 1),
(7, '2025-05-15 19:49:12.000000', '2025-06-10 23:59:59.000000', 11, 1, 'Final exam including all course material', 'Final Quiz', 'QUIZ', 1);

-- --------------------------------------------------------

--
-- Table structure for table `assessment_grades`
--

CREATE TABLE `assessment_grades` (
  `auto_graded_score` double DEFAULT NULL,
  `final_score` double DEFAULT NULL,
  `fully_graded` bit(1) NOT NULL,
  `assessment_id` bigint(20) NOT NULL,
  `assignment_submission_id` bigint(20) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `quiz_submission_id` bigint(20) DEFAULT NULL,
  `student_id` bigint(20) NOT NULL,
  `grading_comments` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `assessment_grades`
--

INSERT INTO `assessment_grades` (`auto_graded_score`, `final_score`, `fully_graded`, `assessment_id`, `assignment_submission_id`, `id`, `quiz_submission_id`, `student_id`, `grading_comments`, `created_at`) VALUES
(9.5, 10, b'1', 1, NULL, 1, 1, 1, 'Excellent work!', '2025-05-14 15:45:14'),
(18, 19.5, b'1', 2, 1, 2, NULL, 1, 'Good job, minor issues', '2025-05-14 15:45:14'),
(17, 18, b'1', 2, 2, 3, NULL, 2, 'Decent effort', '2025-05-14 15:45:14'),
(0, 0, b'0', 6, NULL, 4, NULL, 1, 'Missing submission', '2025-05-14 15:45:14'),
(0, 0, b'0', 5, NULL, 5, NULL, 2, 'Not submitted', '2025-05-14 15:45:14'),
(10, 10, b'1', 11, NULL, 8, 20, 1, NULL, '2025-05-20 19:42:46');

-- --------------------------------------------------------

--
-- Table structure for table `assignment_details`
--

CREATE TABLE `assignment_details` (
  `published` bit(1) NOT NULL,
  `total_score` double NOT NULL,
  `assessment_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `notes` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `assignment_details`
--

INSERT INTO `assignment_details` (`published`, `total_score`, `assessment_id`, `id`, `notes`) VALUES
(b'0', 100, 2, 1, 'This is the final assignment for the course.'),
(b'0', 50, 6, 2, 'This is a research paper assignment with no submissions yet.'),
(b'0', 100, 7, 3, 'Analyze a real-world software system.'),
(b'1', 100, 8, 4, '1) Download the Doctor PDF. 2) Fill in all answers. 3) Upload here.'),
(b'1', 100, 9, 5, 'Your lab report must include: Introduction, Methods, Results, Discussion.');

-- --------------------------------------------------------

--
-- Table structure for table `assignment_submissions`
--

CREATE TABLE `assignment_submissions` (
  `assignment_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `student_id` bigint(20) NOT NULL,
  `submitted_at` datetime(6) NOT NULL,
  `feedback` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `assignment_submissions`
--

INSERT INTO `assignment_submissions` (`assignment_id`, `id`, `student_id`, `submitted_at`, `feedback`) VALUES
(1, 1, 1, '2025-05-13 18:18:58.000000', ''),
(1, 2, 2, '2025-05-13 18:18:58.000000', 'Well done, but review DB normalization.'),
(3, 3, 2, '2025-05-13 18:18:58.000000', 'Detailed analysis. Great effort.'),
(4, 15, 1, '2025-05-15 17:59:19.000000', '');

-- --------------------------------------------------------

--
-- Table structure for table `courses`
--

CREATE TABLE `courses` (
  `credits` int(11) NOT NULL,
  `id` bigint(20) NOT NULL,
  `code` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `courses`
--

INSERT INTO `courses` (`credits`, `id`, `code`, `description`, `name`) VALUES
(3, 1, 'SE101', 'Introduction to Software Engineering', 'Software Engineering'),
(4, 2, 'DB202', 'Advanced Database Concepts', 'Database Systems'),
(3, 3, 'CS301', 'Design and analysis of algorithms', 'Design and analysis of algorithms'),
(4, 4, 'CS302', 'Principles of operating systems', 'Principles of operating systems'),
(3, 5, 'CS303', 'Introduction to computer networking', 'Introduction to computer networking'),
(3, 6, 'PROG101', 'Learn variables, loops and functions', 'Intro to Programming'),
(4, 7, 'PROG102', 'Classes & objects in Java', 'Object-Oriented Programming'),
(3, 8, 'PROG201', 'Arrays, lists, trees, graphs', 'Data Structures'),
(4, 9, 'PROG202', 'Sorting, searching, complexity analysis', 'Algorithms');

-- --------------------------------------------------------

--
-- Table structure for table `course_contents`
--

CREATE TABLE `course_contents` (
  `course_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `instructor_id` bigint(20) NOT NULL,
  `section_id` bigint(20) DEFAULT NULL,
  `upload_date` datetime(6) NOT NULL,
  `title` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `course_contents`
--

INSERT INTO `course_contents` (`course_id`, `id`, `instructor_id`, `section_id`, `upload_date`, `title`) VALUES
(1, 1, 1, 1, '2025-05-13 18:18:58.000000', 'Lecture 1: Introduction to Software Engineering'),
(1, 2, 1, 1, '2025-05-13 18:18:58.000000', 'Lecture 2: Agile Methodologies'),
(2, 3, 2, 2, '2025-05-13 18:18:58.000000', 'Lecture 1: Relational Databases'),
(7, 4, 1, 7, '2025-05-13 22:26:34.000000', 'Lecture 1: Introduction to Objects & Classes'),
(7, 5, 1, 7, '2025-05-13 22:26:34.000000', 'Lecture 2: Methods & Constructors'),
(7, 6, 1, 7, '2025-05-13 22:26:34.000000', 'Lecture 3: Inheritance'),
(7, 7, 1, 7, '2025-05-13 22:26:34.000000', 'Lecture 4: Interfaces & Polymorphism'),
(7, 8, 1, 7, '2025-05-13 22:26:34.000000', 'Lecture 5: Exception Handling');

-- --------------------------------------------------------

--
-- Table structure for table `course_instructors`
--

CREATE TABLE `course_instructors` (
  `course_id` bigint(20) NOT NULL,
  `section_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `instructor_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `course_instructors`
--

INSERT INTO `course_instructors` (`course_id`, `section_id`, `id`, `instructor_id`) VALUES
(1, 1, 4, 1),
(2, 2, 3, 1),
(3, 3, 2, 1),
(7, 7, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `course_instructors_seq`
--

CREATE TABLE `course_instructors_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `course_instructors_seq`
--

INSERT INTO `course_instructors_seq` (`next_val`) VALUES
(1);

-- --------------------------------------------------------

--
-- Table structure for table `enrollments`
--

CREATE TABLE `enrollments` (
  `completed` bit(1) NOT NULL,
  `current_progress` double NOT NULL,
  `course_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `section_id` bigint(20) NOT NULL,
  `student_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `enrollments`
--

INSERT INTO `enrollments` (`completed`, `current_progress`, `course_id`, `id`, `section_id`, `student_id`) VALUES
(b'0', 100, 1, 1, 1, 1),
(b'0', 100, 2, 2, 2, 2),
(b'0', 0, 7, 6, 7, 1),
(b'0', 0, 8, 9, 8, 1),
(b'0', 0, 1, 10, 1, 3),
(b'0', 0, 2, 11, 1, 3),
(b'0', 0, 3, 12, 1, 3),
(b'0', 0, 7, 13, 1, 3),
(b'0', 0, 1, 14, 1, 4),
(b'0', 0, 2, 15, 1, 4),
(b'0', 0, 3, 16, 1, 4),
(b'0', 0, 7, 17, 1, 4),
(b'0', 0, 1, 18, 1, 5),
(b'0', 0, 2, 19, 1, 5),
(b'0', 0, 3, 20, 1, 5),
(b'0', 0, 7, 21, 1, 5),
(b'0', 0, 1, 22, 1, 6),
(b'0', 0, 2, 23, 1, 6),
(b'0', 0, 3, 24, 1, 6),
(b'0', 0, 7, 25, 1, 6),
(b'0', 0, 1, 26, 1, 7),
(b'0', 0, 2, 27, 1, 7),
(b'0', 0, 3, 28, 1, 7),
(b'0', 0, 7, 29, 1, 7),
(b'0', 0, 1, 30, 1, 8),
(b'0', 0, 2, 31, 1, 8),
(b'0', 0, 3, 32, 1, 8),
(b'0', 0, 7, 33, 1, 8),
(b'0', 0, 1, 34, 1, 9),
(b'0', 0, 2, 35, 1, 9),
(b'0', 0, 3, 36, 1, 9),
(b'0', 0, 7, 37, 1, 9),
(b'0', 0, 1, 38, 1, 10),
(b'0', 0, 2, 39, 1, 10),
(b'0', 0, 3, 40, 1, 10),
(b'0', 0, 7, 41, 1, 10),
(b'0', 0, 1, 42, 1, 11),
(b'0', 0, 2, 43, 1, 11),
(b'0', 0, 3, 44, 1, 11),
(b'0', 0, 7, 45, 1, 11),
(b'0', 0, 1, 46, 1, 12),
(b'0', 0, 2, 47, 1, 12),
(b'0', 0, 3, 48, 1, 12),
(b'0', 0, 7, 49, 1, 12),
(b'0', 0, 1, 50, 1, 13),
(b'0', 0, 2, 51, 1, 13),
(b'0', 0, 3, 52, 1, 13),
(b'0', 0, 7, 53, 1, 13),
(b'0', 0, 1, 54, 1, 14),
(b'0', 0, 2, 55, 1, 14),
(b'0', 0, 3, 56, 1, 14),
(b'0', 0, 7, 57, 1, 14),
(b'0', 0, 1, 58, 1, 15),
(b'0', 0, 2, 59, 1, 15),
(b'0', 0, 3, 60, 1, 15),
(b'0', 0, 7, 61, 1, 15),
(b'0', 0, 1, 62, 1, 16),
(b'0', 0, 2, 63, 1, 16),
(b'0', 0, 3, 64, 1, 16),
(b'0', 0, 7, 65, 1, 16),
(b'0', 0, 1, 66, 1, 17),
(b'0', 0, 2, 67, 1, 17),
(b'0', 0, 3, 68, 1, 17),
(b'0', 0, 7, 69, 1, 17),
(b'0', 0, 4, 73, 1, 18),
(b'0', 0, 5, 74, 1, 18),
(b'0', 0, 6, 75, 1, 18),
(b'0', 0, 8, 76, 1, 18),
(b'0', 0, 9, 77, 1, 18),
(b'0', 0, 4, 78, 1, 19),
(b'0', 0, 5, 79, 1, 19),
(b'0', 0, 6, 80, 1, 19),
(b'0', 0, 8, 81, 1, 19),
(b'0', 0, 9, 82, 1, 19),
(b'0', 0, 4, 83, 1, 20),
(b'0', 0, 5, 84, 1, 20),
(b'0', 0, 6, 85, 1, 20),
(b'0', 0, 8, 86, 1, 20),
(b'0', 0, 9, 87, 1, 20),
(b'0', 0, 4, 88, 1, 21),
(b'0', 0, 5, 89, 1, 21),
(b'0', 0, 6, 90, 1, 21),
(b'0', 0, 8, 91, 1, 21),
(b'0', 0, 9, 92, 1, 21),
(b'0', 0, 4, 93, 1, 22),
(b'0', 0, 5, 94, 1, 22),
(b'0', 0, 6, 95, 1, 22),
(b'0', 0, 8, 96, 1, 22),
(b'0', 0, 9, 97, 1, 22),
(b'0', 0, 4, 98, 1, 23),
(b'0', 0, 5, 99, 1, 23),
(b'0', 0, 6, 100, 1, 23),
(b'0', 0, 8, 101, 1, 23),
(b'0', 0, 9, 102, 1, 23),
(b'0', 0, 4, 103, 1, 24),
(b'0', 0, 5, 104, 1, 24),
(b'0', 0, 6, 105, 1, 24),
(b'0', 0, 8, 106, 1, 24),
(b'0', 0, 9, 107, 1, 24),
(b'0', 0, 4, 108, 1, 25),
(b'0', 0, 5, 109, 1, 25),
(b'0', 0, 6, 110, 1, 25),
(b'0', 0, 8, 111, 1, 25),
(b'0', 0, 9, 112, 1, 25),
(b'0', 0, 4, 113, 1, 26),
(b'0', 0, 5, 114, 1, 26),
(b'0', 0, 6, 115, 1, 26),
(b'0', 0, 8, 116, 1, 26),
(b'0', 0, 9, 117, 1, 26),
(b'0', 0, 4, 118, 1, 27),
(b'0', 0, 5, 119, 1, 27),
(b'0', 0, 6, 120, 1, 27),
(b'0', 0, 8, 121, 1, 27),
(b'0', 0, 9, 122, 1, 27),
(b'0', 0, 4, 123, 1, 28),
(b'0', 0, 5, 124, 1, 28),
(b'0', 0, 6, 125, 1, 28),
(b'0', 0, 8, 126, 1, 28),
(b'0', 0, 9, 127, 1, 28),
(b'0', 0, 4, 128, 1, 29),
(b'0', 0, 5, 129, 1, 29),
(b'0', 0, 6, 130, 1, 29),
(b'0', 0, 8, 131, 1, 29),
(b'0', 0, 9, 132, 1, 29),
(b'0', 0, 4, 133, 1, 30),
(b'0', 0, 5, 134, 1, 30),
(b'0', 0, 6, 135, 1, 30),
(b'0', 0, 8, 136, 1, 30),
(b'0', 0, 9, 137, 1, 30),
(b'0', 0, 4, 138, 1, 31),
(b'0', 0, 5, 139, 1, 31),
(b'0', 0, 6, 140, 1, 31),
(b'0', 0, 8, 141, 1, 31),
(b'0', 0, 9, 142, 1, 31),
(b'0', 0, 4, 143, 1, 32),
(b'0', 0, 5, 144, 1, 32),
(b'0', 0, 6, 145, 1, 32),
(b'0', 0, 8, 146, 1, 32),
(b'0', 0, 9, 147, 1, 32),
(b'0', 0, 3, 148, 3, 1);

-- --------------------------------------------------------

--
-- Table structure for table `file_metadata`
--

CREATE TABLE `file_metadata` (
  `assignment_details_id` bigint(20) DEFAULT NULL,
  `course_content_id` bigint(20) DEFAULT NULL,
  `file_size` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `submission_id` bigint(20) DEFAULT NULL,
  `file_extension` varchar(255) NOT NULL,
  `original_filename` varchar(255) NOT NULL,
  `stored_filename` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `file_metadata`
--

INSERT INTO `file_metadata` (`assignment_details_id`, `course_content_id`, `file_size`, `id`, `submission_id`, `file_extension`, `original_filename`, `stored_filename`) VALUES
(NULL, 4, 641, 1, NULL, '.pdf', 'lecture1.pdf', '431f6450-61a9-42f3-b6e8-552dda127f34.pdf'),
(NULL, 5, 641, 11, NULL, '.pdf', 'lecture2.pdf', 'a07fadd8-35bc-402d-8b97-71a8f9359e19.pdf'),
(NULL, 6, 641, 12, NULL, '.pdf', 'lecture3.pdf', 'b5aa9c2f-f5b9-4902-82c9-d593d7e81a56.pdf'),
(NULL, 7, 641, 13, NULL, '.pdf', 'lecture4.pdf', '079e935f-cb0f-4b2c-89e1-3a79673ede25.pdf'),
(NULL, 8, 641, 14, NULL, '.pdf', 'lecture5.pdf', '9cabb9cf-962f-4db1-99b4-0d771fc335f8.pdf'),
(5, NULL, 13264, 18, NULL, '.pdf', 'lab_report.pdf', '5e30c5db-fe57-40ce-866b-f5c3cecaff2a.pdf'),
(4, NULL, 13264, 19, NULL, '.pdf', 'doctor_filled.pdf', 'c9b4c52b-eb70-48b3-a50e-9d0d302d597e.pdf'),
(NULL, NULL, 328256, 28, 15, '.pdf', 'cv.pdf', '031ac6ba-7ee6-4bd1-a032-dc8849197d11.pdf');

-- --------------------------------------------------------

--
-- Table structure for table `instructors`
--

CREATE TABLE `instructors` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `department` varchar(255) NOT NULL,
  `faculty` varchar(255) NOT NULL,
  `instructor_id` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `instructors`
--

INSERT INTO `instructors` (`id`, `user_id`, `department`, `faculty`, `instructor_id`) VALUES
(1, 2, 'Computer Science', 'Engineering Faculty', 'INSTR001'),
(2, 3, 'Information Technology', 'Engineering Faculty', 'INSTR002'),
(3, 4, 'Statistics', 'Mathematics Faculty', 'INSTR003'),
(4, 5, 'Management', 'Business Faculty', 'INSTR004');

-- --------------------------------------------------------

--
-- Table structure for table `instructor_profiles`
--

CREATE TABLE `instructor_profiles` (
  `id` bigint(20) NOT NULL,
  `instructor_id` bigint(20) NOT NULL,
  `profile_id` bigint(20) NOT NULL,
  `academic_rank` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `faculty` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `instructor_profiles`
--

INSERT INTO `instructor_profiles` (`id`, `instructor_id`, `profile_id`, `academic_rank`, `department`, `faculty`) VALUES
(1, 1, 1, 'Associate Professor', 'Computer Science', 'Engineering Faculty'),
(2, 2, 2, 'Professor', 'Information Technology', 'Engineering Faculty');

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

CREATE TABLE `notifications` (
  `retry_count` int(11) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `recipient_id` bigint(20) NOT NULL,
  `sent_at` datetime(6) DEFAULT NULL,
  `error_message` varchar(255) DEFAULT NULL,
  `message` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `channel` enum('EMAIL','SMS') NOT NULL,
  `status` enum('FAILED','PENDING','SENT') NOT NULL,
  `type` enum('ASSIGNMENT_DUE','ASSIGNMENT_SUBMISSION','COURSE_COMPLETION','COURSE_DROPPED','COURSE_UPDATE','DEADLINE_REMINDER','ENROLLMENT_CONFIRMATION','GRADE_RELEASE','INSTRUCTOR_MESSAGE','NEW_ANNOUNCEMENT','NEW_ASSIGNMENT','NEW_RESOURCE_ADDED','UPCOMING_EXAM') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `notifications`
--

INSERT INTO `notifications` (`retry_count`, `created_at`, `id`, `recipient_id`, `sent_at`, `error_message`, `message`, `title`, `channel`, `status`, `type`) VALUES
(NULL, '2025-05-20 18:13:29.000000', 1, 6, NULL, NULL, 'Your Mythology essay is due this Friday.', 'New Assignment Posted', 'EMAIL', 'PENDING', 'NEW_ASSIGNMENT'),
(NULL, '2025-05-19 20:13:29.000000', 2, 6, NULL, NULL, 'Chapter 4 quiz tomorrow at 9 AM.', 'Quiz Reminder', 'EMAIL', 'PENDING', 'DEADLINE_REMINDER'),
(NULL, '2025-05-17 20:13:29.000000', 3, 6, NULL, NULL, 'Office hours moved to Thursday, Room 203.', 'Course Announcement', 'EMAIL', 'PENDING', 'NEW_ANNOUNCEMENT'),
(NULL, '2025-05-15 20:13:29.000000', 4, 6, NULL, NULL, 'Your Grade for Assignment 2 is now available.', 'Grades Released', 'EMAIL', 'PENDING', 'GRADE_RELEASE'),
(0, '2025-05-20 22:44:57.000000', 5, 1, NULL, NULL, 'This is a live-push test.', 'Test alert', 'SMS', 'PENDING', 'NEW_ANNOUNCEMENT'),
(0, '2025-05-20 22:45:34.000000', 6, 1, NULL, NULL, 'This is a live-push test.', 'TestT alert', 'SMS', 'PENDING', 'NEW_ANNOUNCEMENT'),
(0, '2025-05-20 22:46:40.000000', 7, 1, NULL, NULL, 'This is a live-push test.', 'TestTY alert', 'SMS', 'PENDING', 'NEW_ANNOUNCEMENT'),
(0, '2025-05-20 22:54:38.000000', 8, 1, NULL, NULL, 'This is a live-push test.', 'TestTYu alert', 'SMS', 'PENDING', 'NEW_ANNOUNCEMENT'),
(0, '2025-05-20 23:13:23.000000', 9, 6, NULL, NULL, 'This is a live-push test.', 'TestTYu alert', 'SMS', 'PENDING', 'NEW_ANNOUNCEMENT'),
(0, '2025-05-20 23:13:34.000000', 10, 1, NULL, NULL, 'This is a live-push test.', 'TestTYu alert', 'SMS', 'PENDING', 'NEW_ANNOUNCEMENT'),
(0, '2025-05-20 23:13:54.000000', 11, 1, NULL, NULL, 'This is a live-push test.', 'hhhestTYu alert', 'SMS', 'PENDING', 'NEW_ANNOUNCEMENT'),
(0, '2025-05-20 23:14:03.000000', 12, 6, NULL, NULL, 'This is a live-push test.', 'hhhestTYu alert', 'SMS', 'PENDING', 'NEW_ANNOUNCEMENT'),
(0, '2025-05-20 23:20:11.000000', 13, 6, NULL, NULL, 'This is a live-push test.', 'hhhestTYu alert', 'SMS', 'PENDING', 'NEW_ANNOUNCEMENT'),
(0, '2025-05-20 23:21:41.000000', 14, 6, NULL, NULL, 'This is a live-push test.', 'lhhhestTYu alert', 'SMS', 'PENDING', 'NEW_ANNOUNCEMENT'),
(0, '2025-05-20 23:22:16.000000', 15, 6, NULL, NULL, 'This is a live-push test.', 'lhhhestTYu alert', 'SMS', 'PENDING', 'NEW_ANNOUNCEMENT'),
(0, '2025-05-20 23:22:59.000000', 16, 6, NULL, NULL, 'This is a live-push test.', 'lhhhestTYu alert', 'SMS', 'PENDING', 'NEW_ANNOUNCEMENT'),
(0, '2025-05-20 23:23:17.000000', 17, 6, NULL, NULL, 'This is a live-push test.', 'lhhhestTYu alert', 'SMS', 'PENDING', 'NEW_ANNOUNCEMENT'),
(0, '2025-05-21 17:14:47.000000', 18, 2, NULL, NULL, 'This is a live-push test.', 'test instructor', 'SMS', 'PENDING', 'NEW_ANNOUNCEMENT'),
(0, '2025-05-21 17:15:52.000000', 19, 2, NULL, NULL, 'This is a live-push test.', 'test instructor', 'SMS', 'PENDING', 'NEW_ANNOUNCEMENT'),
(0, '2025-05-21 17:16:00.000000', 20, 2, NULL, NULL, 'This is a live-push test.', 'test instructor', 'SMS', 'PENDING', 'NEW_ANNOUNCEMENT');

-- --------------------------------------------------------

--
-- Table structure for table `notification_logs`
--

CREATE TABLE `notification_logs` (
  `id` bigint(20) NOT NULL,
  `notification_id` bigint(20) NOT NULL,
  `timestamp` datetime(6) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `error_message` varchar(255) DEFAULT NULL,
  `status` enum('FAILED','PENDING','SENT') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `notification_logs`
--

INSERT INTO `notification_logs` (`id`, `notification_id`, `timestamp`, `user_id`, `error_message`, `status`) VALUES
(1, 1, '2025-05-20 19:13:29.000000', 6, NULL, 'SENT'),
(2, 2, '2025-05-19 21:13:29.000000', 6, NULL, 'SENT'),
(3, 3, '2025-05-17 21:13:29.000000', 6, NULL, 'SENT'),
(4, 4, '2025-05-15 21:13:29.000000', 6, NULL, 'SENT');

-- --------------------------------------------------------

--
-- Table structure for table `profiles`
--

CREATE TABLE `profiles` (
  `birth_date` date NOT NULL,
  `created_at` date NOT NULL,
  `updated_at` date DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `bio` varchar(255) DEFAULT NULL,
  `country` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `nationality` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) NOT NULL,
  `profile_picture_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `profiles`
--

INSERT INTO `profiles` (`birth_date`, `created_at`, `updated_at`, `id`, `user_id`, `bio`, `country`, `full_name`, `gender`, `nationality`, `phone_number`, `profile_picture_url`) VALUES
('1985-05-15', '2025-05-13', '2025-05-13', 1, 2, NULL, 'USA', 'Jane Doe', NULL, NULL, '+11234567890', NULL),
('1980-03-10', '2025-05-13', '2025-05-13', 2, 3, NULL, 'USA', 'John Doe', NULL, NULL, '+11234567891', NULL),
('2000-01-01', '2025-05-13', '2025-05-13', 3, 6, NULL, 'USA', 'Alice Smith', NULL, NULL, '+11234567892', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `progress_tracking`
--

CREATE TABLE `progress_tracking` (
  `completed_tasks` double NOT NULL,
  `progress` double NOT NULL,
  `total_tasks` double NOT NULL,
  `course_content_id` bigint(20) DEFAULT NULL,
  `enrollment_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `updated_at` datetime(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `progress_tracking`
--

INSERT INTO `progress_tracking` (`completed_tasks`, `progress`, `total_tasks`, `course_content_id`, `enrollment_id`, `id`, `updated_at`) VALUES
(0, 1, 1, 1, 1, 1, '2025-05-13 18:18:58.000000'),
(0, 1, 1, 2, 1, 2, '2025-05-13 18:18:58.000000'),
(0, 1, 1, 3, 2, 3, '2025-05-13 18:18:58.000000');

-- --------------------------------------------------------

--
-- Table structure for table `quiz_details`
--

CREATE TABLE `quiz_details` (
  `published` bit(1) NOT NULL,
  `time_limit_minutes` int(11) DEFAULT NULL,
  `total_score` double NOT NULL,
  `assessment_id` bigint(20) NOT NULL,
  `closing_time` datetime(6) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `open_time` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `quiz_details`
--

INSERT INTO `quiz_details` (`published`, `time_limit_minutes`, `total_score`, `assessment_id`, `closing_time`, `id`, `open_time`) VALUES
(b'1', 30, 20, 1, '2025-05-16 18:18:58.000000', 1, '2025-05-12 18:18:58.000000'),
(b'1', 30, 20, 10, '2025-05-22 23:59:59.000000', 2, '2025-05-20 09:00:00.000000'),
(b'1', 40, 30, 11, '2025-06-10 23:59:59.000000', 3, '2025-06-08 09:00:00.000000');

-- --------------------------------------------------------

--
-- Table structure for table `quiz_options`
--

CREATE TABLE `quiz_options` (
  `is_correct` bit(1) NOT NULL,
  `id` bigint(20) NOT NULL,
  `quiz_question_id` bigint(20) NOT NULL,
  `option_text` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `quiz_options`
--

INSERT INTO `quiz_options` (`is_correct`, `id`, `quiz_question_id`, `option_text`) VALUES
(b'1', 1, 1, 'Paris'),
(b'0', 2, 1, 'London'),
(b'0', 3, 1, 'Rome'),
(b'0', 4, 1, 'Berlin'),
(b'1', 5, 2, 'To store and organize data efficiently'),
(b'0', 6, 2, 'To create animations'),
(b'0', 7, 2, 'To send emails'),
(b'0', 8, 2, 'To design web pages'),
(b'1', 9, 3, 'SELECT'),
(b'0', 10, 3, 'INSERT'),
(b'0', 11, 3, 'UPDATE'),
(b'0', 12, 3, 'DELETE'),
(b'1', 13, 5, 'A JavaScript library for building UIs'),
(b'0', 14, 5, 'A CSS framework'),
(b'0', 15, 5, 'A database'),
(b'0', 16, 5, 'An operating system'),
(b'0', 17, 6, 'True'),
(b'1', 18, 6, 'False'),
(b'1', 19, 7, 'Java'),
(b'0', 20, 7, 'HTML'),
(b'0', 21, 7, 'CSS'),
(b'0', 22, 7, 'React'),
(b'1', 23, 8, '<h1>'),
(b'0', 24, 8, '<h6>'),
(b'0', 25, 8, '<title>'),
(b'0', 26, 8, '<header>');

-- --------------------------------------------------------

--
-- Table structure for table `quiz_questions`
--

CREATE TABLE `quiz_questions` (
  `is_auto_graded` bit(1) NOT NULL,
  `question_number` int(11) NOT NULL,
  `score` double NOT NULL,
  `id` bigint(20) NOT NULL,
  `quiz_id` bigint(20) NOT NULL,
  `question_text` varchar(255) DEFAULT NULL,
  `question_type` enum('ESSAY','FILL_IN_THE_BLANK','MULTIPLE_CHOICE','SHORT_ANSWER','TRUE_FALSE') DEFAULT NULL,
  `correct_option_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `quiz_questions`
--

INSERT INTO `quiz_questions` (`is_auto_graded`, `question_number`, `score`, `id`, `quiz_id`, `question_text`, `question_type`, `correct_option_id`) VALUES
(b'1', 1, 10, 1, 1, 'What is the capital of France?', 'MULTIPLE_CHOICE', 1),
(b'1', 1, 10, 2, 2, 'What is the main purpose of using databases in web applications?', 'MULTIPLE_CHOICE', 5),
(b'1', 1, 10, 3, 3, 'Which SQL statement is used to retrieve data from a database?', 'MULTIPLE_CHOICE', 9),
(b'1', 2, 10, 5, 2, 'What is React?', 'MULTIPLE_CHOICE', 13),
(b'1', 3, 5, 6, 2, 'JavaScript is statically typed.', 'TRUE_FALSE', 18),
(b'1', 4, 10, 7, 2, 'Which of the following is a backend language?', 'MULTIPLE_CHOICE', 19),
(b'1', 5, 10, 8, 2, 'Which tag is used for the largest heading in HTML?', 'MULTIPLE_CHOICE', 23);

-- --------------------------------------------------------

--
-- Table structure for table `quiz_submissions`
--

CREATE TABLE `quiz_submissions` (
  `duration_minutes` int(11) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `quiz_id` bigint(20) NOT NULL,
  `started_at` datetime(6) DEFAULT NULL,
  `student_id` bigint(20) NOT NULL,
  `submitted_at` datetime(6) DEFAULT NULL,
  `auto_graded_answers` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`auto_graded_answers`)),
  `manually_graded_answers` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`manually_graded_answers`)),
  `submission_status` enum('GRADED','NOT_SUBMITTED','SUBMITTED') DEFAULT NULL,
  `auto_graded_score` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `quiz_submissions`
--

INSERT INTO `quiz_submissions` (`duration_minutes`, `id`, `quiz_id`, `started_at`, `student_id`, `submitted_at`, `auto_graded_answers`, `manually_graded_answers`, `submission_status`, `auto_graded_score`) VALUES
(25, 1, 1, '2025-05-13 17:53:58.000000', 1, '2025-05-13 18:18:58.000000', '{\"1\": \"Paris\"}', '{\"1\": null}', 'SUBMITTED', NULL),
(1, 20, 3, '2025-05-20 16:42:41.000000', 1, '2025-05-20 16:42:46.000000', '{\"3\":9}', NULL, 'SUBMITTED', 10);

-- --------------------------------------------------------

--
-- Table structure for table `refresh_token`
--

CREATE TABLE `refresh_token` (
  `expiry_date` datetime(6) NOT NULL,
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `token` varchar(36) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `refresh_token`
--

INSERT INTO `refresh_token` (`expiry_date`, `id`, `user_id`, `token`) VALUES
('2025-05-28 14:36:27.000000', 1, 6, '75a13b5e-6297-4b7c-b351-f3abaa5a54a5'),
('2025-05-28 14:20:46.000000', 2, 2, 'f816abb7-a10a-4eab-875e-0ae9c7e723be'),
('2025-05-21 21:27:27.000000', 3, 1, 'fe4d39cf-8880-44d3-afa1-9cd3fa67b888'),
('2025-05-27 19:29:43.000000', 4, 38, '32ec66da-f817-4cbc-8913-9db4f424f69e'),
('2025-05-27 19:39:12.000000', 5, 39, '91396780-371b-4385-b4ee-b895e00342b0'),
('2025-05-28 14:14:32.000000', 6, 40, '609a6ae3-cff0-4e18-96be-06e61468f2fc');

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`id`, `name`) VALUES
(1, 'ADMIN'),
(2, 'INSTRUCTOR'),
(3, 'STUDENT');

-- --------------------------------------------------------

--
-- Table structure for table `schedule_entries`
--

CREATE TABLE `schedule_entries` (
  `id` bigint(20) NOT NULL,
  `day_of_week` varchar(9) DEFAULT NULL,
  `end_time` time(6) NOT NULL,
  `start_time` time(6) NOT NULL,
  `section_id` bigint(20) NOT NULL,
  `course_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `schedule_entries`
--

INSERT INTO `schedule_entries` (`id`, `day_of_week`, `end_time`, `start_time`, `section_id`, `course_id`) VALUES
(1, 'MONDAY', '11:00:00.000000', '10:00:00.000000', 1, 1),
(2, 'TUESDAY', '11:00:00.000000', '10:00:00.000000', 1, 1),
(3, 'FRIDAY', '11:00:00.000000', '10:00:00.000000', 1, 1),
(4, 'MONDAY', '14:30:00.000000', '13:00:00.000000', 2, 2),
(5, 'FRIDAY', '14:30:00.000000', '13:00:00.000000', 2, 2),
(6, 'WEDNESDAY', '12:15:00.000000', '11:15:00.000000', 3, 3),
(7, 'THURSDAY', '12:15:00.000000', '11:15:00.000000', 3, 3),
(8, 'MONDAY', '16:00:00.000000', '14:45:00.000000', 4, 4),
(9, 'TUESDAY', '16:00:00.000000', '14:45:00.000000', 4, 4),
(10, 'FRIDAY', '17:30:00.000000', '16:00:00.000000', 5, 5),
(11, 'SATURDAY', '17:30:00.000000', '16:00:00.000000', 5, 5),
(12, 'MONDAY', '21:00:00.000000', '18:00:00.000000', 6, 6),
(13, 'WEDNESDAY', '15:00:00.000000', '13:00:00.000000', 7, 7),
(14, 'SATURDAY', '10:00:00.000000', '08:00:00.000000', 8, 8),
(15, 'FRIDAY', '17:00:00.000000', '14:00:00.000000', 9, 9);

-- --------------------------------------------------------

--
-- Table structure for table `sections`
--

CREATE TABLE `sections` (
  `number` int(11) NOT NULL,
  `course_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `semester_id` bigint(20) NOT NULL,
  `schedule` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sections`
--

INSERT INTO `sections` (`number`, `course_id`, `id`, `semester_id`, `schedule`) VALUES
(1, 1, 1, 1, 'MON,on/WED,ed/FRI,ri 10:00|11:00|10:00|11:00|11:00'),
(1, 2, 2, 1, 'Tue/Thu 13:00|14:30|13:00|14:30|11:00'),
(1, 3, 3, 1, 'MON,on,WED,ed|11:15|12:15|Mon,Wed|11:15|12:15|11:00'),
(1, 4, 4, 1, 'Tue,Thu|14:45|16:00|Tue,Thu|14:45|16:00|11:00'),
(1, 5, 5, 1, 'MON,on,WED,ed|16:00|17:30|Mon,Wed|16:00|17:30|11:00'),
(1, 6, 6, 1, 'Tue|18:00|21:00|Tue|18:00|21:00|11:00'),
(1, 7, 7, 1, 'WED,ed|13:00|15:00|Wed|13:00|15:00|11:00'),
(1, 8, 8, 1, 'Thu|08:00|10:00|Thu|08:00|10:00|11:00'),
(1, 9, 9, 1, 'FRI,ri|14:00|17:00|Fri|14:00|17:00|11:00');

-- --------------------------------------------------------

--
-- Table structure for table `semesters`
--

CREATE TABLE `semesters` (
  `year` int(11) NOT NULL,
  `id` bigint(20) NOT NULL,
  `term` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `semesters`
--

INSERT INTO `semesters` (`year`, `id`, `term`) VALUES
(2025, 1, 'Spring');

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `major` varchar(255) DEFAULT NULL,
  `student_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`id`, `user_id`, `major`, `student_id`) VALUES
(1, 6, 'Computer Science', 'STU001'),
(2, 7, 'Information Technology', 'STU002'),
(3, 8, 'Major5', 'STU005'),
(4, 9, 'Major6', 'STU006'),
(5, 10, 'Major7', 'STU007'),
(6, 11, 'Major8', 'STU008'),
(7, 12, 'Major9', 'STU009'),
(8, 13, 'Major10', 'STU010'),
(9, 14, 'Major11', 'STU011'),
(10, 15, 'Major12', 'STU012'),
(11, 16, 'Major13', 'STU013'),
(12, 17, 'Major14', 'STU014'),
(13, 18, 'Major15', 'STU015'),
(14, 19, 'Major16', 'STU016'),
(15, 20, 'Major17', 'STU017'),
(16, 21, 'Major18', 'STU018'),
(17, 22, 'Major19', 'STU019'),
(18, 23, 'Major20', 'STU020'),
(19, 24, 'Major21', 'STU021'),
(20, 25, 'Major22', 'STU022'),
(21, 26, 'Major23', 'STU023'),
(22, 27, 'Major24', 'STU024'),
(23, 28, 'Major25', 'STU025'),
(24, 29, 'Major26', 'STU026'),
(25, 30, 'Major27', 'STU027'),
(26, 31, 'Major28', 'STU028'),
(27, 32, 'Major29', 'STU029'),
(28, 33, 'Major30', 'STU030'),
(29, 34, 'Major31', 'STU031'),
(30, 35, 'Major32', 'STU032'),
(31, 36, 'Major33', 'STU033'),
(32, 37, 'Major34', 'STU034');

-- --------------------------------------------------------

--
-- Table structure for table `student_profiles`
--

CREATE TABLE `student_profiles` (
  `advisor_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `profile_id` bigint(20) NOT NULL,
  `student_id` bigint(20) NOT NULL,
  `admitted_year` varchar(255) DEFAULT NULL,
  `current_semester` varchar(255) DEFAULT NULL,
  `degree_program` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `major` varchar(255) DEFAULT NULL,
  `minor` varchar(255) DEFAULT NULL,
  `probation_status` varchar(255) DEFAULT NULL,
  `student_level` varchar(255) DEFAULT NULL,
  `tawjihi_stream` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `student_profiles`
--

INSERT INTO `student_profiles` (`advisor_id`, `id`, `profile_id`, `student_id`, `admitted_year`, `current_semester`, `degree_program`, `department`, `major`, `minor`, `probation_status`, `student_level`, `tawjihi_stream`) VALUES
(1, 1, 3, 1, '2024', 'Fall 2024', 'Bachelor', 'Engineering', 'Computer Science', 'Mathematics', 'None', 'Undergraduate', 'Science');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `role_id`, `email`, `first_name`, `last_name`, `password`) VALUES
(1, 1, 'admin@example.com', 'Admin', 'User', '$2a$10$EYBEEJ.aiMgzQ8zKNOP9uuyLHF0YcKF9bjNOGeutf7.SFdpYR74/S'),
(2, 2, 'jane.doe@example.com', 'Jane', 'Doe', '$2a$10$HnEm/znHaYe5kTz8SGwLqeaQtErtrvQdxDuWWAmtdL1u.Q2Vv0At.'),
(3, 2, 'john.doe@example.com', 'John', 'Doe', '$2a$10$XcdRw6qHk5CHoCN2vqUNeOH4v2n3AYRAGFoYfbNNtZSM6CY5SS/9S'),
(4, 2, 'sophia.anderson@example.com', 'Sophia', 'Anderson', '$2a$10$p3YCefTQJxhuyzhVtXWjH.qjuX3qXmVPouv1QXH7mUgPUENdM0NcK'),
(5, 2, 'michael.scott@example.com', 'Michael', 'Scott', '$2a$10$Sc/Yz5YPgeVDc6Z59Wil8uOdztlxl0eUArlnTR8tGoE6GyIvU4hAm'),
(6, 3, 'student1@example.com', 'Alice', 'Smith', '$2a$10$hoepPiWa1e8S/FX3tZ8/juuebOthmXuwiAgp9sr7Gb/cusngH3yne'),
(7, 3, 'student2@example.com', 'Bob', 'Brown', '$2a$10$AeUEpILKFgR2n7hQ65a.JuN4aMnPHKx5zpT1w14pX16JILyphYTE2'),
(8, 3, 'stu005@example.com', 'Student', '005', '$2y$…hash…'),
(9, 3, 'stu006@example.com', 'Student', '006', '$2y$…hash…'),
(10, 3, 'stu007@example.com', 'Student', '007', '$2y$…hash…'),
(11, 3, 'stu008@example.com', 'Student', '008', '$2y$…hash…'),
(12, 3, 'stu009@example.com', 'Student', '009', '$2y$…hash…'),
(13, 3, 'stu010@example.com', 'Student', '010', '$2y$…hash…'),
(14, 3, 'stu011@example.com', 'Student', '011', '$2y$…hash…'),
(15, 3, 'stu012@example.com', 'Student', '012', '$2y$…hash…'),
(16, 3, 'stu013@example.com', 'Student', '013', '$2y$…hash…'),
(17, 3, 'stu014@example.com', 'Student', '014', '$2y$…hash…'),
(18, 3, 'stu015@example.com', 'Student', '015', '$2y$…hash…'),
(19, 3, 'stu016@example.com', 'Student', '016', '$2y$…hash…'),
(20, 3, 'stu017@example.com', 'Student', '017', '$2y$…hash…'),
(21, 3, 'stu018@example.com', 'Student', '018', '$2y$…hash…'),
(22, 3, 'stu019@example.com', 'Student', '019', '$2y$…hash…'),
(23, 3, 'stu020@example.com', 'Student', '020', '$2y$…hash…'),
(24, 3, 'stu021@example.com', 'Student', '021', '$2y$…hash…'),
(25, 3, 'stu022@example.com', 'Student', '022', '$2y$…hash…'),
(26, 3, 'stu023@example.com', 'Student', '023', '$2y$…hash…'),
(27, 3, 'stu024@example.com', 'Student', '024', '$2y$…hash…'),
(28, 3, 'stu025@example.com', 'Student', '025', '$2y$…hash…'),
(29, 3, 'stu026@example.com', 'Student', '026', '$2y$…hash…'),
(30, 3, 'stu027@example.com', 'Student', '027', '$2y$…hash…'),
(31, 3, 'stu028@example.com', 'Student', '028', '$2y$…hash…'),
(32, 3, 'stu029@example.com', 'Student', '029', '$2y$…hash…'),
(33, 3, 'stu030@example.com', 'Student', '030', '$2y$…hash…'),
(34, 3, 'stu031@example.com', 'Student', '031', '$2y$…hash…'),
(35, 3, 'stu032@example.com', 'Student', '032', '$2y$…hash…'),
(36, 3, 'stu033@example.com', 'Student', '033', '$2y$…hash…'),
(37, 3, 'stu034@example.com', 'Student', '034', '$2y$…hash…'),
(38, 2, 'instructor@example.com', 'Meera', 'Barskiwan', '$2a$10$AFwpF0585evYk0YKZcrJPuiIEaMJ77PVviRfJmn5FWrSlaS/BqWii'),
(39, 2, 'instructor1@example.com', 'Meera', 'Barskiwan', '$2a$10$ZeqCVhrZja93yLUxKOoE2e4t0Rr/W97J.qbEqeFg3J2q.FjCPyL.S'),
(40, 2, 'instructor12@example.com', 'Meera', 'Barskiwan', '$2a$10$V/3ykIjiyluio3Xq3rCSW.F4LV32BoRIAS67QYkZiWrnLPZgAmK0O');

-- --------------------------------------------------------

--
-- Table structure for table `user_preferences`
--

CREATE TABLE `user_preferences` (
  `email_enabled` bit(1) NOT NULL,
  `sms_enabled` bit(1) NOT NULL,
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `phone_number` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `assessments`
--
ALTER TABLE `assessments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKa2nh608bmj0k0wjf0rw7oiha5` (`course_id`),
  ADD KEY `FKicyqpvtbc5g0la0kxjbum8vqb` (`instructor_id`);

--
-- Indexes for table `assessment_grades`
--
ALTER TABLE `assessment_grades`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK5vtdmv9scxt5en4nnx2wvjndl` (`assessment_id`,`student_id`),
  ADD UNIQUE KEY `UK5p5voy7ja1u41n3hv2ebv9vk0` (`assignment_submission_id`),
  ADD UNIQUE KEY `UK740ryybqgkqbku9rubp3s3oyc` (`quiz_submission_id`),
  ADD KEY `FKa1cg1kppiauvtm700008n1imd` (`student_id`);

--
-- Indexes for table `assignment_details`
--
ALTER TABLE `assignment_details`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK6l82eway0vtf8dp5tbl2e11in` (`assessment_id`);

--
-- Indexes for table `assignment_submissions`
--
ALTER TABLE `assignment_submissions`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKjpaoiqlq2bm3rv52lcri47g4s` (`assignment_id`,`student_id`),
  ADD KEY `FKix5j5yhosm3rle137aqkm6wgn` (`student_id`);

--
-- Indexes for table `courses`
--
ALTER TABLE `courses`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK61og8rbqdd2y28rx2et5fdnxd` (`code`);

--
-- Indexes for table `course_contents`
--
ALTER TABLE `course_contents`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKjcgw2ehtbn42cqet3v8vxcslg` (`course_id`,`instructor_id`,`title`,`section_id`),
  ADD KEY `FKkut7hkrrmhwdw3qnr5xgnofgf` (`instructor_id`),
  ADD KEY `FK6ruopg8b6qedie0uximjxevx8` (`section_id`);

--
-- Indexes for table `course_instructors`
--
ALTER TABLE `course_instructors`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_ci_instr_course_section` (`instructor_id`,`course_id`,`section_id`),
  ADD UNIQUE KEY `UKjq3tbg9dsvrydhtowmu7ox0l6` (`instructor_id`,`course_id`,`section_id`),
  ADD KEY `FK4mm05mmoa7y2u3obtjdk45psb` (`course_id`),
  ADD KEY `fk_ci_section` (`section_id`);

--
-- Indexes for table `enrollments`
--
ALTER TABLE `enrollments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKho8mcicp4196ebpltdn9wl6co` (`course_id`),
  ADD KEY `FKh00337cnw2p14fuan7x3rwye6` (`section_id`),
  ADD KEY `FK8kf1u1857xgo56xbfmnif2c51` (`student_id`);

--
-- Indexes for table `file_metadata`
--
ALTER TABLE `file_metadata`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKe1ymdc4xjvj8twv2llted037k` (`assignment_details_id`),
  ADD KEY `FK2p6mstn1d0wtn4ktv0rwh1nvv` (`submission_id`),
  ADD KEY `FKkqgonyh5q7un37omxaahxsfvl` (`course_content_id`);

--
-- Indexes for table `instructors`
--
ALTER TABLE `instructors`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKmb4y0fi7mfnxsktmguwdah6ka` (`instructor_id`),
  ADD UNIQUE KEY `UK9bnko4773vvkd5f2pdbxvsly3` (`user_id`);

--
-- Indexes for table `instructor_profiles`
--
ALTER TABLE `instructor_profiles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKg9v6mp2srognteba48ttsx6nw` (`instructor_id`),
  ADD UNIQUE KEY `UKmmyplvbhocapbhkagne54yffp` (`profile_id`);

--
-- Indexes for table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKqqnsjxlwleyjbxlmm213jaj3f` (`recipient_id`);

--
-- Indexes for table `notification_logs`
--
ALTER TABLE `notification_logs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKnc1gydajjr1axlduw0ttn7nc9` (`notification_id`),
  ADD KEY `FKsbx1lf2w8tr7siwwibcj9k3fg` (`user_id`);

--
-- Indexes for table `profiles`
--
ALTER TABLE `profiles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK4ixsj6aqve5pxrbw2u0oyk8bb` (`user_id`);

--
-- Indexes for table `progress_tracking`
--
ALTER TABLE `progress_tracking`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK5l4aw1ciovwl61lua6vill1vn` (`course_content_id`),
  ADD KEY `FKemisaxnowm3dgifuwyf6n865q` (`enrollment_id`);

--
-- Indexes for table `quiz_details`
--
ALTER TABLE `quiz_details`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKrej67gx19tnypph0qaeoh64s1` (`assessment_id`);

--
-- Indexes for table `quiz_options`
--
ALTER TABLE `quiz_options`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKhpr35shfeo1uxwqnx0kwxd0b0` (`quiz_question_id`);

--
-- Indexes for table `quiz_questions`
--
ALTER TABLE `quiz_questions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK5veycjomff211l9kf8g0faf36` (`quiz_id`);

--
-- Indexes for table `quiz_submissions`
--
ALTER TABLE `quiz_submissions`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKeu8wjlll8l727kkipq55d8gb7` (`quiz_id`,`student_id`),
  ADD KEY `FKexmf2yq2mkaqe4aucsbp8nl2s` (`student_id`);

--
-- Indexes for table `refresh_token`
--
ALTER TABLE `refresh_token`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKr4k4edos30bx9neoq81mdvwph` (`token`),
  ADD UNIQUE KEY `UKf95ixxe7pa48ryn1awmh2evt7` (`user_id`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKofx66keruapi6vyqpv6f2or37` (`name`);

--
-- Indexes for table `schedule_entries`
--
ALTER TABLE `schedule_entries`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK542d7hw0d9m85x8myjtha1efk` (`section_id`),
  ADD KEY `FKs38p83cmodn6e8g4sskr6ujtj` (`course_id`);

--
-- Indexes for table `sections`
--
ALTER TABLE `sections`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK453ajk8xg44uq3ju35wwsg0p4` (`course_id`,`number`,`semester_id`),
  ADD KEY `FKsgsbntbuyi2uk5wk0s6d1jwxb` (`semester_id`);

--
-- Indexes for table `semesters`
--
ALTER TABLE `semesters`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKj4aj4e6d83nc60uvvyjvjyp4y` (`term`,`year`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKg4fwvutq09fjdlb4bb0byp7t` (`user_id`),
  ADD UNIQUE KEY `UKhnkdj453spsaokfe0dqhnwwyt` (`student_id`);

--
-- Indexes for table `student_profiles`
--
ALTER TABLE `student_profiles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKk72skbe7cvetmu4vseiplnvac` (`profile_id`),
  ADD UNIQUE KEY `UKbhey0124ikags176pln84q1t0` (`student_id`),
  ADD KEY `FKrg3ilskb73cs9pxpuneq9vuct` (`advisor_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  ADD KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`);

--
-- Indexes for table `user_preferences`
--
ALTER TABLE `user_preferences`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKqy8dkrkc8b34dcgwoq2km43rd` (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `assessments`
--
ALTER TABLE `assessments`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `assessment_grades`
--
ALTER TABLE `assessment_grades`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `assignment_details`
--
ALTER TABLE `assignment_details`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `assignment_submissions`
--
ALTER TABLE `assignment_submissions`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `courses`
--
ALTER TABLE `courses`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `course_contents`
--
ALTER TABLE `course_contents`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `course_instructors`
--
ALTER TABLE `course_instructors`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `enrollments`
--
ALTER TABLE `enrollments`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=149;

--
-- AUTO_INCREMENT for table `file_metadata`
--
ALTER TABLE `file_metadata`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `instructors`
--
ALTER TABLE `instructors`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `instructor_profiles`
--
ALTER TABLE `instructor_profiles`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `notification_logs`
--
ALTER TABLE `notification_logs`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `profiles`
--
ALTER TABLE `profiles`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `progress_tracking`
--
ALTER TABLE `progress_tracking`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `quiz_details`
--
ALTER TABLE `quiz_details`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `quiz_options`
--
ALTER TABLE `quiz_options`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `quiz_questions`
--
ALTER TABLE `quiz_questions`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `quiz_submissions`
--
ALTER TABLE `quiz_submissions`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `refresh_token`
--
ALTER TABLE `refresh_token`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `schedule_entries`
--
ALTER TABLE `schedule_entries`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `sections`
--
ALTER TABLE `sections`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `semesters`
--
ALTER TABLE `semesters`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `students`
--
ALTER TABLE `students`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT for table `student_profiles`
--
ALTER TABLE `student_profiles`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT for table `user_preferences`
--
ALTER TABLE `user_preferences`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `assessments`
--
ALTER TABLE `assessments`
  ADD CONSTRAINT `FKa2nh608bmj0k0wjf0rw7oiha5` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  ADD CONSTRAINT `FKicyqpvtbc5g0la0kxjbum8vqb` FOREIGN KEY (`instructor_id`) REFERENCES `instructors` (`id`);

--
-- Constraints for table `assessment_grades`
--
ALTER TABLE `assessment_grades`
  ADD CONSTRAINT `FK8v6big4k5q54d2pxk9g3si109` FOREIGN KEY (`quiz_submission_id`) REFERENCES `quiz_submissions` (`id`),
  ADD CONSTRAINT `FKa1cg1kppiauvtm700008n1imd` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`),
  ADD CONSTRAINT `FKex9o9w4ytqb1h9wuq32viwig7` FOREIGN KEY (`assignment_submission_id`) REFERENCES `assignment_submissions` (`id`),
  ADD CONSTRAINT `FKomeptep32fe94e2h9ecy16pwv` FOREIGN KEY (`assessment_id`) REFERENCES `assessments` (`id`);

--
-- Constraints for table `assignment_details`
--
ALTER TABLE `assignment_details`
  ADD CONSTRAINT `FKm08smn7vrf76e3kacv9udrl6b` FOREIGN KEY (`assessment_id`) REFERENCES `assessments` (`id`);

--
-- Constraints for table `assignment_submissions`
--
ALTER TABLE `assignment_submissions`
  ADD CONSTRAINT `FKcexglly8ekmdu2q2yf7k8c07d` FOREIGN KEY (`assignment_id`) REFERENCES `assignment_details` (`id`),
  ADD CONSTRAINT `FKix5j5yhosm3rle137aqkm6wgn` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

--
-- Constraints for table `course_contents`
--
ALTER TABLE `course_contents`
  ADD CONSTRAINT `FK6ruopg8b6qedie0uximjxevx8` FOREIGN KEY (`section_id`) REFERENCES `sections` (`id`),
  ADD CONSTRAINT `FKkut7hkrrmhwdw3qnr5xgnofgf` FOREIGN KEY (`instructor_id`) REFERENCES `instructors` (`id`),
  ADD CONSTRAINT `FKrd8pyow3gkglte0993jpipbu5` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`);

--
-- Constraints for table `course_instructors`
--
ALTER TABLE `course_instructors`
  ADD CONSTRAINT `FK4mm05mmoa7y2u3obtjdk45psb` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  ADD CONSTRAINT `FK8kqu9lo7aqj5wo3c19p1sv1y` FOREIGN KEY (`instructor_id`) REFERENCES `instructors` (`id`),
  ADD CONSTRAINT `fk_ci_section` FOREIGN KEY (`section_id`) REFERENCES `sections` (`id`);

--
-- Constraints for table `enrollments`
--
ALTER TABLE `enrollments`
  ADD CONSTRAINT `FK8kf1u1857xgo56xbfmnif2c51` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`),
  ADD CONSTRAINT `FKh00337cnw2p14fuan7x3rwye6` FOREIGN KEY (`section_id`) REFERENCES `sections` (`id`),
  ADD CONSTRAINT `FKho8mcicp4196ebpltdn9wl6co` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`);

--
-- Constraints for table `file_metadata`
--
ALTER TABLE `file_metadata`
  ADD CONSTRAINT `FK2p6mstn1d0wtn4ktv0rwh1nvv` FOREIGN KEY (`submission_id`) REFERENCES `assignment_submissions` (`id`),
  ADD CONSTRAINT `FKe1ymdc4xjvj8twv2llted037k` FOREIGN KEY (`assignment_details_id`) REFERENCES `assignment_details` (`id`),
  ADD CONSTRAINT `FKkqgonyh5q7un37omxaahxsfvl` FOREIGN KEY (`course_content_id`) REFERENCES `course_contents` (`id`);

--
-- Constraints for table `instructors`
--
ALTER TABLE `instructors`
  ADD CONSTRAINT `FKds2m3jgxj98sd5mr1qw23ecjp` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `instructor_profiles`
--
ALTER TABLE `instructor_profiles`
  ADD CONSTRAINT `FKpbbmqbf3ovy1mxs55tfo7yj99` FOREIGN KEY (`profile_id`) REFERENCES `profiles` (`id`),
  ADD CONSTRAINT `FKtrgxp91p2pc5f2ji9g91bujf` FOREIGN KEY (`instructor_id`) REFERENCES `instructors` (`id`);

--
-- Constraints for table `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `FKqqnsjxlwleyjbxlmm213jaj3f` FOREIGN KEY (`recipient_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `notification_logs`
--
ALTER TABLE `notification_logs`
  ADD CONSTRAINT `FKnc1gydajjr1axlduw0ttn7nc9` FOREIGN KEY (`notification_id`) REFERENCES `notifications` (`id`),
  ADD CONSTRAINT `FKsbx1lf2w8tr7siwwibcj9k3fg` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `profiles`
--
ALTER TABLE `profiles`
  ADD CONSTRAINT `FK410q61iev7klncmpqfuo85ivh` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `progress_tracking`
--
ALTER TABLE `progress_tracking`
  ADD CONSTRAINT `FK5l4aw1ciovwl61lua6vill1vn` FOREIGN KEY (`course_content_id`) REFERENCES `course_contents` (`id`),
  ADD CONSTRAINT `FKemisaxnowm3dgifuwyf6n865q` FOREIGN KEY (`enrollment_id`) REFERENCES `enrollments` (`id`);

--
-- Constraints for table `quiz_details`
--
ALTER TABLE `quiz_details`
  ADD CONSTRAINT `FKkxj8qlf76vfwymr1trmhu8w9q` FOREIGN KEY (`assessment_id`) REFERENCES `assessments` (`id`);

--
-- Constraints for table `quiz_options`
--
ALTER TABLE `quiz_options`
  ADD CONSTRAINT `FKhpr35shfeo1uxwqnx0kwxd0b0` FOREIGN KEY (`quiz_question_id`) REFERENCES `quiz_questions` (`id`);

--
-- Constraints for table `quiz_questions`
--
ALTER TABLE `quiz_questions`
  ADD CONSTRAINT `FK5veycjomff211l9kf8g0faf36` FOREIGN KEY (`quiz_id`) REFERENCES `quiz_details` (`id`);

--
-- Constraints for table `quiz_submissions`
--
ALTER TABLE `quiz_submissions`
  ADD CONSTRAINT `FKeo3j0098ub3wv7fypx46q92my` FOREIGN KEY (`quiz_id`) REFERENCES `quiz_details` (`id`),
  ADD CONSTRAINT `FKexmf2yq2mkaqe4aucsbp8nl2s` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

--
-- Constraints for table `refresh_token`
--
ALTER TABLE `refresh_token`
  ADD CONSTRAINT `FKjtx87i0jvq2svedphegvdwcuy` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `schedule_entries`
--
ALTER TABLE `schedule_entries`
  ADD CONSTRAINT `FK542d7hw0d9m85x8myjtha1efk` FOREIGN KEY (`section_id`) REFERENCES `sections` (`id`),
  ADD CONSTRAINT `FKs38p83cmodn6e8g4sskr6ujtj` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`);

--
-- Constraints for table `sections`
--
ALTER TABLE `sections`
  ADD CONSTRAINT `FK7ty9cevpq04d90ohtso1q8312` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  ADD CONSTRAINT `FKsgsbntbuyi2uk5wk0s6d1jwxb` FOREIGN KEY (`semester_id`) REFERENCES `semesters` (`id`);

--
-- Constraints for table `students`
--
ALTER TABLE `students`
  ADD CONSTRAINT `FKdt1cjx5ve5bdabmuuf3ibrwaq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `student_profiles`
--
ALTER TABLE `student_profiles`
  ADD CONSTRAINT `FKhgg3ld43ur7w5dnyii8eghsyc` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`),
  ADD CONSTRAINT `FKorqx1y1095s1gn6xvqxt916fh` FOREIGN KEY (`profile_id`) REFERENCES `profiles` (`id`),
  ADD CONSTRAINT `FKrg3ilskb73cs9pxpuneq9vuct` FOREIGN KEY (`advisor_id`) REFERENCES `instructor_profiles` (`id`);

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);

--
-- Constraints for table `user_preferences`
--
ALTER TABLE `user_preferences`
  ADD CONSTRAINT `FKepakpib0qnm82vmaiismkqf88` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
