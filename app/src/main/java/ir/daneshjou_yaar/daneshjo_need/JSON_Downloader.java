package ir.daneshjou_yaar.daneshjo_need;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class JSON_Downloader extends AsyncTask<String , Void , String>{
    private static final String TAG = "JSON_Downloader";
    private final onDownloadComplete mCallback;

    public interface onDownloadComplete {
        void onDownloadComplete(String data , DownloadStatus status);
    }

    private DownloadStatus mDownloadStatus;

    public JSON_Downloader(onDownloadComplete callback) {
        this.mDownloadStatus = DownloadStatus.IDLE;
        mCallback = callback;
    }

    public void runInSameThread ( String s ){
        Log.d(TAG, "runInSameThread: starts");

      //  onPostExecute(doInBackground(s));

        if (mCallback != null) {
//            String result = doInBackground(s);
//            mCallback.onDownloadComplete(result , mDownloadStatus);
        mCallback.onDownloadComplete(doInBackground(s) , mDownloadStatus);
        }
        Log.d(TAG, "runInSameThread: ends");
    }

    @Override
    protected void onPostExecute(String s) {
      //  Log.d(TAG, "onPostExecute: parameter = " + s);
      //  super.onPostExecute(s);
        if (mCallback != null){
            mCallback.onDownloadComplete(s , mDownloadStatus);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if (params == null){
            mDownloadStatus = DownloadStatus.NOT_INITIALISED;
            return null;
        }

        try {
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(params[0]);
            Log.d(TAG, "doInBackground: "+url.toString());

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code was "+ response);

            StringBuilder result = new StringBuilder();
            reader =  new BufferedReader(new InputStreamReader(connection.getInputStream()));

//            String line;
//            while (null != (line = reader.readLine())){
            for (String line = reader.readLine(); line != null ; line = reader.readLine()){
                result.append(line).append("\n");
            }

            Log.d(TAG, "doInBackground: "+result.toString());
            mDownloadStatus = DownloadStatus.OK;
            return result.toString();

        }catch (MalformedURLException e){
            Log.e(TAG, "doInBackground: INVALID url " + e.getMessage() );
        } catch (IOException e){
            Log.e(TAG, "doInBackground: IO Exception reading data : " + e.getMessage() );
        } catch (SecurityException e){
            Log.e(TAG, "doInBackground: Security exception :" + e.getMessage() );
        } finally {
            if (connection != null){
                connection.disconnect();
            }
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error closing Stream" + e.getMessage() );
                }
            }
        }

        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }





}






















