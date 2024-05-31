package com.livecard.front.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoveDto {
    @Schema(description = "회원비밀번호")
    private String memberPass;
    @Schema(description = "pk코드")
    private Long code;
    @Schema(description = "성공실패 여부")
    private boolean success;
    @Schema(description = "성공실패 메시지")
    private String message;
}
