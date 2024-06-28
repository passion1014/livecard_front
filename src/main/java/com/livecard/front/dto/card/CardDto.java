package com.livecard.front.dto.card;


import jakarta.persistence.Column;
import lombok.Data;

/**
 *
 */
@Data
public class CardDto {

    private Long crdLivecardId;

    // 라이브카드명
    private String name;

    private String content; // 템플릿내용 (html소스코드가 들어간다)
    // 라이브카드설명
    private String description;

    private String cmnTemplateId;

    private String mbrUserProfileId;

    private String mbrUserId;

    private String email; // 이메일

    private String address; //주소

    private String detailAddress; //상세주소

    private String phone; //전화번호

    private String officePhone; //사무실전화번호


}
