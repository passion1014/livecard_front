package com.livecard.front.common.type;

import com.livecard.front.domain.entity.CmnCommonCodeEntity;
import com.livecard.front.domain.repository.CmnCommonCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TypeServiceImpl implements TypeService{
    private final CmnCommonCodeRepository cmnCommonCodeRepository;

    @Override
    public Optional<List<CmnCommonCodeEntity>> searchActiveType(String type) {
//        List<CmnCommonCodeEntity> cmnTypeEntities = cmnCommonCodeRepository.findByTypeTypeAndStatus(type,'Y');
//        return Optional.of(cmnTypeEntities);
        return Optional.empty();
    }
}
