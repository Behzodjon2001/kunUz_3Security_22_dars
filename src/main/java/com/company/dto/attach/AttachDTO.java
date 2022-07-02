package com.company.dto.attach;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttachDTO {
    private String id;
    private String originalName;
    private String extension;
    private Long size;
    private String path;
    private LocalDateTime createdDate;
    private String url;
}
