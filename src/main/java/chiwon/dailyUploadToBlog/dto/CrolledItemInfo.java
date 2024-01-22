package chiwon.dailyUploadToBlog.dto;

import java.util.List;

public class CrolledItemInfo {
    private String itemUrl;
    private String itemImgLink;
    private String description; // 필수표기정보 => 제목에서 긁어오기
    private List<String> descriptionsImgLink; // 이미지 링크
    private List<String> Reviews;

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public String getItemImgLink() {
        return itemImgLink;
    }

    public void setItemImgLink(String itemImgLink) {
        this.itemImgLink = itemImgLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getDescriptionsImgLink() {
        return descriptionsImgLink;
    }

    public void setDescriptionsImgLink(List<String> descriptionsImgLink) {
        this.descriptionsImgLink = descriptionsImgLink;
    }

    public List<String> getReviews() {
        return Reviews;
    }

    public void setReviews(List<String> reviews) {
        Reviews = reviews;
    }
}

