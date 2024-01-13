package chiwon.dailyUploadToBlog.controller;

import chiwon.dailyUploadToBlog.dto.SearchConditionDto;
import chiwon.dailyUploadToBlog.service.CrollingService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.*;


@RestController
public class CrollingController {

    private CrollingService crollservice = new CrollingService();

    @PostMapping(value = "/crolling")
    public void crolling(@RequestBody SearchConditionDto crollDto) {
        String targetUrl = crollservice.convertUrl("https://www.coupang.com/np/search/", crollDto);

        try {
            Document doc = Jsoup.connect(targetUrl).get();
            //Elements 크롤링 진행하기. 광고 제외

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
