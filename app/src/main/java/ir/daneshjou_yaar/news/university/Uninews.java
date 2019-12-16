
package ir.daneshjou_yaar.news.university;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Uninews {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("news")
    @Expose
    private List<News_uni> news = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public List<News_uni> getNews() {
        return news;
    }

    public void setNews(List<News_uni> news) {
        this.news = news;
    }

}
