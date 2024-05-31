package com.livecard.front.common.type;

import com.livecard.front.domain.entity.CmnTypeEntity;

import java.util.List;
import java.util.Optional;

public interface TypeService {

    Optional<List<CmnTypeEntity>> searchActiveType(String type);
}
