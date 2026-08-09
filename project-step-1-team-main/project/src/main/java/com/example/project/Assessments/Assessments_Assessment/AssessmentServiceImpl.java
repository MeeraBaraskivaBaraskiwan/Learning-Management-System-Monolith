package com.example.project.Assessments.Assessments_Assessment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Sinks;

import com.example.project.Courses.Course;
import com.example.project.Courses.CourseRepository;
import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Instructors.Instructor;
import com.example.project.Instructors.InstructorRepository;
import com.example.project.Sections.Section;
import com.example.project.Sections.SectionRepository;


@Service
public class AssessmentServiceImpl implements AssessmentService {

    private static final Logger logger = LoggerFactory.getLogger(AssessmentServiceImpl.class); 

     
    private final AssessmentRepository assessmentRepository;

    private final CourseRepository courseRepository;
    
    private final InstructorRepository instructorRepository;
    
    private final AssessmentAssembler assessmentAssembler;

    private final PagedResourcesAssembler<AssessmentDTO> pagedAssembler;

    private final SectionRepository sectionRepository;
    private final Sinks.Many<AssessmentDTO> assessmentSink;

    public AssessmentServiceImpl(AssessmentRepository assessmentRepository,SectionRepository sectionRepository, CourseRepository courseRepository, InstructorRepository instructorRepository, AssessmentAssembler assessmentAssembler,PagedResourcesAssembler<AssessmentDTO> pagedAssembler,Sinks.Many<AssessmentDTO> assessmentSink) {

     this.assessmentRepository = assessmentRepository;
     this.courseRepository = courseRepository;
     this.instructorRepository = instructorRepository;
     this.assessmentAssembler = assessmentAssembler;
     this.pagedAssembler = pagedAssembler;
  this.sectionRepository = sectionRepository;
   this.assessmentSink    = assessmentSink;
}





@Override
    public PagedModel<EntityModel<AssessmentDTO>> getAllAssessments(Pageable pageable) {
        logger.info("Retrieving all assessments with pagination: {}", pageable); 

        Page<AssessmentDTO> assessmentPage = assessmentRepository.findAll(pageable)
                .map(AssessmentMapper::toDTO);

        return pagedAssembler.toModel(assessmentPage, assessmentAssembler);
    }
   


    @Override
    public EntityModel<AssessmentDTO> getAssessmentById(Long id) {
        logger.info("Fetching assessment by ID: {}", id);
        Assessment assessment = assessmentRepository.findById(id)
        .orElseThrow(() -> {
            logger.error("Assessment with ID {} not found", id); 
            return new ResourceNotFoundException("Assessment with ID " + id + " not found");
        });
        return assessmentAssembler.toModel(AssessmentMapper.toDTO(assessment));
    }


  


@Override
    public PagedModel<EntityModel<AssessmentDTO>> getAssessmentsByCourseCode(String courseCode, Pageable pageable) {
        logger.info("Fetching assessments by course code: {}", courseCode); 
        Page<AssessmentDTO> assessmentPage = assessmentRepository.findByCourseCode(courseCode, pageable)
                .map(AssessmentMapper::toDTO);

                if (assessmentPage.isEmpty()) {
                    logger.error("No assessments found for course {}", courseCode); 
                    throw new ResourceNotFoundException("No assessments found for course " + courseCode);
                }

        return pagedAssembler.toModel(assessmentPage, assessmentAssembler);
    }

   @Override
    public ResponseEntity<?> createAssessment(AssessmentDTO dto) {
        logger.info("Creating new assessment for course {} and instructor {}", dto.getCourseCode(), dto.getInstructorId());
      
        Course course = courseRepository.findByCode(dto.getCourseCode())
        .orElseThrow(() -> {
            logger.error("Course with code {} not found", dto.getCourseCode()); 
            return new ResourceNotFoundException("Course with code " + dto.getCourseCode() + " not found");
        });

        Instructor instructor = instructorRepository.findById(dto.getInstructorId())
        .orElseThrow(() -> {
                    logger.error("Instructor with ID {} not found", dto.getInstructorId()); 
                    return new ResourceNotFoundException("Instructor with ID " + dto.getInstructorId() + " not found");
                });

                 Section section = sectionRepository.findById(dto.getSectionId())
           .orElseThrow(() -> new ResourceNotFoundException("Section with ID " + dto.getSectionId() + " not found"));

Assessment assessment = AssessmentMapper.toEntity(dto, course, instructor, section);
        // save + assemble as before…        assessment = assessmentRepository.save(assessment);

 Assessment saved = assessmentRepository.save(assessment);
     AssessmentDTO out = AssessmentMapper.toDTO(saved);
        assessmentSink.tryEmitNext(out);

        logger.debug("Assessment created with ID: {}", assessment.getId());

         EntityModel<AssessmentDTO> entityModel = assessmentAssembler.toModel(out);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

   


    @Override
public ResponseEntity<?> updateAssessment(Long id, AssessmentDTO dto) {
    logger.info("Updating or creating assessment with ID: {}", id);
   
    Course course = courseRepository.findByCode(dto.getCourseCode())
    .orElseThrow(() -> {
        logger.error("Course with code {} not found", dto.getCourseCode()); 
        return new ResourceNotFoundException("Course with code " + dto.getCourseCode() + " not found");
    });

        Instructor instructor = instructorRepository.findById(dto.getInstructorId())
    .orElseThrow(() -> {
                    logger.error("Instructor with ID {} not found", dto.getInstructorId()); 
                    return new ResourceNotFoundException("Instructor with ID " + dto.getInstructorId() + " not found");
                }); 
                     Section section = sectionRepository.findById(dto.getSectionId())
           .orElseThrow(() -> new ResourceNotFoundException("Section with ID " + dto.getSectionId() + " not found"));        
   
           return assessmentRepository.findById(id)
            .map(assessment -> {
                logger.debug("Updating existing assessment with ID: {}", id); 
                assessment.setTitle(dto.getTitle());
                assessment.setDescription(dto.getDescription());
                assessment.setDueDate(dto.getDueDate());
                assessment.setSection(section);
                assessment.setCourse(course);
                assessment.setInstructor(instructor);
                assessmentRepository.save(assessment);
               AssessmentDTO out = AssessmentMapper.toDTO(assessment);
            assessmentSink.tryEmitNext(out);

             EntityModel<AssessmentDTO> entityModel = assessmentAssembler.toModel(out);
                return ResponseEntity.ok(entityModel); 
            })
            .orElseGet(() -> {
                logger.debug("Creating new assessment since ID {} not found", id);
               
            Assessment newAssessment = AssessmentMapper.toEntity(dto, course, instructor, section);                 Assessment savedAssessment = assessmentRepository.save(newAssessment);

                EntityModel<AssessmentDTO> entityModel = assessmentAssembler.toModel(AssessmentMapper.toDTO(savedAssessment));
                return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel); // ✅ Returns 201 Created
            });
}


    @Override
    public ResponseEntity<?> deleteAssessment(Long id) {
        logger.info("Deleting assessment with ID: {}", id);
        if (!assessmentRepository.existsById(id)) {
            logger.error("Assessment with ID {} not found", id);
            throw new ResourceNotFoundException("Assessment with ID " + id + " not found");
        }

        assessmentRepository.deleteById(id);
        logger.debug("Assessment with ID {} successfully deleted", id);
        return ResponseEntity.noContent().build();
    }


    
    @Override
    public PagedModel<EntityModel<AssessmentDTO>> getAssessmentsByCourseId(Long courseId, Pageable pageable) {
        Page<AssessmentDTO> page = assessmentRepository
            .findByCourse_Id(courseId, pageable)
            .map(AssessmentMapper::toDTO);

        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No assessments found for course ID " + courseId);
        }
        return pagedAssembler.toModel(page, assessmentAssembler);
    }


    

   

}
