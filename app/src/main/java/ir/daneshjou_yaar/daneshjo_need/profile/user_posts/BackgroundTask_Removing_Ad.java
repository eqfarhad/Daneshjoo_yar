package ir.daneshjou_yaar.daneshjo_need.profile.user_posts;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by iqfarhad on 3/14/2018.
 */

public class BackgroundTask_Removing_Ad extends AsyncTask<String , Void , String> {
    private static final String TAG = "BackgroundTask_Removing";
    Context ctx;

    public BackgroundTask_Removing_Ad(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... params) {

        String reg_url = "http://eqtech.ir/remove_user_ad.php";
        String method= params[0];
        if (method.equalsIgnoreCase("removing_ad")){

            String user_id = params[1];
            String post_id = params[2];

            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream , "UTF-8"));

                String data = URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id ,"UTF-8")+"&"+
                        URLEncoder.encode("post_id","UTF-8")+"="+URLEncoder.encode(post_id ,"UTF-8");

                Log.d(TAG, "doInBackground: "+data);
                Log.d(TAG, "doInBackground: "+url.toString());


                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream IS =httpURLConnection.getInputStream();
                IS.close();

                return "Removing Success";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


        return null;
    }

    public BackgroundTask_Removing_Ad() {
        super();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "onPostExecute: Removed");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
