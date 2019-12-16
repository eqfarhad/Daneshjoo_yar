package ir.daneshjou_yaar.daneshjo_need.profile;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import ir.daneshjou_yaar.daneshjo_need.DownloadStatus;
import ir.daneshjou_yaar.daneshjo_need.JSON_Downloader;

/**
 * Created by iqfarhad on 3/19/2018.
 */

public class Getting_user_hashed_pass extends AsyncTask<String , Void , String> implements JSON_Downloader.onDownloadComplete{
    private static final String TAG = "Getting_user_hashed_pas";
    private String base_url;
    private String user_id;
    private String user_stored_hashed_pass;
    private String success;


    private final onDataAvailable mCallback;
    private boolean runningOnSameThread = false;


    public Getting_user_hashed_pass(String base_url, String user_id, onDataAvailable callback) {
        Log.d(TAG, "Getting_user_hashed_pass: starts");
        this.base_url = base_url;
        this.user_id = user_id;
        mCallback = callback;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: starts");

        if (mCallback != null){
            mCallback.onDataAvailable(user_stored_hashed_pass ,DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    void executeOnSameThread (String searchCriteria){
        Log.d(TAG, "executeOnSameThread: starts");
        runningOnSameThread = true;
        String destinationUri = createUri(searchCriteria , user_id );

        JSON_Downloader json_downloader = new JSON_Downloader(this);
        json_downloader.execute(destinationUri);
        Log.d(TAG, "executeOnSameThread: ends");
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: starts status = "+ status);

        if (status == DownloadStatus.OK){

            try {

                JSONObject jsonData = new JSONObject(data);
                success = jsonData.get("success").toString();

                if (success.equalsIgnoreCase("1")){
                    user_stored_hashed_pass = jsonData.get("pass").toString();
                    Log.d(TAG, "onDownloadComplete: check ="+ user_stored_hashed_pass);
                }
                else
                {
                    user_stored_hashed_pass = "Not_Found";
                    success = "Not_Found";
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
            mCallback.onDataAvailable(user_stored_hashed_pass , status);
        }
        Log.d(TAG, "onDownloadComplete: ends");

    }


    interface onDataAvailable {
        void onDataAvailable(String data, DownloadStatus status);
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");
        String destinationUri = createUri(params[0] , user_id);

        JSON_Downloader json_downloader = new JSON_Downloader(this);
        json_downloader.runInSameThread(destinationUri);
        Log.d(TAG, "doInBackground: ends");
        return user_stored_hashed_pass;
    }

    private String createUri (String searchCriteria , String user_id){
        Log.d(TAG, "createUri: starts");

        Uri uri = Uri.parse(base_url);

        //lazy load ro ejra konim ya ye motagheyer append konim be darkhastemon
        return Uri.parse(base_url).buildUpon()
                .appendQueryParameter("user_id" , user_id+"")
                .build().toString();
    }

    public String getPass (){
        return user_stored_hashed_pass;
    }
}
