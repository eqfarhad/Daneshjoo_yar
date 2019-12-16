package ir.daneshjou_yaar.news.university;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ir.daneshjou_yaar.Custom_Text_View;
import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.news.news_detail.BackgroundTask_Increase_counter;

import static ir.daneshjou_yaar.daneshjo_need.mainpage.ads_detail.Mainpage_Activity_Detail.setTranslucentStatusBar;

public class News_Detail_uni_Activity extends AppCompatActivity {
    private static final String TAG = "News_Detail_Activity";

    private static final String EXTRA_IMAGE = "ir.eqtech.daneshjou_yaar.extraImage";
    private static final String EXTRA_TITLE = "ir.eqtech.daneshjou_yaar.extraTitle";
    private static final String EXTRA_DESCRIPTION = "ir.eqtech.daneshjou_yaar.extraDescription";
    private static final String EXTRA_TIME = "ir.eqtech.daneshjou_yaar.extraTime";
    private static final String EXTRA_NEWS_ID = "ir.eqtech.daneshjou_yaar.extraNews_id";
    private static final String EXTRA_NEWS_link = "ir.eqtech.daneshjou_yaar.extraNews_link";

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private String itemNews_id;
    private LinearLayout emotion_linear;
    private LinearLayout go_to_link_linear;

    public static void navigate_uni(FragmentActivity activity, byte[] byteArray, View transitionImage, News_uni viewModel ) {
        Intent intent = new Intent(activity, News_Detail_uni_Activity.class);
        intent.putExtra(EXTRA_IMAGE, viewModel.getImage());
        intent.putExtra(EXTRA_TITLE, viewModel.getTitle());
        intent.putExtra(EXTRA_DESCRIPTION, viewModel.getDescription());
        intent.putExtra(EXTRA_TIME, viewModel.getDate());
        intent.putExtra(EXTRA_NEWS_ID , viewModel.getId());
        intent.putExtra(EXTRA_NEWS_link , viewModel.getLink());

        intent.putExtra("Image", byteArray);

        Log.d(TAG, "navigate: "+ viewModel.getId());


        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_IMAGE);
        //ActivityOptionsCompat options_ = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_TITLE);
        ActivityCompat.startActivityForResult(activity, intent, 100 , options.toBundle());
        //   progressBar.setVisibility(View.GONE);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.activity_news_detail);
        setTranslucentStatusBar(getWindow());

        ViewCompat.setTransitionName(findViewById(R.id.news_detail_activity_appbar), EXTRA_IMAGE);
        supportPostponeEnterTransition();

        setSupportActionBar((Toolbar) findViewById(R.id.news_detail_activity_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toolbar toolbar = findViewById(R.id.news_detail_activity_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        String itemTitle = getIntent().getStringExtra(EXTRA_TITLE);
        String itemDescription = getIntent().getStringExtra(EXTRA_DESCRIPTION);
        String itemTime = getIntent().getStringExtra(EXTRA_TIME);
        itemNews_id = getIntent().getStringExtra(EXTRA_NEWS_ID);

        final String itemLink = getIntent().getStringExtra(EXTRA_NEWS_link);

        emotion_linear = (LinearLayout) findViewById(R.id.emotion_linear);
        go_to_link_linear = (LinearLayout) findViewById(R.id.link_linear);

        emotion_linear.setVisibility(View.GONE);
        go_to_link_linear.setVisibility(View.VISIBLE);


        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.news_collapsing_toolbar);
        collapsingToolbarLayout.setTitle(itemTitle);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        final ImageView image = (ImageView) findViewById(R.id.news_detail_activity_image);

        byte[] byteArray = getIntent().getByteArrayExtra("Image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

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

        TextView title = (TextView) findViewById(R.id.news_detail_activity_title);
        title.setText(itemTitle);

        Custom_Text_View description = (Custom_Text_View) findViewById(R.id.news_detail_activity_description);
        description.setText("توضیحات : " + itemDescription);

        Custom_Text_View time = (Custom_Text_View) findViewById(R.id.news_detail_activity_time);
        time.setText("زمان انتشار :" + itemTime);

        go_to_link_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemLink == null){

                }else {
                    String url = itemLink;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }

            }
        });

        //increasing counter for post
        String method = "counter";
        BackgroundTask_Increase_counter backgroundTask_increase_counter = new BackgroundTask_Increase_counter(News_Detail_uni_Activity.this);
        backgroundTask_increase_counter.execute( method , itemNews_id);
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
}
