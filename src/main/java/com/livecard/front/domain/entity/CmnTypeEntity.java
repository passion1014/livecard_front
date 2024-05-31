package com.livecard.front.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "CMN_TYPE")
public class CmnTypeEntity extends SystemEntity {
    @Id
    @Column(name = "TYPE_CODE", nullable = false)
    private Long id;

//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "TYPE_FARM_CODE", nullable = false)
//    private MoFarm typeFarmCode;

    @NotNull
    @Column(name = "STATUS", nullable = false)
    private Character status;

    @NotNull
    @Column(name = "TYPE_TYPE", nullable = false, length = 4)
    private String typeType;

    @Column(name = "TYPE_LEVEL")
    private Byte typeLevel;

//    @Column(name = "TYPE_IMG_ID")
//    private Long typeImgId;

    @NotNull
    @Column(name = "CODE", nullable = false, length = 4)
    private String code;

    @Size(max = 50)
    @NotNull
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @NotNull
    @Column(name = "TYPE_DATE", nullable = false)
    private LocalDateTime typeDate;

}