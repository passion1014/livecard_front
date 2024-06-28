package com.livecard.front.card.service;

import com.livecard.front.common.exception.SearchNotFoundException;
import com.livecard.front.domain.entity.CrdLivecardEntity;
import com.livecard.front.domain.entity.MbrUserProfileEntity;
import com.livecard.front.domain.repository.CrdLivecardRepository;
import com.livecard.front.dto.card.CardDto;
import com.livecard.front.dto.member.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CrdLivecardRepository crdLivecardRepository;


    /**
     * 카드 만들기
     * @param cardDto
     */
    @Override
    public void createCard(CardDto cardDto) {

        CrdLivecardEntity crdLivecardEntity = CrdLivecardEntity.builder()
                .content(cardDto.getContent())
                .description(cardDto.getDescription())
                .name(cardDto.getName())
                .build();

        crdLivecardRepository.save(crdLivecardEntity);

    }

    /**
     *
     * @param cardDto
     */
    @Override
    public void getCard(CardDto cardDto) {
        //TODO: 사용자id 검증
        Optional<CrdLivecardEntity> crdLivecardEntityOptional = crdLivecardRepository.findById(cardDto.getCrdLivecardId());
        //TODO: EXception 확인
        CrdLivecardEntity crdLivecardEntity = crdLivecardEntityOptional.orElseThrow(()-> new SearchNotFoundException("카드 정보를 찾을수 없습니다"));
    }
}
