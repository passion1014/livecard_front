package com.livecard.front.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(name = "MenuBadgeDto", description = "메뉴 정보")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuBadgeDto {
    @Schema(description = "PK")
    private Long id;

    @Schema(description = "1시간 신규등록 회원수")
    private int memberCnt;

    @Schema(description = "1시간 신규등록 양돈수")
    private int pigCnt;

    @Schema(description = "1시간 신규등록 센서수")
    private int sensorCnt;

    @Schema(description = "1시간 신규등록 공지사항수")
    private int noticeCnt;

    @Schema(description = "1시간 신규등록 업데이트일")
    private LocalDateTime updateDate;

    @Schema(description = "업데이트시 필요필드")
    private String type;
}
