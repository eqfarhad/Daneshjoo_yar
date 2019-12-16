package ir.daneshjou_yaar.news;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.news.culture.Culture_Fragment;
import ir.daneshjou_yaar.news.general.Generalnews_Fragment;
import ir.daneshjou_yaar.news.takhfif.Takhfif_Fragment;
import ir.daneshjou_yaar.news.university.University_news_Fragment;

public class News_Mainpage extends DrawerActivity implements PaginationAdapterCallback
{
    private static final String TAG = "News_Mainpage";
     MaterialViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_mainpage);
        setTitle("");
        //mishe ezafe kardo o nevesht akhbar

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        final Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 4) {
                    case 0:
                        return Generalnews_Fragment.newInstance();
                    case 1:
                        return Takhfif_Fragment.newInstance();
                    case 2:
                        return University_news_Fragment.newInstance();
                    case 3:
                        return Culture_Fragment.newInstance();
                    default:
                        return Generalnews_Fragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 4) {
                    case 0:
                        return "کاربران";
                    case 1:
                        return "تخفیفات";
                    case 2:
                        return "دانشگاه";
                    case 3:
                        return "فرهنگی";
                }
                return "";
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.colorPrimaryDark,
                                getResources().getDrawable(R.drawable.news_header));
                    case 1:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.red,
                                getResources().getDrawable(R.drawable.takhfif_header_));
                    case 2:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.blue,
                                getResources().getDrawable(R.drawable.university));
                    case 3:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.red,
                                getResources().getDrawable(R.drawable.farhangi));
                    /*case 3:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.cyan,
                                getResources().getDrawable(R.drawable.semnan));*/
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        final View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                   // Toast.makeText(getApplicationContext(), ":)", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    @Override
    public void retryPageLoad() {
        Generalnews_Fragment fragment = new Generalnews_Fragment();
        ((Generalnews_Fragment) fragment).loadNextPage();
    }
}
