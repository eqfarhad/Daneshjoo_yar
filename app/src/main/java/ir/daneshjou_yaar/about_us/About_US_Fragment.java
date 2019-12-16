package ir.daneshjou_yaar.about_us;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.daneshjou_yaar.R;

public class About_US_Fragment extends Fragment {

    public static About_US_Fragment newInstance() {
        About_US_Fragment fm = new About_US_Fragment();
        return fm;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_about__us_fragment, container, false);

        return v;
    }


}
