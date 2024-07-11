package com.livecard.front.card.service;

import com.livecard.front.domain.repository.CmnLivecardTemplateRepository;
import com.livecard.front.domain.repository.CmnLivecardTemplateTypeRepository;
import com.livecard.front.dto.member.LivecardTemplateDto;
import com.livecard.front.dto.member.LivecardTemplateTypeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardTypeServiceImpl implements CardTypeService {

    private final CmnLivecardTemplateTypeRepository cmnLivecardTemplateTypeRepository;
    private final CmnLivecardTemplateRepository cmnLivecardTemplateRepository;


    @Override
    public List<LivecardTemplateTypeDto> getAllCardTypes() {
        return cmnLivecardTemplateTypeRepository.findAll().stream()
                .map(LivecardTemplateTypeDto::fromEntity)
                .toList();
    }

    @Override
    public Optional<LivecardTemplateTypeDto> getCardTypeById(Long id) {
        return cmnLivecardTemplateTypeRepository.findById(id).map(LivecardTemplateTypeDto::fromEntity);
    }

    @Override
    public List<LivecardTemplateDto> getAllTemplates() {
        return cmnLivecardTemplateRepository.findAll().stream()
                .map(LivecardTemplateDto::fromEntity)
                .toList();
    }

    @Override
    public Optional<LivecardTemplateDto> getTemplateById(Long id) {
        return cmnLivecardTemplateRepository.findById(id).map(LivecardTemplateDto::fromEntity);
    }

    @Override
    public List<LivecardTemplateDto> getTemplatesByCardTypeId(Long id) {
        return null;
    }

//    public List<LivecardTemplateDto> getTemplatesByCardTypeId(Long cardTypeId) {
//        Optional<CmnLivecardTemplateTypeEntity> cardType = cmnLivecardTemplateTypeRepository.findById(cardTypeId);
//        if (cardType.isPresent()) {
//            return cmnLivecardTemplateRepository.findAll().stream()
//                    .filter(template -> template.getTemplateType().getId().equals(cardTypeId))
//                    .toList();
//        }
//        return List.of();
//    }
}
