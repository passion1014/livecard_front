package com.livecard.front.domain.repository;

import com.livecard.front.domain.entity.AstUserBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AstUserBalanceRepository extends JpaRepository<AstUserBalanceEntity, Long> {
     Optional<AstUserBalanceEntity> findByMbrUserEntityId(Long userId);

}
