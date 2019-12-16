package ir.daneshjou_yaar.daneshjo_need.categories.Category_Choosed;

import ir.daneshjou_yaar.daneshjo_need.mainpage.GetDataFromWeb;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by iqfarhad on 4/14/2018.
 */

public interface Category_Ads_Service {

    @GET("get_data.php")
    Call<GetDataFromWeb> GET_DATA_FROM_WEB_CALL(
            @Query("cat") int catindex,
            @Query("page") int pageindex

    );

}
