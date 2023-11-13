package com.dob.telegrambotformsocial.service.impl;

import com.dob.telegrambotformsocial.entity.DailyDomains;
import com.dob.telegrambotformsocial.repository.DailyDomainsRepository;
import com.dob.telegrambotformsocial.service.DailyDomainsService;
import com.dob.telegrambotformsocial.utils.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyDomainsServiceImpl implements DailyDomainsService {

    private final DailyDomainsRepository dailyDomainsRepository;
    private final JsonParser jsonParser;

    @Override
    public Long countDomains() {
        return dailyDomainsRepository.count();
    }

    @Override
    public void deleteDomains() {
        dailyDomainsRepository.deleteAll();
    }

    public void saveDomains(){
        List<String> domainnames = jsonParser.parseJsonArrayToObject(
                "https://backorder.ru/json/?order=desc&expired=1&by=hotness&page=1&items=50", "domainname");
        List<DailyDomains> dailyDomains = new ArrayList<>();
        for (String domainname : domainnames) {
            dailyDomains.add(DailyDomains.builder()
                    .domainName(domainname)
                    .build());
        }
    dailyDomainsRepository.saveAll(dailyDomains);
    }
    @Scheduled(cron = "0 0 8 * * ?")
    @Transactional
    public void deleteAndSaveDomains(){
        deleteDomains();
        saveDomains();
    }
}
