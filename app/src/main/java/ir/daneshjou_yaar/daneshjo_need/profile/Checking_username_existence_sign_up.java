package ir.daneshjou_yaar.daneshjo_need.profile;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import ir.daneshjou_yaar.daneshjo_need.DownloadStatus;
import ir.daneshjou_yaar.daneshjo_need.JSON_Downloader;

/**
 * Created by iqfarhad on 3/18/2018.
 */

public class Checking_username_existence_sign_up extends AsyncTask<String, Void , String> implements JSON_Downloader.onDownloadComplete {
    private static final String TAG = "Checking_username_exist";
    private static boolean existence;
    private String check;
    private String base_url;
    private String user_id_for_check;

    private final onDataAvailable mCallback;

    private boolean runningOnSameThread = false;

    interface onDataAvailable {
        void onDataAvailable(String data, DownloadStatus status);
    }

    public Checking_username_existence_sign_up(String base_url, String user_id_for_check, onDataAvailable callback) {
        Log.d(TAG, "Checking_username_existence_sign_up: starts");
        this.base_url = base_url;
        this.user_id_for_check = user_id_for_check;
        mCallback = callback;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: starts");

        if (mCallback != null){
            mCallback.onDataAvailable(check ,DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    void executeOnSameThread (String searchCriteria){
        Log.d(TAG, "executeOnSameThread: starts");
        runningOnSameThread = true;
        String destinationUri = createUri(searchCriteria , user_id_for_check );

        JSON_Downloader json_downloader = new JSON_Downloader(this);
        json_downloader.execute(destinationUri);
        Log.d(TAG, "executeOnSameThread: ends");
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");
        String destinationUri = createUri(params[0] , user_id_for_check);

        JSON_Downloader json_downloader = new JSON_Downloader(this);
        json_downloader.runInSameThread(destinationUri);
        Log.d(TAG, "doInBackground: ends");
        return check;
    }

    private String createUri (String searchCriteria , String user_id){
        Log.d(TAG, "createUri: starts");

        Uri uri = Uri.parse(base_url);

        //lazy load ro ejra konim ya ye motagheyer append konim be darkhastemon
        return Uri.parse(base_url).buildUpon()
                .appendQueryParameter("u_users" , user_id+"")
                .build().toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: starts status = "+ status);

        if (status == DownloadStatus.OK){

            try {
                JSONObject jsonData = new JSONObject(data);
                check = jsonData.get("existence").toString();
                Log.d(TAG, "onDownloadComplete: check ="+ check);
                if (check.equalsIgnoreCase("1"))
                    existence = true;
                else
                    existence = false;

            }catch (JSONException e){
                e.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing Json Data" + e.getMessage() );
                status = DownloadStatus.FAILED_OR_EMPTY;

            }
        }

        if (runningOnSameThread && mCallback != null){
            //now inform caller that processing is done - possibly returning null if there
            //was in error
            mCallback.onDataAvailable(check , status);
        }
        Log.d(TAG, "onDownloadComplete: ends");
    }

    public static boolean User_Exist(){
        return existence;
    }

}
