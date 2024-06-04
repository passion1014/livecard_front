package com.livecard.front.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CRD_MEDIA")
public class CrdMediaEntity extends SystemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_MEDIA_ID")
    private CrdMediaEntity parentMediaEntity; // 편집할 경우 원본 이미지를 가리킨다.

    @Column(name = "MEDIA_TYPE")
//    @Enumerated(EnumType.STRING)
    private String mediaType;

    @Size(max = 255)
    @NotNull
    @Column(name = "MEDIA_ORIGINAL_FILE_NAME", nullable = false)
    private String mediaOriginalFileName;

    @Size(max = 255)
    @NotNull
    @Column(name = "MEDIA_SAVED_FILE_NAME", nullable = false)
    private String mediaSavedFileName;

    @Size(max = 10)
    @NotNull
    @Column(name = "MEDIA_FILE_EXTENSION", nullable = false, length = 10)
    private String mediaFileExtension;

    @NotNull
    @Column(name = "MEDIA_FILE_SIZE", nullable = false)
    private Long mediaFileSize;

    @Size(max = 255)
    @NotNull
    @Column(name = "MEDIA_FILE_PATH", nullable = false)
    private String mediaFilePath;
}