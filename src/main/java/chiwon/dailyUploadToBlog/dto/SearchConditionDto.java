package chiwon.dailyUploadToBlog.dto;

public class SearchConditionDto {
    private String q;
    private int minPrice;
    private int maxPrice;

    public SearchConditionDto(String q, int minPrice, int maxPrice) {
        this.q = q;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }
}
