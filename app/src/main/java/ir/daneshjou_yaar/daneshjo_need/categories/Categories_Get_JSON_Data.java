package ir.daneshjou_yaar.daneshjo_need.categories;

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

class Categories_Get_JSON_Data extends AsyncTask<String , Void , List<Category_Model>> implements JSON_Downloader.onDownloadComplete {
    private static final String TAG = "Categories_Get_JSON";

    private List<Category_Model> mCategory_list = null;
    private String mBaseURL ;
    private String mLanguege;
    private boolean mMatchall;
    
    private final onDataAvailable mCallback;
    private boolean runningOnSameThread = false;

    interface onDataAvailable {
        void onDataAvailable(List<Category_Model> data , DownloadStatus status);
    }

    public Categories_Get_JSON_Data(onDataAvailable callback , String baseURL, String languege, boolean matchall ) {
        Log.d(TAG, "Categories_Get_JSON_Data: called");
        mBaseURL = baseURL;
        mLanguege = languege;
        mMatchall = matchall;
        mCallback = callback;
    }

    void executeOnSameThread (String searchCriteria){
        Log.d(TAG, "executeOnSameThread: starts");
        runningOnSameThread = true;
        String destinationUri = createUri(searchCriteria , mLanguege , mMatchall);
        
        JSON_Downloader json_downloader = new JSON_Downloader(this);
        json_downloader.execute(destinationUri);
        Log.d(TAG, "executeOnSameThread: ends");
        
    }

    @Override
    protected void onPostExecute(List<Category_Model> category_models) {
        Log.d(TAG, "onPostExecute: starts");
        
        if (mCallback != null){
            mCallback.onDataAvailable(mCategory_list ,DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected List<Category_Model> doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");
        String destinationUri = createUri(params[0] , mLanguege , mMatchall);

        JSON_Downloader json_downloader = new JSON_Downloader(this);
        json_downloader.runInSameThread(destinationUri);
        Log.d(TAG, "doInBackground: ends");
        return mCategory_list;
    }

    private String createUri (String searchCriteria , String lang , boolean matchAll){
        Log.d(TAG, "createUri: starts");

        Uri uri = Uri.parse(mBaseURL);

        Log.d(TAG, "createUri: " + Uri.parse(mBaseURL).buildUpon()
                .build().toString() );

        //inja bayad lazy load o page=1 ro ejra konim
        return Uri.parse(mBaseURL).buildUpon()
                .build().toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: starts Status = "+ status);

        if (status == DownloadStatus.OK){
            mCategory_list = new ArrayList<>();

            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("cat");

                for (int i = 0; i<itemsArray.length(); i++){
                    JSONObject jsonCat = itemsArray.getJSONObject(i);
                    String name = jsonCat.getString("name");
                    String id = jsonCat.getString("id");
                    String amount = jsonCat.getString("amount");
                    String image = jsonCat.getString("image");

                    Category_Model catObject = new Category_Model(name , id , amount , image);
                    mCategory_list.add(catObject);

                    Log.d(TAG, "onDownloadComplete: "+ catObject.toString());
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
            mCallback.onDataAvailable(mCategory_list , status);
        }
        Log.d(TAG, "onDownloadComplete: ends");
    }
}
