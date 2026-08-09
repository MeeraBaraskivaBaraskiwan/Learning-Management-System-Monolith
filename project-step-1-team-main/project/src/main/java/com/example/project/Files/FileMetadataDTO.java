package com.example.project.Files;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class FileMetadataDTO {

    private Long id;

    @NotBlank(message = "Original filename is required")
    private String originalFilename;

    @NotBlank(message = "Stored filename is required")
    private String storedFilename;

    @NotBlank(message = "File extension is required")
    private String fileExtension;

    @NotNull(message = "File size is required")
    @Positive(message = "File size must be greater than zero")
    private Long fileSize;

    @NotNull(message = "Owner type must be specified")
    private FileOwnerType ownerType;

    @NotNull(message = "Owner ID must be provided")
    private Long ownerId;
}


