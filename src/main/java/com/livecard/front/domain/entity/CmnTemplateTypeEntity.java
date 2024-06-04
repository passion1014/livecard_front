package com.livecard.front.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CMN_TEMPLATE_TYPE")
public class CmnTemplateTypeEntity extends SystemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    // 코드
    @Column(name = "CODE", nullable = false, length = 4)
    private String code;

    // 코드명
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;
}