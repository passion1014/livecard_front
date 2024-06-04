package com.livecard.front.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "CMN_COMMON_CODE")
public class CmnCommonCodeEntity extends SystemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "CODE_TYPE", nullable = false, length = 4)
    private String codeType;

    @NotNull
    @Column(name = "CODE", nullable = false, length = 50)
    private String code;

    @Size(max = 50)
    @NotNull
    @Column(name = "NAME", nullable = false, length = 200)
    private String name;

}