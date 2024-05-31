package com.livecard.front.domain.entity;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "STATUS", nullable = false)
    private Character status;

    @NotNull
    @Column(name = "TYPE_TYPE", nullable = false, length = 4)
    private String typeType;

    @Column(name = "TYPE_LEVEL")
    private Byte typeLevel;

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