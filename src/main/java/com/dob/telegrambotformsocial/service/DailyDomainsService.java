package com.dob.telegrambotformsocial.service;

import com.dob.telegrambotformsocial.entity.DailyDomains;
import com.dob.telegrambotformsocial.repository.DailyDomainsRepository;
import com.dob.telegrambotformsocial.utils.JsonParser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DailyDomainsService {
    private final DailyDomainsRepository dailyDomainsRepository;
    private final JsonParser jsonParser;

    public DailyDomainsService(DailyDomainsRepository dailyDomainsRepository, JsonParser jsonParser) {
        this.dailyDomainsRepository = dailyDomainsRepository;
        this.jsonParser = jsonParser;
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

    public Long countDomains(){
      return dailyDomainsRepository.countAllByDomainName();
    }

    public void deleteDomains(){
        dailyDomainsRepository.deleteAll();
    }

}
