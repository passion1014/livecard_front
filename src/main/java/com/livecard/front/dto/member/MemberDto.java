package com.livecard.front.dto.member;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String socialId; // 소셜 ID
    private String name; // 이름
    private String providerCd; // 제공자 (예: GOOGLE, FACEBOOK 등)
    private String role; // 역할

    private List<MemberProfileDto> memberProfileDto;
    private MemberBalanceDto memberBalanceDto;

}
