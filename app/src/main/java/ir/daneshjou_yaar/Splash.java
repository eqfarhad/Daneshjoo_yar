package ir.daneshjou_yaar;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import ir.daneshjou_yaar.R;

public class Splash extends AppCompatActivity {

    private static final String TAG = "SplashScreen";

    private boolean start = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (start){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent main = new Intent(Splash.this, First_Menu_Activity.class);
                    startActivity(main);
                    start = false;
                    finish();
                }
            }, 4000);
        }
    }


//----------------------------------------is online ro bordim to menu va makhsos goznie hayie ke internet mikhad------------------------
    /*public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }*/

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: called");
        super.onResume();

        /*if (isOnline()) {*/


        /*}else{
            Toast.makeText(this, "اینترنت وصل نمی باشد!", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "جهت بروزرسانی اطلاعات برنامه اینترنت مورد نیاز است.", Toast.LENGTH_LONG).show();
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        }*/

    }


}
