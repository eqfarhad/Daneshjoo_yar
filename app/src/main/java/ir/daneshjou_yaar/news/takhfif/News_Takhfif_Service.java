package ir.daneshjou_yaar.news.takhfif;

import ir.daneshjou_yaar.news.GetDataFromWeb;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by iqfarhad on 4/14/2018.
 */

public interface News_Takhfif_Service {

    @GET("get_takhfif.php")
    Call<GetDataFromWeb> GET_DATA_FROM_WEB_CALL(
            @Query("page") int pageIndex
    );

}
