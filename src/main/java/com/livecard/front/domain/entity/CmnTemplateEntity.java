package com.livecard.front.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CMN_TEMPLATE")
public class CmnTemplateEntity extends SystemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    // 템플릿명
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    // 템플릿타입
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEMPLATE_TYPE_ID")
    private CmnTemplateTypeEntity cmnTemplateTypeEntity;

    // 템플릿내용 (html소스코드가 들어간다)
    @Column(name = "CONTENT", nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    // 템플릿설명
    @Column(name = "DESCRIPTION", length = 200)
    private String description;

}