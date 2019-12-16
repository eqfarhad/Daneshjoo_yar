package ir.daneshjou_yaar.daneshjo_need.categories;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.daneshjo_need.DownloadStatus;
import ir.daneshjou_yaar.daneshjo_need.categories.Category_Choosed.Category_Choosed_Activity;

/**
 * Created by iqfarhad on 1/28/2018.
 */

public class CategoriesFragment extends Fragment implements Categories_Get_JSON_Data.onDataAvailable {

    private static final String TAG = "CategoriesFragment";
    private String url_cat = "http://eqtech.ir/get_cat.php";
    private GridView category_gridview;
    private grid_adapter mGrid_adapter;
    private ProgressBar mProgressBar;

    public static List<Category_Model> categories_list;

    public static CategoriesFragment newInstance() {
        CategoriesFragment fm = new CategoriesFragment();
        return fm;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
//        JSON_Downloader json_downloader = new JSON_Downloader(this);
//        json_downloader.execute(url_cat);
//
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();

        Categories_Get_JSON_Data categoriesGet_json_data = new Categories_Get_JSON_Data(this, "http://eqtech.ir/get_cat.php", null, true);
        // categoriesGet_json_data.executeOnSameThread(null);
        categoriesGet_json_data.execute("");
        Log.d(TAG, "onResume: ends");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starts");
        View v = inflater.inflate(R.layout.fragment_daneshjo_need_categories, container, false);

        category_gridview = (GridView) v.findViewById(R.id.fragment_category_gridview);

        mProgressBar = (ProgressBar) v.findViewById(R.id.fragment_category_progressbar);
        mProgressBar.setVisibility(View.VISIBLE);

        mGrid_adapter = new grid_adapter(new ArrayList<Category_Model>(), getActivity());
        category_gridview.setAdapter(mGrid_adapter);

//------------------------------------item click listener---------------------------

        category_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

               // Toast.makeText(getActivity(), "" + mGrid_adapter.getCategory(i).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity() , Category_Choosed_Activity.class);
                intent.putExtra("cat" , mGrid_adapter.getCategory(i).getId());
                startActivity(intent);


                /*if (i == 0) {
                    Toast.makeText(getActivity(), "" + mGrid_adapter.getCategory(i).getName(), Toast.LENGTH_SHORT).show();
                    *//*((Daneshjo_Need_Activity)getActivity()).setCurrentItem (0, true);*//*
                    Intent intent = new Intent(getActivity() , Category_Choosed_Activity.class);
                    intent.putExtra("cat" , mGrid_adapter.getCategory(i).getId());
                    startActivity(intent);
                } else if (i == 1) {
                    Toast.makeText(getActivity(), "" + mGrid_adapter.getCategory(i).getName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity() , Category_Choosed_Activity.class);
                    intent.putExtra("cat" , mGrid_adapter.getCategory(i).getId());
                    startActivity(intent);
                } else if (i == 2) {
                    Toast.makeText(getActivity(), "" + mGrid_adapter.getCategory(i).getName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity() , Category_Choosed_Activity.class);
                    intent.putExtra("cat" , mGrid_adapter.getCategory(i).getId());
                    startActivity(intent);
                }*/
            }
        });


        Log.d(TAG, "onCreateView: ends");
        return v;
    }


    @Override
    public void onDataAvailable(final List<Category_Model> data, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable : starts");

        if (status == DownloadStatus.OK) {
            mGrid_adapter.loadNewData(data);
            categories_list = data;
            mProgressBar.setVisibility(View.GONE);

        } else {
            //download or processing failed
            Log.e(TAG, "onDataAvailavle: failed with status" + status);
        }

        Log.d(TAG, "onDataAvailable: ends");
    }
}
