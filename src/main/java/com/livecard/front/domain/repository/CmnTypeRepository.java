package com.livecard.front.domain.repository;

import com.livecard.front.domain.entity.CmnCommonCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CmnTypeRepository extends JpaRepository<CmnCommonCodeEntity, Long> {
    Optional<CmnCommonCodeEntity> findByTypeTypeAndCode(String typeType, String code);
    List<CmnCommonCodeEntity> findByTypeTypeAndStatus(String typeType, Character status);
}
