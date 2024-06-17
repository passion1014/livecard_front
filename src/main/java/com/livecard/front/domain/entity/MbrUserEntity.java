package com.livecard.front.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "MBR_USER")
@NoArgsConstructor
public class MbrUserEntity extends SystemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "SOCIAL_ID", length = 50, nullable = false, unique= true)
    private String socialId; // 소셜 ID

    @Column(name = "NAME", length = 50)
    private String name; // 이름

    @Column(name = "PROVIDER_CD", length = 1, nullable = false)
    private String providerCd; // 제공자 (KAKAO=0, NAVER=1, GOOGLE=2)

    @Column(name = "ROLE", length = 50)
    private String role; // 역할

    @Column(name = "PROFILE_IMG", length = 4000)
    private String profileImg; // 프로필 이미지

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mbrUserEntity")
    @ToString.Exclude
    private List<MbrUserProfileEntity> mbrUserProfileEntity;

//    @OneToOne(fetch = FetchType.LAZY, mappedBy = "mbrUserEntity")
//    private AstUserBalanceEntity mbrUserEntity;


    @Builder
    public MbrUserEntity(String socialId, String name, String providerCd, String role, String profileImg) {
        this.socialId = socialId;
        this.name = name;
        this.providerCd = providerCd;
        this.role = role;
        this.profileImg = profileImg;
    }
}