package com.livecard.front.domain.repository;
import com.livecard.front.domain.entity.CmnLivecardTemplateTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CmnLivecardTemplateTypeRepository extends JpaRepository<CmnLivecardTemplateTypeEntity, Long> {
}



//// LivecardTemplateRepository.java
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface LivecardTemplateRepository extends JpaRepository<LivecardTemplate, Long> {
//}