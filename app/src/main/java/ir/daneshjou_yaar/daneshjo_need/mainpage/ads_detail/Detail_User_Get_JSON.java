package ir.daneshjou_yaar.daneshjo_need.mainpage.ads_detail;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.daneshjou_yaar.daneshjo_need.DownloadStatus;
import ir.daneshjou_yaar.daneshjo_need.JSON_Downloader;

/**
 * Created by iqfarhad on 3/13/2018.
 */

public class Detail_User_Get_JSON extends AsyncTask<String , Void , List<User_Model>> implements JSON_Downloader.onDownloadComplete {
    private static final String TAG = "Detail_User_Get_JSON";

    private static List<User_Model> mUser_models = null;
    private static String mBaseURL ;
    private static String mUser_id;

    private final onDataAvailable mCallback;
    private boolean runningOnSameThread = false;

    public interface onDataAvailable {
        void onDataAvailable(List<User_Model> data, DownloadStatus status);
    }

    public Detail_User_Get_JSON(String user_id, onDataAvailable callback , String baseURL) {
        Log.d(TAG, "Detail_User_Get_JSON: starts");
        mUser_id = user_id;
        mCallback = callback;
        mBaseURL = baseURL;
    }

    void executeOnSameThread (String searchCriteria){
        Log.d(TAG, "executeOnSameThread: starts");
        runningOnSameThread = true;
        String destinationUri = createUri(searchCriteria , mUser_id );

        JSON_Downloader json_downloader = new JSON_Downloader(this);
        json_downloader.execute(destinationUri);
        Log.d(TAG, "executeOnSameThread: ends");
    }

    @Override
    protected void onPostExecute(List<User_Model> user_models) {
        Log.d(TAG, "onPostExecute: starts");

        if (mCallback != null){
            mCallback.onDataAvailable(mUser_models ,DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected List<User_Model> doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");
        String destinationUri = createUri(params[0] , mUser_id);

        JSON_Downloader json_downloader = new JSON_Downloader(this);
        json_downloader.runInSameThread(destinationUri);
        Log.d(TAG, "doInBackground: ends");
        return mUser_models;
    }

    private String createUri (String searchCriteria , String user_id){
        Log.d(TAG, "createUri: starts");

        Uri uri = Uri.parse(mBaseURL);

        //lazy load ro ejra konim ya ye motagheyer append konim be darkhastemon
        return Uri.parse(mBaseURL).buildUpon()
                .appendQueryParameter("u_id" , user_id+"")
                .build().toString();
    }


    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: starts Status = "+ status);

        if (status == DownloadStatus.OK){
            mUser_models = new ArrayList<>();

            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("user");
                Log.d(TAG, "onDownloadComplete: "+itemsArray.toString());

                for (int i = 0; i<itemsArray.length(); i++){
                    String jsonCats = String.valueOf(itemsArray.getJSONObject(i));
                    Log.d(TAG, "onDownloadComplete: "+jsonCats);
                    JSONObject jsonCat = itemsArray.getJSONObject(i);
                    String user_id = jsonCat.getString("user_id");
                    Log.d(TAG, "onDownloadComplete: userid "+ user_id);
                    String name = jsonCat.getString("name");
                    String family = jsonCat.getString("family");
                    String phone = jsonCat.getString("phone");
                    String email = jsonCat.getString("email");
                    String telegram_id = jsonCat.getString("telegram_id");
                    String profile_pic = jsonCat.getString("profile_pic");



                    User_Model adObject = new User_Model( name , family , user_id , phone , email , telegram_id , profile_pic );
                    mUser_models.add(adObject);

                    Log.d(TAG, "onDownloadComplete: "+ adObject.toString());
                }
            }catch (JSONException e){
                e.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing Json Data" + e.getMessage() );
                status = DownloadStatus.FAILED_OR_EMPTY;
            }

        }

        if (runningOnSameThread && mCallback != null){
            //now inform caller that processing is done - possibly returning null if there
            //was in error
            mCallback.onDataAvailable(mUser_models , status);
        }
        Log.d(TAG, "onDownloadComplete: ends");
    }

    public static User_Model getUser(){
        return ((mUser_models != null) && (mUser_models.size() != 0) ? mUser_models.get(0) : null);
    }

}
