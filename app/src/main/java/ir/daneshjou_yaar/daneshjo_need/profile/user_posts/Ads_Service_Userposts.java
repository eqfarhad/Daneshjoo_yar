package ir.daneshjou_yaar.daneshjo_need.profile.user_posts;

import ir.daneshjou_yaar.daneshjo_need.mainpage.GetDataFromWeb;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by iqfarhad on 4/14/2018.
 */

public interface Ads_Service_Userposts {

    @GET("get_data_for_user.php")
    Call<GetDataFromWeb> GET_DATA_FROM_WEB_CALL(
            @Query("id") String userID,
            @Query("page") int pageIndex
    );

}
