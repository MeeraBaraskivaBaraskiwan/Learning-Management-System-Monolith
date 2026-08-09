package com.example.project.Instructors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import java.util.DoubleSummaryStatistics;

@Repository
public class InstructorRepositoryImpl implements InstructorRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public long countStudentsForInstructor(Long instructorId) {
        // count distinct students across all that instructor’s courses
        String jpql =
            "SELECT COUNT(DISTINCT e.student.id) " +
            "FROM CourseInstructor ci " +
            " JOIN ci.course c " +
            " JOIN c.enrollments e " +
            "WHERE ci.instructor.id = :instId";
        return em.createQuery(jpql, Long.class)
                 .setParameter("instId", instructorId)
                 .getSingleResult();
    }

    @Override
    public long countAssessmentsForInstructor(Long instructorId) {
        String jpql =
            "SELECT COUNT(a) " +
            "FROM Assessment a " +
            "WHERE a.instructor.id = :instId";
        return em.createQuery(jpql, Long.class)
                 .setParameter("instId", instructorId)
                 .getSingleResult();
    }

   @Override
public double calcPassRate(Long instructorId) {
    // percent of grades whose finalScore >= 50% of max (assumes finalScore between 0–100)
    String jpql =
        "SELECT SUM(CASE WHEN g.finalScore >= 50 THEN 1 ELSE 0 END), " +
        "       COUNT(g) " +
        "  FROM AssessmentGrade g " +
        " WHERE g.assessment.instructor.id = :instId";

    Object[] result = (Object[]) em.createQuery(jpql)
        .setParameter("instId", instructorId)
        .getSingleResult();

    long passed = ((Number) result[0]).longValue();
    long total  = ((Number) result[1]).longValue();
    return total == 0 ? 0 : (passed * 100.0 / total);
}

    @Override
public double calcImpact(Long instructorId) {
    // average finalScore across all grades
    String jpql =
        "SELECT AVG(g.finalScore) " +
        "  FROM AssessmentGrade g " +
        " WHERE g.assessment.instructor.id = :instId";
    Double avg = em.createQuery(jpql, Double.class)
                   .setParameter("instId", instructorId)
                   .getSingleResult();
    return avg == null ? 0 : avg;
}

}