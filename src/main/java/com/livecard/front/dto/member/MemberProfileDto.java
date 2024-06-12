package com.livecard.front.dto.member;

import com.livecard.front.domain.entity.MbrUserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberProfileDto {
    private Long id;
    private String email; // 이메일
    private String address; //주소
    private String detailAddress; //상세주소
    private String phone; //전화번호
    private String officePhone; //사무실전화번호
}
