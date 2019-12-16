package ir.daneshjou_yaar.news.university;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by iqfarhad on 4/14/2018.
 */

public interface News_Uni_Service {

    @GET("parse_uni_json.php")
    Call<Uninews> GET_DATA_FROM_WEB_CALL(
    );

}
