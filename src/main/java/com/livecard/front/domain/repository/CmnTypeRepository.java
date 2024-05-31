package com.livecard.front.domain.repository;

import com.livecard.front.domain.entity.CmnTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CmnTypeRepository extends JpaRepository<CmnTypeEntity, Long> {
    Optional<CmnTypeEntity> findByTypeTypeAndCode(String typeType, String code);
    List<CmnTypeEntity> findByTypeTypeAndStatus(String typeType, Character status);
}
