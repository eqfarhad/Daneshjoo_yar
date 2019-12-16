package ir.daneshjou_yaar.about_us;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import ir.daneshjou_yaar.R;

public class About_US_Activity extends FragmentActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about_us_);

        ArrayList<String> tabsList = new ArrayList<>();
        tabsList.add("درباره ما");
        tabsList.add("قوانین");


        viewPager = (ViewPager) findViewById(R.id.pager_about_us);
        FragmentPagerAdapter fragmentPagerAdapter = new Pager_Adapter_About_Us(getSupportFragmentManager() , tabsList);
        viewPager.setAdapter(fragmentPagerAdapter);

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs_about_us);
        tabs.setViewPager(viewPager);
        tabs.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

    }
}
