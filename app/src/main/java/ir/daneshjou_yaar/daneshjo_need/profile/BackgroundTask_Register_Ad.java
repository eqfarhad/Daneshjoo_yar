package ir.daneshjou_yaar.daneshjo_need.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import ir.daneshjou_yaar.R;

/**
 * Created by iqfarhad on 3/27/2018.
 */

public class BackgroundTask_Register_Ad extends AsyncTask<String , Integer , String> {
    private static final String TAG = "BackgroundTask_Register";

    Context ctx;
    private ProgressDialog pdia;

    public BackgroundTask_Register_Ad(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String reg_url = "http://eqtech.ir/register_ad.php";
        String method= params[0];

        if (method.equalsIgnoreCase("register_ad")){

            String username = params[1];
            String title = params[2];
            String description = params[3];
            String price = params[4];
            String category = params[5];
            String image = params[6];

            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream , "UTF-8"));

                String data = URLEncoder.encode("username" , "UTF-8")+"="+URLEncoder.encode(username , "UTF-8")+"&"+
                        URLEncoder.encode("title" , "UTF-8")+"="+URLEncoder.encode(title , "UTF-8")+"&"+
                        URLEncoder.encode("description" , "UTF-8")+"="+URLEncoder.encode(description , "UTF-8")+"&"+
                        URLEncoder.encode("price" , "UTF-8")+"="+URLEncoder.encode(price , "UTF-8")+"&"+
                        URLEncoder.encode("category" , "UTF-8")+"="+URLEncoder.encode(category , "UTF-8")+"&"+
                        URLEncoder.encode("image" , "UTF-8")+"="+URLEncoder.encode(image , "UTF-8");

                Log.d(TAG, "doInBackground: "+data);

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream IS =httpURLConnection.getInputStream();
                IS.close();

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
        pdia.dismiss();
        //Toast.makeText(ctx, "آگهی ثبت شد !", Toast.LENGTH_SHORT).show();
        new StyleableToast.Builder(ctx).text("آگهی شما ثبت گردید.").textColor(Color.WHITE).backgroundColor(ctx.getResources().getColor(R.color.green_sign_up)).cornerRadius(5).show();

        ((Activity)ctx).finish();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pdia = new ProgressDialog(ctx);
        pdia.setMessage("در حال بارگزاری...");
        pdia.setCancelable(false);
        pdia.setCanceledOnTouchOutside(false);
        pdia.show();
    }

}
