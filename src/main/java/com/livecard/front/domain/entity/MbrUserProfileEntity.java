package com.livecard.front.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "MBR_USER_PROFILE")
public class MbrUserProfileEntity extends SystemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "EMAIL", length = 50)
    private String email; // 이메일

    //주소
    @Column(name = "ADDRESS", length = 200)
    private String address;

    //상세주소
    @Column(name = "DETAIL_ADDRESS", length = 200)
    private String detailAddress;

    //전화번호
    @Column(name = "PHONE", length = 20)
    private String phone;

    //사무실전화번호
    @Column(name = "OFFICE_PHONE", length = 20)
    private String officePhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBR_USER_ID")
    private MbrUserEntity mbrUserEntity;
}