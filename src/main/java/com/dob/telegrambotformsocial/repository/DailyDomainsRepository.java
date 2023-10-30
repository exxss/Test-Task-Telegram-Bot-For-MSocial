package com.dob.telegrambotformsocial.repository;

import com.dob.telegrambotformsocial.entity.DailyDomains;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyDomainsRepository extends JpaRepository<DailyDomains,Long> {
    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) FROM daily_domains")
    Long countAllByDomainName();
}