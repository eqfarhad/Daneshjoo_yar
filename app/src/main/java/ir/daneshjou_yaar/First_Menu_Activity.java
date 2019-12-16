package ir.daneshjou_yaar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.about_us.About_US_Activity;
import ir.daneshjou_yaar.daneshjo_need.Daneshjo_Need_Activity;
import ir.daneshjou_yaar.location_address.Location_Activity;
import ir.daneshjou_yaar.map.MapsActivity;
import ir.daneshjou_yaar.news.News_Mainpage;
import ir.daneshjou_yaar.notification.Config_Notification;
import ir.daneshjou_yaar.notification.utils.NotificationUtils;

import com.google.firebase.messaging.FirebaseMessaging;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class First_Menu_Activity extends AppCompatActivity {
    private static final String TAG = "First_Menu_Activity";

    private LinearLayout news;
    private LinearLayout daneshjo;
    private LinearLayout location;
    private LinearLayout map;
    private LinearLayout about;
    private LinearLayout exit;

    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_menu);

        news =(LinearLayout) findViewById(R.id.news);
        daneshjo =(LinearLayout) findViewById(R.id.daneshjo);
        location =(LinearLayout) findViewById(R.id.location);
        map =(LinearLayout) findViewById(R.id.map);
        about =(LinearLayout) findViewById(R.id.about);
        exit =(LinearLayout) findViewById(R.id.exit);

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                   // Toast.makeText(First_Menu_Activity.this, "اخبار", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(First_Menu_Activity.this , News_Mainpage.class);
                    startActivity(intent);
                }else{
                   // Toast.makeText(First_Menu_Activity.this, "اینترنت شما وصل نمی باشد !", Toast.LENGTH_SHORT).show();
                    new StyleableToast.Builder(First_Menu_Activity.this).text("اینترنت شما متصل نمی باشد !").textColor(Color.WHITE).backgroundColor(First_Menu_Activity.this.getResources().getColor(R.color.colorPrimaryDark)).cornerRadius(20).iconStart(R.drawable.baseline_wifi_off_white_48).show();

                }
            }
        });

        daneshjo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()){
                 //   Toast.makeText(First_Menu_Activity.this, "نیازمندی", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(First_Menu_Activity.this , Daneshjo_Need_Activity.class);
                    startActivity(intent);
                }else{
                    new StyleableToast.Builder(First_Menu_Activity.this).text("اینترنت شما متصل نمی باشد !").textColor(Color.WHITE).backgroundColor(First_Menu_Activity.this.getResources().getColor(R.color.colorPrimaryDark)).cornerRadius(20).iconStart(R.drawable.baseline_wifi_off_white_48).show();

                   // Toast.makeText(First_Menu_Activity.this, "اینترنت شما وصل نمی باشد !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(First_Menu_Activity.this, "اماکن", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(First_Menu_Activity.this , Location_Activity.class);
                startActivity(intent);

            }
        });
        
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(First_Menu_Activity.this, "نقشه سمنان", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(First_Menu_Activity.this , MapsActivity.class);
                startActivity(intent);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(First_Menu_Activity.this, "درباره ما", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(First_Menu_Activity.this , About_US_Activity.class);
                startActivity(intent);
            }
        });


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(First_Menu_Activity.this, "خروج", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config_Notification.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config_Notification.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config_Notification.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                   // Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };

        displayFirebaseRegId();
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config_Notification.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.d(TAG, "Firebase reg id: " + regId);

      //  if (!TextUtils.isEmpty(regId))
           // Toast.makeText(this, "Firebase Reg Id: " + regId, Toast.LENGTH_SHORT).show();
       // else
           // Toast.makeText(this, "Firebase Reg Id is not received yet!", Toast.LENGTH_SHORT).show();
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config_Notification.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config_Notification.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
