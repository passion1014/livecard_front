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

    private String userId;
    private String userPw;
    private String userNm;
    private String userPhone;
    private String userEmail;
    private String userAddr;
    private String userAddrDetail;
    private String userZipcode;
    private String userBirth;
    private String userGender;
    private String userGrade;
    private String userStatus;
    private String userRegDate;
    private String userUpdateDate;
    private String userDelDate;
}