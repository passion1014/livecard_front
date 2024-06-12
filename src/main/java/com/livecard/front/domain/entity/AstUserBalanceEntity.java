package com.livecard.front.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "AST_USER_BALANCE")
public class AstUserBalanceEntity extends SystemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    // 사용자 ID
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @ToString.Exclude
    private MbrUserEntity mbrUserEntity;


    // 포인트
    @Column(name = "POINT", nullable = false)
    private Long point;

}
