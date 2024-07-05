package com.livecard.front.card.service;

import com.livecard.front.dto.member.LivecardTemplateDto;
import com.livecard.front.dto.member.LivecardTemplateTypeDto;

import java.util.List;
import java.util.Optional;

public interface CardTypeService {
    List<LivecardTemplateTypeDto> getAllCardTypes();

    Optional<LivecardTemplateTypeDto> getCardTypeById(Long id);

    List<LivecardTemplateDto> getAllTemplates();

    Optional<LivecardTemplateDto> getTemplateById(Long id);

    List<LivecardTemplateDto> getTemplatesByCardTypeId(Long id);
}
