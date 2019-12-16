package ir.daneshjou_yaar.daneshjo_need.mainpage.ads_detail;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by iqfarhad on 3/27/2018.
 */

public class BackgroundTask_Increase_counter_ad extends AsyncTask<String , Integer , String> {
    private static final String TAG = "BackgroundTask_Increase";
    Context ctx;

    public BackgroundTask_Increase_counter_ad(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String reg_url = "http://eqtech.ir/register_ad.php";
        String method= params[0];


        if (method.equalsIgnoreCase("counter")){

            String post_id = params[1];

            BufferedReader reader=null;
            try {

                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream , "UTF-8"));

                String data = URLEncoder.encode("postid_counter" , "UTF-8")+"="+URLEncoder.encode(post_id , "UTF-8");

                Log.d(TAG, "doInBackground: "+data);

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line + "\n");
                }


                Log.d(TAG, "doInBackground: "+sb.toString() );

                InputStream IS =httpURLConnection.getInputStream();
                IS.close();

                // Get the server response





                return "Registration Success";

            }catch (Exception e){
                Log.d(TAG, "doInBackground: " );
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: ad resgistered");

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

}
