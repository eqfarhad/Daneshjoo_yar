
package ir.daneshjou_yaar.daneshjo_need.mainpage;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetDataFromWeb {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("totol_page")
    @Expose
    private Integer totolPage;
    @SerializedName("current_page")
    @Expose
    private Integer currentPage;
    @SerializedName("ads")
    @Expose
    private List<Ad> ads = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public GetDataFromWeb withSuccess(Integer success) {
        this.success = success;
        return this;
    }

    public  Integer getTotolPage() {
        return totolPage;
    }

    public void setTotolPage(Integer totolPage) {
        this.totolPage = totolPage;
    }

    public GetDataFromWeb withTotolPage(Integer totolPage) {
        this.totolPage = totolPage;
        return this;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public GetDataFromWeb withCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public List<Ad> getAds() {
        return ads;
    }

    public void setAds(List<Ad> ads) {
        this.ads = ads;
    }

    public GetDataFromWeb withAds(List<Ad> ads) {
        this.ads = ads;
        return this;
    }

}
