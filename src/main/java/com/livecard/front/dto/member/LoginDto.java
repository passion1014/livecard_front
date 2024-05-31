package com.livecard.front.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @Schema(description = "사용자 ID", example = "admin", required = true)
    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @Schema(description = "비밀번호", example = "1234", required = true)
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @Schema(description = "로그인유지", required = true)
    @NotNull
    private boolean remember;

}