package chiwon.dailyUploadToBlog.service;

import chiwon.dailyUploadToBlog.dto.SearchConditionDto;
import org.springframework.stereotype.Service;

@Service
public class CrollingService {

    public String convertUrl(String originUrl, SearchConditionDto searchConditionDto) {

        String res = originUrl;
        res += "?q=" + searchConditionDto.getQ();
        res += "?minPrice=" + searchConditionDto.getMinPrice();
        res += "?maxPrice=" + searchConditionDto.getMaxPrice();
        return res;

    }

}
