package com.livecard.front.domain.repository;

import com.livecard.front.domain.entity.CrdLivecardEntity;
import com.livecard.front.domain.entity.MbrUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrdLivecardRepository extends JpaRepository<CrdLivecardEntity, Long> {

}
