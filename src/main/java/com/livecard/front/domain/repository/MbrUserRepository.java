package com.livecard.front.domain.repository;

import com.livecard.front.domain.entity.MbrUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MbrUserRepository extends JpaRepository<MbrUserEntity, Long> {
    Optional<MbrUserEntity> findBySocialId(String socialId);
}
