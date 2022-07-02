package com.company.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "attach")
public class AttachEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false)
    private String extension;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private String path;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    public AttachEntity() {
    }

    public AttachEntity(String attachId) {

    }
}
