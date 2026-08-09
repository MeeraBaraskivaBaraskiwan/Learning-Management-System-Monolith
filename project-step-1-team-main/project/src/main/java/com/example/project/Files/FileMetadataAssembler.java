package com.example.project.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

@Component
public class FileMetadataAssembler implements RepresentationModelAssembler<FileMetadataDTO, EntityModel<FileMetadataDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(FileMetadataAssembler.class);

    @Override
    public EntityModel<FileMetadataDTO> toModel(FileMetadataDTO fileMetadataDTO) {
        logger.info("Assembling EntityModel for FileMetadataDTO with ID: {}", fileMetadataDTO.getId());

        Pageable defaultPageable = PageRequest.of(0, 5);

        String directory;
        if (fileMetadataDTO.getOwnerType() == FileOwnerType.ASSIGNMENT_DETAILS) {
            directory = "assignments";
        } else if (fileMetadataDTO.getOwnerType() == FileOwnerType.ASSIGNMENT_SUBMISSION) {
            directory = "submissions";
        } else if (fileMetadataDTO.getOwnerType() == FileOwnerType.COURSE_CONTENT) {
            directory = "courseContents";
        } else {
            directory = "unknown";
        }

        logger.info("Determined directory for FileMetadataDTO with ID {}: {}", fileMetadataDTO.getId(), directory);

        EntityModel<FileMetadataDTO> entityModel = EntityModel.of(fileMetadataDTO,
                linkTo(methodOn(FileMetadataController.class).getFileById(fileMetadataDTO.getId())).withSelfRel(),
                linkTo(methodOn(FileMetadataController.class).getAllFiles(defaultPageable)).withRel("all-files"),
                createDownloadLink(directory, fileMetadataDTO.getStoredFilename()),
                linkTo(methodOn(FileMetadataController.class).deleteFile(fileMetadataDTO.getId())).withRel("delete-file")
        );

        logger.info("Successfully assembled EntityModel for FileMetadataDTO with ID: {}", fileMetadataDTO.getId());
        return entityModel;
    }

    // Safe way to handle IOException
    private static Link createDownloadLink(String directory, String storedFilename) {
        logger.info("Creating download link for directory: {} and storedFilename: {}", directory, storedFilename);
        return Link.of("/api/files/" + directory + "/" + storedFilename).withRel("download-file");
    }
}
