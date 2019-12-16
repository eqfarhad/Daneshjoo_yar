package ir.daneshjou_yaar.daneshjo_need.mainpage.ads_detail;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;


import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.List;

import ir.daneshjou_yaar.Custom_Text_View;
import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.daneshjo_need.DownloadStatus;
import ir.daneshjou_yaar.daneshjo_need.mainpage.Ad;

import ir.daneshjou_yaar.daneshjo_need.profile.user_posts.BackgroundTask_Removing_Ad;

public class Mainpage_Activity_Detail extends AppCompatActivity implements Detail_User_Get_JSON.onDataAvailable {
    private static final String TAG = "Mainpage_Activity_Detai";

    private static final String EXTRA_IMAGE = "ir.eqtech.daneshjou_yaar.extraImage";
    private static final String EXTRA_TITLE = "ir.eqtech.daneshjou_yaar.extraTitle";
    private static final String EXTRA_DESCRIPTION = "ir.eqtech.daneshjou_yaar.extraDescription";
    private static final String EXTRA_PRICE = "ir.eqtech.daneshjou_yaar.extraPrice";
    private static final String EXTRA_TIME = "ir.eqtech.daneshjou_yaar.extraTime";
    private static final String EXTRA_USER_ID = "extraUser_id";
    private static final String EXTRA_AD_ID = "ir.eqtech.daneshjou_yaar.extraAD_id";
    private static final String EXTRA_REMOVE = "ir.eqtech.daneshjou_yaar.extraUser_id";

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private LinearLayout telegram;
    private LinearLayout phone;
    private String itemUser_id;
    private String itemAD_id;
    private Boolean remove_item;

    public static void navigate(FragmentActivity activity, byte[] byteArray , View transitionImage, Ad viewModel ,Boolean remove) {
        Intent intent = new Intent(activity, Mainpage_Activity_Detail.class);
        intent.putExtra(EXTRA_IMAGE, viewModel.getImage());
        intent.putExtra(EXTRA_TITLE, viewModel.getTitle());
        intent.putExtra(EXTRA_DESCRIPTION, viewModel.getDesc());
        intent.putExtra(EXTRA_PRICE, viewModel.getPrice());
        intent.putExtra(EXTRA_TIME, viewModel.getDate());
        intent.putExtra(EXTRA_USER_ID, viewModel.getUserId());
        intent.putExtra(EXTRA_AD_ID , viewModel.getId());
        intent.putExtra(EXTRA_REMOVE , remove);
        intent.putExtra("Image", byteArray);

        Log.d(TAG, "navigate: "+ viewModel.getUserId() +"-"+ viewModel.getId());


        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_IMAGE);
        //ActivityOptionsCompat options_ = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_TITLE);
        ActivityCompat.startActivityForResult(activity, intent, 100 , options.toBundle());
     //   progressBar.setVisibility(View.GONE);

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.activity_mainpage_detail);
        setTranslucentStatusBar(getWindow());

        ViewCompat.setTransitionName(findViewById(R.id.mainpage_detail_activity_appbar), EXTRA_IMAGE);
        supportPostponeEnterTransition();


        setSupportActionBar((Toolbar) findViewById(R.id.mainpage_detail_activity_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toolbar toolbar = findViewById(R.id.mainpage_detail_activity_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        String itemTitle = getIntent().getStringExtra(EXTRA_TITLE);
        String itemDescription = getIntent().getStringExtra(EXTRA_DESCRIPTION);
        String itemPrice = getIntent().getStringExtra(EXTRA_PRICE);
        String itemTime = getIntent().getStringExtra(EXTRA_TIME);
        itemUser_id = getIntent().getStringExtra(EXTRA_USER_ID);
        itemAD_id = getIntent().getStringExtra(EXTRA_AD_ID);
        remove_item = getIntent().getBooleanExtra(EXTRA_REMOVE , false);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(itemTitle);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        final ImageView image = (ImageView) findViewById(R.id.mainpage_detail_activity_image);

        byte[] byteArray = getIntent().getByteArrayExtra("Image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        //Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("BitmapImage");
        image.setImageBitmap(bmp);

        try {
            Palette.from(bmp).generate(new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette palette) {
                    applyPalette(palette);
                }
            });

        }catch (Exception e){
            Log.d(TAG, "onCreate: "+e);
        }


        //ravesh ziro hazf kardim be jash az balie az hamoon avalie ke load shode mifrestim invar

        /*Picasso.with(this).load(getIntent().getStringExtra(EXTRA_IMAGE)).fit().into(image, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        applyPalette(palette);
                    }
                });


            }

            @Override
            public void onError() {

            }
        });
*/


//        findViewById(R.id.mainpage_recycle_loading_detail).setVisibility(View.INVISIBLE);

        Custom_Text_View removing = (Custom_Text_View) findViewById(R.id.mainpage_detail_activity_removing);

        if (remove_item){
            removing.setVisibility(View.VISIBLE);
        }
        removing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog diaBox = AskOption();
                diaBox.show();


            }
        });

        TextView title = (TextView) findViewById(R.id.mainpage_detail_activity_title);
        title.setText(itemTitle);

        Custom_Text_View description = (Custom_Text_View) findViewById(R.id.mainpage_detail_activity_description);
        description.setText("توضیحات : " + itemDescription);

        Custom_Text_View price = (Custom_Text_View) findViewById(R.id.mainpage_detail_activity_price);
        price.setText("قیمت : " + itemPrice + " تومان ");

        Custom_Text_View time = (Custom_Text_View) findViewById(R.id.mainpage_detail_activity_time);
        time.setText("زمان انتشار :" + itemTime);

        telegram = (LinearLayout) findViewById(R.id.telegram_linear);
        phone = (LinearLayout) findViewById(R.id.phone_linear);

        Detail_User_Get_JSON categoriesGet_json_data = new Detail_User_Get_JSON(itemUser_id, this, "http://eqtech.ir/get_user.php");
        // categoriesGet_json_data.executeOnSameThread(null);
        categoriesGet_json_data.execute("");



        //increasing counter for post
        String method = "counter";
        BackgroundTask_Increase_counter_ad backgroundTask_increase_counter_ad = new BackgroundTask_Increase_counter_ad(Mainpage_Activity_Detail.this);
        backgroundTask_increase_counter_ad.execute( method , itemAD_id);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }


    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
        int primary = getResources().getColor(R.color.colorPrimary);
        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
       // updateBackground((FloatingActionButton) findViewById(R.id.fab), palette);
        supportStartPostponedEnterTransition();
    }

    private void updateBackground(FloatingActionButton fab, Palette palette) {
        int lightVibrantColor = palette.getLightVibrantColor(getResources().getColor(android.R.color.white));
        int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.colorAccent));

        fab.setRippleColor(lightVibrantColor);
        fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
    }

    @Override
    public void onDataAvailable(final List<User_Model> data, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable : starts");

        if (status == DownloadStatus.OK) {

            telegram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("http://telegram.me/" + Detail_User_Get_JSON.getUser().getTelegram_id()));
                    Log.d(TAG, "onClick: " + Detail_User_Get_JSON.getUser().getTelegram_id());
                    Log.d(TAG, "onClick: " + Uri.parse("http://telegram.me/" + Detail_User_Get_JSON.getUser().getTelegram_id()));
                    final String appName = "org.telegram.messenger";
                    try {
                        if (isAppAvailable(Mainpage_Activity_Detail.this, appName))
                            i.setPackage(appName);
                        else{
                            new StyleableToast.Builder(Mainpage_Activity_Detail.this).text("تلگرام بر روی گوشی شما نصب نمی باشد!").textColor(Color.WHITE).backgroundColor(Mainpage_Activity_Detail.this.getResources().getColor(R.color.red_map)).cornerRadius(5).show();
                            //Toast.makeText(Mainpage_Activity_Detail.this, "Telegram is Not Installed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "onClick: " + e.toString());
                    }

                    if (Detail_User_Get_JSON.getUser().getTelegram_id().equalsIgnoreCase("") || Detail_User_Get_JSON.getUser().getTelegram_id().isEmpty()) {
                        new StyleableToast.Builder(Mainpage_Activity_Detail.this).text("کاربر تلگرام ندارد").textColor(Color.WHITE).backgroundColor(Mainpage_Activity_Detail.this.getResources().getColor(R.color.colorPrimary)).cornerRadius(5).show();
                        //Toast.makeText(Mainpage_Activity_Detail.this, "کاربر تلگرام ندارد", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Mainpage_Activity_Detail.this.startActivity(i);
                    }
                }
            });

            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Detail_User_Get_JSON.getUser().getPhone()));
                    if (Detail_User_Get_JSON.getUser().getPhone().isEmpty() || Detail_User_Get_JSON.getUser().getPhone().equalsIgnoreCase("")){
                        //Toast.makeText(Mainpage_Activity_Detail.this, "شماره کاربر موجود نمی باشد", Toast.LENGTH_SHORT).show();
                        new StyleableToast.Builder(Mainpage_Activity_Detail.this).text("شماره کاربر موجود نمی باشد").textColor(Color.WHITE).backgroundColor(Mainpage_Activity_Detail.this.getResources().getColor(R.color.colorPrimary)).cornerRadius(5).show();

                    }
                    else
                        Mainpage_Activity_Detail.this.startActivity(intent);
                }
            });


        }else{
            //download or processing failed
            Log.e(TAG, "onDataAvailavle: failed with status" + status );
        }

        Log.d(TAG, "onDataAvailable: ends");
    }


    public static boolean isAppAvailable(Context context, String appName)
    {
        PackageManager pm = context.getPackageManager();
        try
        {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

    public static void setTranslucentStatusBar(Window window) {
        if (window == null) return;
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatusBarLollipop(window);
        } else if (sdkInt >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatusBarKiKat(window);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setTranslucentStatusBarLollipop(Window window) {
        window.setStatusBarColor(
                window.getContext()
                        .getResources()
                        .getColor(R.color.colorPrimaryDark));
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void setTranslucentStatusBarKiKat(Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this , R.style.AlertDialogTheme)
                //set message, title, and icon
                .setMessage("آیا نسبت به حذف این آگهی مطمئن هستید ؟")
                .setIcon(R.drawable.ic_delete_white_24dp)

                .setPositiveButton("بله", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        String method = "removing_ad";
                        BackgroundTask_Removing_Ad backgroundTask_removing_ad = new BackgroundTask_Removing_Ad(Mainpage_Activity_Detail.this);
                        backgroundTask_removing_ad.execute(method , itemUser_id , itemAD_id);

                        dialog.dismiss();
                        onStop2();
                        finish();

                    }

                })



                .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }

    protected void onStop2() {
        Log.d(TAG, "onStop2: ");
        setResult(2);
        super.onStop();
    }

}
