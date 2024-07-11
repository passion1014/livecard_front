package com.livecard.front.domain.repository;
import com.livecard.front.domain.entity.CmnLivecardTemplateEntity;
import com.livecard.front.domain.entity.CmnLivecardTemplateTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CmnLivecardTemplateRepository extends JpaRepository<CmnLivecardTemplateEntity, Long> {
}

