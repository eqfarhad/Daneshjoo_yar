package ir.daneshjou_yaar.daneshjo_need.mainpage;

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
 * Created by iqfarhad on 2/6/2018.
 */

class Mainpage_Get_JSON_Data extends AsyncTask<String , Void , List<Advertise_Model>> implements JSON_Downloader.onDownloadComplete {
    private static final String TAG = "Mainpage_Get_JSON_Data";

    private List<Advertise_Model> mAdvertise_models = null;
    private String mBaseURL ;
    private int mPage;
    private boolean mMatchall;
    
    private final onDataAvailable mCallback;
    private boolean runningOnSameThread = false;

    interface onDataAvailable {
        void onDataAvailable(List<Advertise_Model> data, DownloadStatus status);
    }

    public Mainpage_Get_JSON_Data(onDataAvailable callback , String baseURL, int page, boolean matchall ) {
        Log.d(TAG, "Mainpage_Get_JSON_Data: starts");
        mBaseURL = baseURL;
        mPage = page;
        mMatchall = matchall;
        mCallback = callback;
    }

    void executeOnSameThread (String searchCriteria){
        Log.d(TAG, "executeOnSameThread: starts");
        runningOnSameThread = true;
        String destinationUri = createUri(searchCriteria , mPage , mMatchall);
        
        JSON_Downloader json_downloader = new JSON_Downloader(this);
        json_downloader.execute(destinationUri);
        Log.d(TAG, "executeOnSameThread: ends");
        
    }

    @Override
    protected void onPostExecute(List<Advertise_Model> advertise_models) {
        Log.d(TAG, "onPostExecute: starts");
        
        if (mCallback != null){
            mCallback.onDataAvailable(mAdvertise_models ,DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected List<Advertise_Model> doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");
        String destinationUri = createUri(params[0] , mPage , mMatchall);

        JSON_Downloader json_downloader = new JSON_Downloader(this);
        json_downloader.runInSameThread(destinationUri);
        Log.d(TAG, "doInBackground: ends");
        return mAdvertise_models;
    }

    private String createUri (String searchCriteria , int page , boolean matchAll){
        Log.d(TAG, "createUri: starts");

        Uri uri = Uri.parse(mBaseURL);


        //lazy load ro ejra konim
        return Uri.parse(mBaseURL).buildUpon()
                .appendQueryParameter("page" , page+"")
                .build().toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: starts Status = "+ status);

        if (status == DownloadStatus.OK){
            mAdvertise_models = new ArrayList<>();

            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("ads");

                for (int i = 0; i<itemsArray.length(); i++){
                    JSONObject jsonCat = itemsArray.getJSONObject(i);
                    String id = jsonCat.getString("id");
                    String user_id = jsonCat.getString("user_id");
                    String title = jsonCat.getString("title");
                    String intro = jsonCat.getString("intro");
                    String desc = jsonCat.getString("desc");
                    String image = jsonCat.getString("image");
                    String date = jsonCat.getString("date");
                    String price = jsonCat.getString("price");
                    String cat = jsonCat.getString("cat");


                    Advertise_Model adObject = new Advertise_Model(id , user_id,  title , intro , desc , image , date , price , cat);
                    mAdvertise_models.add(adObject);

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
            mCallback.onDataAvailable(mAdvertise_models , status);
        }
        Log.d(TAG, "onDownloadComplete: ends");
    }
}
