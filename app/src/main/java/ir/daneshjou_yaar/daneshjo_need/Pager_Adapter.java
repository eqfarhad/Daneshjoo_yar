package ir.daneshjou_yaar.daneshjo_need;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.daneshjo_need.categories.CategoriesFragment;
import ir.daneshjou_yaar.daneshjo_need.mainpage.MainpageFragment;
import ir.daneshjou_yaar.daneshjo_need.profile.ProfileFragment;
import ir.daneshjou_yaar.daneshjo_need.search.SearchFragment;

/**
 * Created by iqfarhad on 1/28/2018.
 */

public class Pager_Adapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.CustomTabProvider{

    ArrayList<Tab_Model> tabs;

    public Pager_Adapter(FragmentManager fm, ArrayList<Tab_Model> tabs) {
        super(fm);
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return MainpageFragment.newInstance();
        if (position == 1)
            return SearchFragment.newInstance();
        if (position == 2)
            return CategoriesFragment.newInstance();
        if (position == 3)
            return ProfileFragment.newInstance();

        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }


    @Override
    public View getCustomTabView(ViewGroup viewGroup, int i) {

        LinearLayout tabLayout = (LinearLayout)
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_tab_layout, null);

        TextView title = (TextView) tabLayout.findViewById(R.id.tab_text);
        ImageView image = (ImageView) tabLayout.findViewById(R.id.tab_image);

        Tab_Model tab = tabs.get(i);
        title.setText(tab.text);
        image.setImageResource(tab.image);


        return tabLayout;
    }

    @Override
    public void tabSelected(View view) {

    }

    @Override
    public void tabUnselected(View view) {

    }

    @Override
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }
}
