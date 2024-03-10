package chiwon.dailyUploadToBlog.controller;

import chiwon.dailyUploadToBlog.dto.CrolledItemInfo;
import chiwon.dailyUploadToBlog.dto.SearchConditionDto;
import chiwon.dailyUploadToBlog.service.CrollingService;

import chiwon.dailyUploadToBlog.service.ImgProcessingService;
import chiwon.dailyUploadToBlog.service.OcrService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class CrollingController {

    @Autowired
    private Environment env;
    private CrollingService crollservice = new CrollingService();
    private ImgProcessingService imgProcessingService = new ImgProcessingService();
    private OcrService ocrService = new OcrService();

    @PostMapping(value = "/crolling")
    public void crolling(@RequestBody SearchConditionDto crollDto) {
        String targetUrl = crollservice.convertUrl("https://www.coupang.com/np/search?q=", crollDto);
        List<String> itemList = new ArrayList<>();


        try {
            Document doc = Jsoup.connect(targetUrl)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
                    .header("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.5,en;q=0.3")
                    .timeout(50000)
                    .get();

            Elements contents = doc.select("ul.search-product-list li.search-product:not(.search-product__ad-badge) a");

            int cnt = 0;
            for (Element content : contents) {
                String href = "https://www.coupang.com" + content.attr("href");
                itemList.add(href);
                if (++cnt == 3)
                    break;
            }

            List<CrolledItemInfo> crolledItemInfoList = new ArrayList<>();
            for (int i = 0 ; i < cnt ; ++i) {
                crolledItemInfoList.add(crollservice.crollingItem(itemList.get(i)));
                imgProcessingService.downloadImage(crolledItemInfoList.get(i).getItemImgLink(),env.getProperty("thumbnail.no.processing") + String.valueOf(i) + ".jpg");
                crolledItemInfoList.get(i).setItemImgLink(env.getProperty("thumbnail.no.processing") + String.valueOf(i) + ".jpg");
                imgProcessingService.processingImage(crolledItemInfoList.get(i).getItemImgLink(),env.getProperty("thumbnail.processing") + String.valueOf(i) + "/", 0);

                int imgCnt = 0;
                String targetDir = env.getProperty("description.img") + String.valueOf(i);
                for (String imgLink : crolledItemInfoList.get(i).getDescriptionsImgLink()) {
                    imgProcessingService.downloadImage(imgLink, targetDir + "/origin/" + String.valueOf(imgCnt++) + ".jpg");
                }
                int resImgCnt = 0;
                for (int j = 0 ; j < imgCnt ; ++j) {
                    resImgCnt = imgProcessingService.processingImage(targetDir + "/origin/" + String.valueOf(j) + ".jpg", targetDir + "/result/", resImgCnt);
                }
                for (int j = 0 ; j < resImgCnt ; ++j) {
                    crolledItemInfoList.get(i).getDescriptionsLocalLink().add(targetDir + "/result/" + String.valueOf(j) + ".jpg");
                }
                ocrService.imageToText(crolledItemInfoList.get(i), env.getProperty("clova.ocr.apikey"), env.getProperty("clova.ocr.api.gateway"));

                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
