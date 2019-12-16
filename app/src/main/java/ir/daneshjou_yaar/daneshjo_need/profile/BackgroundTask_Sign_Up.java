package ir.daneshjou_yaar.daneshjo_need.profile;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import ir.daneshjou_yaar.R;

/**
 * Created by iqfarhad on 3/14/2018.
 */

public class BackgroundTask_Sign_Up extends AsyncTask<String , Void , String> {
    private static final String TAG = "BackgroundTask_Sign_Up";

    Context ctx;

    public BackgroundTask_Sign_Up(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... params) {

        String reg_url = "http://eqtech.ir/register_user.php";
        String method= params[0];
        if (method.equalsIgnoreCase("register")){

            String name = params[1];
            String family = params[2];
            String username = params[3];
            String password = params[4];
            String email = params[5];
            String telegram = params[6];
            String phone = params[7];

            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream , "UTF-8"));

                String data = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name ,"UTF-8")+"&"+
                        URLEncoder.encode("family","UTF-8")+"="+URLEncoder.encode(family ,"UTF-8")+"&"+
                        URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username ,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password ,"UTF-8")+"&"+
                        URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email ,"UTF-8")+"&"+
                        URLEncoder.encode("telegram","UTF-8")+"="+URLEncoder.encode(telegram ,"UTF-8")+"&"+
                        URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(phone ,"UTF-8");

                Log.d(TAG, "doInBackground: "+data);


                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream IS =httpURLConnection.getInputStream();
                IS.close();

                return "Registration Success";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


        return null;
    }

    public BackgroundTask_Sign_Up() {
        super();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        //Toast.makeText(ctx, "ثبت نام انجام شد :)", Toast.LENGTH_SHORT).show();
        new StyleableToast.Builder(ctx).text("ثبت نام با موفقیت انجام شد :)").textColor(Color.WHITE).backgroundColor(ctx.getResources().getColor(R.color.green_sign_up)).cornerRadius(5).show();

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
