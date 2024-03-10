package chiwon.dailyUploadToBlog.service;

import chiwon.dailyUploadToBlog.dto.CrolledItemInfo;
import chiwon.dailyUploadToBlog.dto.SearchConditionDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class CrollingService {

    private SeleniumService seleniumService = new SeleniumService();

    public String convertUrl(String originUrl, SearchConditionDto searchConditionDto) {

        String query = "";
        try {
            searchConditionDto.setQ(URLEncoder.encode(searchConditionDto.getQ(), "UTF-8"));
            query += searchConditionDto.getQ();
            query += "&minPrice=" + searchConditionDto.getMinPrice();
            query += "&maxPrice=" + searchConditionDto.getMaxPrice();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return originUrl + query;
    }

    public CrolledItemInfo crollingItem(String itemUrl) {

        CrolledItemInfo crolledItemInfo = new CrolledItemInfo();
        crolledItemInfo.setDescriptionsImgLink(new ArrayList<>());
        crolledItemInfo.setDescriptionsLocalLink(new ArrayList<>());
        crolledItemInfo.setReviews(new ArrayList<>());
        crolledItemInfo.setItemUrl(itemUrl);
        crolledItemInfo.setDescription("");
        try {

            Document item = Jsoup.connect(itemUrl)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
                    .header("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.5,en;q=0.3")
                    .timeout(50000)
                    .get();

            //thumbnail
            Element thumbnail = item.selectFirst("img.prod-image__detail");
            crolledItemInfo.setItemImgLink(thumbnail.absUrl("src"));

            //description => title
            Element description = item.selectFirst("h2.prod-buy-header__title");
            crolledItemInfo.setDescription(description.text());

            seleniumService.crolledDescriptionImgWithSelenium(crolledItemInfo);
            seleniumService.crolledReviewWithSelenium(crolledItemInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return crolledItemInfo;
    }

}
