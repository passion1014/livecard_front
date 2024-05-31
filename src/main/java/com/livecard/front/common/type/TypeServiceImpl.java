package com.livecard.front.common.type;

import com.livecard.front.domain.entity.CmnTypeEntity;
import com.livecard.front.domain.repository.CmnTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TypeServiceImpl implements TypeService{
    private final CmnTypeRepository cmnTypeRepository;

    @Override
    public Optional<List<CmnTypeEntity>> searchActiveType(String type) {
        List<CmnTypeEntity> cmnTypeEntities = cmnTypeRepository.findByTypeTypeAndStatus(type,'Y');
        return Optional.of(cmnTypeEntities);
    }
}