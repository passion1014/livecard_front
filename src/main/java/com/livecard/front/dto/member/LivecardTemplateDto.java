package com.livecard.front.dto.member;

import com.livecard.front.domain.entity.CmnLivecardTemplateEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LivecardTemplateDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String description;
    private String content;
    private String useYn;
    private String regId;
    private String regDt;
    private String modId;
    private String modDt;
    private LivecardTemplateTypeDto livecardTemplateTypeDto;

    public static LivecardTemplateDto fromEntity(CmnLivecardTemplateEntity cmnLivecardTemplateEntity) {
        return null;
    }
}
