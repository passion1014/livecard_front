package com.livecard.front.dto.member;

import com.livecard.front.domain.entity.MbrUserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberBalanceDto implements Serializable {
    private Long id;
    private Long point;
}
