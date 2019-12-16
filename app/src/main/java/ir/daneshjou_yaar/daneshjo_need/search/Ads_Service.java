package ir.daneshjou_yaar.daneshjo_need.search;

import ir.daneshjou_yaar.daneshjo_need.mainpage.GetDataFromWeb;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by iqfarhad on 4/14/2018.
 */

public interface Ads_Service {

    @GET("ads_search.php")
    Call<GetDataFromWeb> GET_DATA_FROM_WEB_CALL(
            @Query("search_word") String searchword,
            @Query("page") int pageIndex
    );

}
