package com.livecard.front.common.type;

import com.livecard.front.domain.entity.CmnCommonCodeEntity;

import java.util.List;
import java.util.Optional;

public interface TypeService {

    Optional<List<CmnCommonCodeEntity>> searchActiveType(String type);
}
