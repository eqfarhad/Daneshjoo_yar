package ir.daneshjou_yaar.daneshjo_need.mainpage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by iqfarhad on 4/14/2018.
 */

public interface Ads_Service {

    @GET("get_data.php")
    Call<GetDataFromWeb> GET_DATA_FROM_WEB_CALL(
            @Query("page") int pageIndex
    );

}
