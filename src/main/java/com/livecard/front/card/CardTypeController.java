package com.livecard.front.card;

import com.livecard.front.card.service.CardTypeService;
import com.livecard.front.dto.member.LivecardTemplateDto;
import com.livecard.front.dto.member.LivecardTemplateTypeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cardTypes")
public class CardTypeController {
    @Autowired
    private final CardTypeService cardTypeService;


    @GetMapping
    public List<LivecardTemplateTypeDto> getAllCardTypes() {
        return cardTypeService.getAllCardTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivecardTemplateTypeDto> getCardTypeById(@PathVariable Long id) {
        return cardTypeService.getCardTypeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/templates")
    public List<LivecardTemplateDto> getTemplatesByCardTypeId(@PathVariable Long id) {
        return cardTypeService.getTemplatesByCardTypeId(id);
    }

}



