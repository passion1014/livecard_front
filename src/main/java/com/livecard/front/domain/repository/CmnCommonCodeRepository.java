package com.livecard.front.domain.repository;

import com.livecard.front.domain.entity.CmnCommonCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CmnCommonCodeRepository extends JpaRepository<CmnCommonCodeEntity, Long> {
//    Optional<CmnCommonCodeEntity> findByTypeTypeAndCode(String typeType, String code);
//    List<CmnCommonCodeEntity> findByTypeTypeAndStatus(String typeType, Character status);
}
