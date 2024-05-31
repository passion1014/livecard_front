package com.livecard.front.domain.entity;

import com.livecard.front.common.util.SessionUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class SystemEntity {
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FIRST_INPUT_DTM", nullable = false, updatable = false)
    private LocalDateTime firstInputDtm;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_CHANGE_DTM", nullable = false)
    private LocalDateTime lastChangeDtm;

    @Size(max = 1)
    @Column(name = "DELETE_YN", nullable = true)
    private String deleteYn;

    @Size(max = 45)
    @Column(name = "FIRST_INPUT_USER", nullable = false)
    private String firstInputUser;

    @Size(max = 45)
    @Column(name = "LAST_CHANGE_USER", nullable = false)
    private String lastChangeUser;

    @PrePersist
    public void insertAt() {
//        String memberId = SessionUtil.getMemberId();
        String memberId;
        try {
            memberId = SessionUtil.getMemberId();
        } catch (Exception e) {
            memberId = "system"; // 세션 정보가 없는 경우 기본값으로 설정
        }
        this.deleteYn = StringUtils.hasText(this.deleteYn) ? this.deleteYn : "N";
        this.firstInputUser = StringUtils.hasText(memberId) ? memberId : "system";
        this.lastChangeUser = StringUtils.hasText(memberId) ? memberId : "system";
        this.firstInputDtm = this.firstInputDtm == null ? LocalDateTime.now() : this.firstInputDtm;
        this.lastChangeDtm = this.lastChangeDtm == null ? LocalDateTime.now() : this.lastChangeDtm;
    }

    @PreUpdate
    public void updateAt() {
//        String memberId = SessionUtil.getMemberId();
        String memberId;
        try {
            memberId = SessionUtil.getMemberId();
        } catch (Exception e) {
            memberId = "system"; // 세션 정보가 없는 경우 기본값으로 설정
        }
        this.lastChangeUser = StringUtils.hasText(memberId) ? memberId : "system";
        this.lastChangeDtm = this.lastChangeDtm == null ? LocalDateTime.now() : this.lastChangeDtm;
    }
}