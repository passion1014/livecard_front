package com.livecard.front.member.service;

import com.livecard.front.domain.entity.MbrUserEntity;
import com.livecard.front.domain.repository.AstUserBalanceRepository;
import com.livecard.front.domain.repository.MbrUserRepository;
import com.livecard.front.domain.repository.OauthTokenRepository;
import com.livecard.front.dto.member.MemberBalanceDto;
import com.livecard.front.dto.member.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MbrUserRepository mbrUserRepository;
    private final AstUserBalanceRepository astUserBalanceRepository;
    private final OauthTokenRepository oauthTokenRepository;

    @Override
    public Long getMemberId(String socialId) {
        return mbrUserRepository.findBySocialId(socialId)
                .map(MbrUserEntity::getId).orElse(null);
    }

    @Override
    public Optional<MemberDto> getMemberDetailBySocial(String socialId) {
        Optional<MemberDto> memberDtoOptional = mbrUserRepository.findBySocialId(socialId).map(this::convertToDto);

        memberDtoOptional.ifPresent(dto -> {
            astUserBalanceRepository.findByMbrUserEntityId(dto.getId())
                    .ifPresent(astUserBalanceEntity -> {
                        MemberBalanceDto memberBalanceDto = MemberBalanceDto.builder()
                                .id(astUserBalanceEntity.getId())
                                .point(astUserBalanceEntity.getPoint())
                                .build();
                        dto.setMemberBalanceDto(memberBalanceDto);
                    });
        });

        return memberDtoOptional;
    }

    /**
     * 회원삭제
     * @param mbrUserId
     */
    @Transactional
    @Override
    public void deleteMember(Long mbrUserId) {
        // Refresh-token 삭제
        oauthTokenRepository.deleteByMbrUserId(mbrUserId);
        //TODO: 멤버 테이블과 관련된 테이블 삭제 구현필요
    }


    private MemberDto convertToDto(MbrUserEntity mbrUserEntity) {
        return MemberDto.builder()
                .id(mbrUserEntity.getId())
                .socialId(mbrUserEntity.getSocialId())
                .name(mbrUserEntity.getName())
                .providerCd(mbrUserEntity.getProviderCd())
                .role(mbrUserEntity.getRole())
                .build();
    }

}
