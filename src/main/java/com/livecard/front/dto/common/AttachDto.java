package com.livecard.front.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Schema(name = "AttachDto", description = "첨부파일정보")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttachDto implements Serializable {

    @Schema(description = "첨부파일PK")
    private Long id;
    @Schema(description = "첨부파일원본명")
    private String attachOriginalFileName;
    @Schema(description = "첨부파일저장명")
    private String attachSavedFileName;
    @Schema(description = "첨부파일확장자")
    private String attachFileExtension;
    @Schema(description = "첨부파일크기")
    private Long attachFileSize;
    @Schema(description = "첨부파일경로")
    private String attachFilePath;
    @Schema(description = "등록자")
    private String memberId;
}