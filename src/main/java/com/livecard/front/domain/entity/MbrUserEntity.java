package com.livecard.front.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "MBR_USER")
public class MbrUserEntity extends SystemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    private String userNo;
    private String email;
    private String password;
    private String name;
    private String profileImgPath;
    private String profileImgNm;
    private String profileImgExt;
    private String profileImgSize; // 프로필 이미지 사이즈
    private String profileImgType; // 프로필 이미지 타입


}