package ir.daneshjou_yaar.daneshjo_need;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.daneshjo_need.mainpage.MainpageFragment;
import ir.daneshjou_yaar.daneshjo_need.mainpage.PaginationAdapterCallback;

public class Daneshjo_Need_Activity extends AppCompatActivity implements PaginationAdapterCallback {

    private String url_cat = "http://eqtech.ir/get_cat.php";
    private String url_data = "http://eqtech.ir/get_data.php";
    private ViewPager viewPager;
    private PaginationAdapterCallback mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daneshjo__need);


        ArrayList<Tab_Model> tabsList = new ArrayList<>();
        tabsList.add(new Tab_Model("صفحه اصلی", R.drawable.ic_home_white_48dp));
        tabsList.add(new Tab_Model("جستجو", R.drawable.ic_search_white_48dp));
        tabsList.add(new Tab_Model("دسته بندی",R.drawable.ic_list_white_48dp));
        tabsList.add(new Tab_Model("پروفایل", R.drawable.ic_person_pin_white_48dp));


        viewPager = (ViewPager) findViewById(R.id.main_pager);
        FragmentPagerAdapter fragmentPagerAdapter = new Pager_Adapter(getSupportFragmentManager() , tabsList);
        viewPager.setAdapter(fragmentPagerAdapter);


        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
        tabs.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

/*-------------------------------------------------------------------------------------------*/

    }
    public void setCurrentItem (int item, boolean smoothScroll) {
        viewPager.setCurrentItem(item, smoothScroll);
    }

    public void setListener(PaginationAdapterCallback listener)
    {
        this.mCallback = listener ;
    }

    @Override
    public void retryPageLoad() {
        MainpageFragment fragment = new MainpageFragment();
        ((MainpageFragment) fragment).loadNextPage();
    }
}
