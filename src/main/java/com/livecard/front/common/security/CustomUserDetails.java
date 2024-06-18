package com.livecard.front.common.security;

import com.livecard.front.dto.common.AttachDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Schema(name = "MemberDto", description = "UserDetails 구현한 사용자상세정보")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {
    @Schema(description = "사용자PK")
    private Long id;

    @Schema(description = "사용자ID")
    private String memberId;

    @Schema(description = "사용자명")
    private String memberName;

//    @Schema(description = "사용자비밀번호")
//    private String memberPass;

    @Schema(description = "사용자만료여부")
    private Character memberExpiredYn;

    @Schema(description = "사용자만료일시")
    private LocalDateTime memberExpiryDate;

    @Schema(description = "제공자")
    private String providerCd;

    @Schema(description = "프로필이미지")
    private String profileImg; // 프로필 이미지

    @Schema(description = "권한")
    private List<GrantedAuthority> grantedAuthorities;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return memberId;
    }

    @Schema(description = "첨부파일목록")
    private AttachDto attachDto;

    public CustomUserDetails(String memberId, List<GrantedAuthority> grantedAuthorities) {
        this.memberId = memberId;
        this.memberExpiredYn = memberExpiredYn;
    }

    @Override
    public boolean isAccountNonExpired() {
        if (memberExpiryDate == null) {
            return true;
        }

        return memberExpiryDate.isAfter(LocalDateTime.now());
    }

    @Override
    public boolean isAccountNonLocked() {
        if (memberExpiredYn == null) {
            return true;
        }
        return !memberExpiredYn.equals('Y');
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isAccountNonExpired();
    }

}
