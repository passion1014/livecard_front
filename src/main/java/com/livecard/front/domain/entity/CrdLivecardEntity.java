package com.livecard.front.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "CRD_LIVECARD")
public class CrdLivecardEntity extends SystemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    // 라이브카드명
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    // 라이브카드설명
    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    // 사용자정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBR_USER_PROFILE_ID")
    private MbrUserProfileEntity mbrUserProfileEntity;

    // 템플릿내용 (html소스코드가 들어간다)
    @Column(name = "CONTENT", nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CMN_TEMPLATE_ID")
    private CmnTemplateEntity cmnTemplateEntity;

    @OneToMany(fetch = FetchType.LAZY)
    private List<CrdMediaEntity> crdMediaEntity; // 해당 라이브카드에서 사용한 미디어 목록
}