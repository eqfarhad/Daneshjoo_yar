package ir.daneshjou_yaar.news;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import ir.daneshjou_yaar.R;

import static ir.daneshjou_yaar.news.DrawerActivity.mDrawer;

/**
 * Created by iqfarhad on 3/27/2018.
 */

public class BackgroundTask_Register_News extends AsyncTask<String , Integer , String> {
    private static final String TAG = "BGTask_Register_News";

    Context ctx;
    private ProgressDialog pdia;

    public BackgroundTask_Register_News(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String reg_url = "http://eqtech.ir/register_news.php";
        String method= params[0];


        if (method.equalsIgnoreCase("register_news")){

            String title = params[1];
            String description = params[2];
            String phone = params[3];
            String category = params[4];
            String image = params[5];
            BufferedReader reader=null;
            try {

                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream , "UTF-8"));

                String data = URLEncoder.encode("title" , "UTF-8")+"="+URLEncoder.encode(title , "UTF-8")+"&"+
                        URLEncoder.encode("description" , "UTF-8")+"="+URLEncoder.encode(description , "UTF-8")+"&"+
                        URLEncoder.encode("phone" , "UTF-8")+"="+URLEncoder.encode(phone , "UTF-8")+"&"+
                        URLEncoder.encode("category" , "UTF-8")+"="+URLEncoder.encode(category , "UTF-8")+"&"+
                        URLEncoder.encode("image" , "UTF-8")+"="+URLEncoder.encode(image , "UTF-8");

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
        pdia.dismiss();
        //Toast.makeText(ctx, "خبر شما پس از تایید ثبت خواهد شد !", Toast.LENGTH_SHORT).show();
        new StyleableToast.Builder(ctx).text("خبر شما پس از تایید ثبت خواهد شد.").textColor(Color.WHITE).backgroundColor(ctx.getResources().getColor(R.color.green_sign_up)).cornerRadius(5).show();

        mDrawer.closeDrawer(Gravity.LEFT);
        //((Activity)ctx).finish();
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
