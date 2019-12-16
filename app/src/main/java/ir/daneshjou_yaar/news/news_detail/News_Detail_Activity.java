package ir.daneshjou_yaar.news.news_detail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import ir.daneshjou_yaar.Custom_Text_View;
import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.news.News;

import static ir.daneshjou_yaar.daneshjo_need.mainpage.ads_detail.Mainpage_Activity_Detail.setTranslucentStatusBar;

public class News_Detail_Activity extends AppCompatActivity {
    private static final String TAG = "News_Detail_Activity";

    private static final String EXTRA_IMAGE = "ir.eqtech.daneshjou_yaar.extraImage";
    private static final String EXTRA_TITLE = "ir.eqtech.daneshjou_yaar.extraTitle";
    private static final String EXTRA_DESCRIPTION = "ir.eqtech.daneshjou_yaar.extraDescription";
    private static final String EXTRA_TIME = "ir.eqtech.daneshjou_yaar.extraTime";
    private static final String EXTRA_NEWS_ID = "ir.eqtech.daneshjou_yaar.extraNews_id";

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private String itemNews_id;
    private SmileRating smileRating;

    public static void navigate(FragmentActivity activity, byte[] byteArray, View transitionImage, News viewModel ) {
        Intent intent = new Intent(activity, News_Detail_Activity.class);
        intent.putExtra(EXTRA_IMAGE, viewModel.getImage());
        intent.putExtra(EXTRA_TITLE, viewModel.getTitle());
        intent.putExtra(EXTRA_DESCRIPTION, viewModel.getDescription());
        intent.putExtra(EXTRA_TIME, viewModel.getDate());
        intent.putExtra(EXTRA_NEWS_ID , viewModel.getId());

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

        collapsingToolbarLayout = findViewById(R.id.news_collapsing_toolbar);
        collapsingToolbarLayout.setTitle(itemTitle);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        final ImageView image = findViewById(R.id.news_detail_activity_image);
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

        TextView title = findViewById(R.id.news_detail_activity_title);
        title.setText(itemTitle);

        Custom_Text_View description = findViewById(R.id.news_detail_activity_description);
        description.setText("توضیحات : " + itemDescription);

        Custom_Text_View time = findViewById(R.id.news_detail_activity_time);
        time.setText("زمان انتشار :" + itemTime);

        smileRating = findViewById(R.id.smile_rating_news_detail);
        smileRating.setNameForSmile(BaseRating.TERRIBLE, "عصبانی!");
        smileRating.setNameForSmile(BaseRating.BAD, "ناراحت");
        smileRating.setNameForSmile(BaseRating.OKAY, "اوکی");
        smileRating.setNameForSmile(BaseRating.GOOD, "خوبه");
        smileRating.setNameForSmile(BaseRating.GREAT, "عالیه!");

        //increasing counter for post
        String method = "counter";
        BackgroundTask_Increase_counter backgroundTask_increase_counter = new BackgroundTask_Increase_counter(News_Detail_Activity.this);
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

    @Override
    protected void onPause() {
        int emotion = 0;
        @BaseRating.Smiley int smiley = smileRating.getSelectedSmile();
        switch (smiley) {
            case SmileRating.BAD:
                Log.i(TAG, "Bad");
                emotion = 1;
                break;
            case SmileRating.GOOD:
                Log.i(TAG, "Good");
                emotion = 3;
                break;
            case SmileRating.GREAT:
                Log.i(TAG, "Great");
                emotion = 4;
                break;
            case SmileRating.OKAY:
                Log.i(TAG, "Okay");
                emotion = 2;
                break;
            case SmileRating.TERRIBLE:
                Log.i(TAG, "Terrible");
                emotion = 0;
                break;
            case BaseRating.NONE:
                Log.i(TAG, "None");
                emotion = 2;
                break;
        }

        //increasing counter for post
        String method = "emotion";
        BackgroundTask_Increase_counter backgroundTask_increase_counter = new BackgroundTask_Increase_counter(News_Detail_Activity.this);
        backgroundTask_increase_counter.execute( method , itemNews_id , String.valueOf(emotion));

        super.onPause();
    }
}
