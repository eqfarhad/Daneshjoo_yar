
package ir.daneshjou_yaar.news;

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
    @SerializedName("news")
    @Expose
    private List<News> news = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getTotolPage() {
        return totolPage;
    }

    public void setTotolPage(Integer totolPage) {
        this.totolPage = totolPage;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }

}
