package com.example.project.CourseContents;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.util.List;
public interface CourseContentService {
    CollectionModel<EntityModel<CourseContentDTO>> all(Pageable pageable);
    EntityModel<CourseContentDTO> one(Long id);
    ResponseEntity<?> addCourseContent(CourseContentDTO dto);
    ResponseEntity<?> deleteCourseContent(Long id);
    CollectionModel<EntityModel<CourseContentDTO>> getContentByCourse(Long courseId);
    CollectionModel<EntityModel<CourseContentDTO>> getContentByInstructor(Long instructorId);
    
    void deleteByInstructorAndContentId(Long instructorId, Long contentId);

     List<CourseContentDTO> getPlainContentByCourse(Long courseId);
    CourseContentDTO createContentEntry(CourseContentDTO dto);
    CourseContentDTO editContentEntry(Long contentId, CourseContentDTO dto);
    void deleteContentEntry(Long contentId);


}
