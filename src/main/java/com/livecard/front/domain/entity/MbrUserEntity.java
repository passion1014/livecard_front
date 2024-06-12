package com.livecard.front.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "MBR_USER")
public class MbrUserEntity extends SystemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "SOCIAL_ID", length = 50)
    private String socialId; // 소셜 ID

    @Column(name = "NAME", length = 50)
    private String name; // 이름

    @Column(name = "PROVIDER", length = 50)
    private String provider; // 제공자 (예: GOOGLE, FACEBOOK 등)

    @Column(name = "ROLE", length = 50)
    private String role; // 역할

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mbrUserEntity")
    @ToString.Exclude
    private List<MbrUserProfileEntity> mbrUserProfileEntity;

//    @OneToOne(fetch = FetchType.LAZY, mappedBy = "mbrUserEntity")
//    private AstUserBalanceEntity mbrUserEntity;
}