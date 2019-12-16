package ir.daneshjou_yaar.news;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by iqfarhad on 4/14/2018.
 */

public interface News_Service {

    @GET("get_news.php")
    Call<GetDataFromWeb> GET_DATA_FROM_WEB_CALL(
            @Query("page") int pageIndex
    );

}
