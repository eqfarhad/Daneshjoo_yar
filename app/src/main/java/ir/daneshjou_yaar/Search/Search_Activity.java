package ir.daneshjou_yaar.Search;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import ir.daneshjou_yaar.Custom_Text_View;
import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.databinding.ActivitySearchBinding;
import ir.daneshjou_yaar.location_address.DatabaseAccess;
import ir.daneshjou_yaar.location_address.Object_model;
import ir.daneshjou_yaar.location_address.Showing_Detail_Category.Showing_models;


public class Search_Activity extends AppCompatActivity{

    ActivitySearchBinding activityMainBinding;
    ListAdapter adapter;
    List arrayList= new ArrayList();


    private List<Object_model> model_list = new ArrayList<>();
    private String choosed;

    private String keyword = null ;

    private SearchView mSearchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        prepareDate();

        mSearchView = (SearchView) findViewById(R.id.search);

        final TextView searchText = (TextView)
                mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        final Typeface myCustomFont = Typeface.createFromAsset(getAssets(),"IRKamran.ttf");
        searchText.setTypeface(myCustomFont);
        searchText.setTextSize(22);
        searchText.setTextColor(getResources().getColor(R.color.icons));
        searchText.setHintTextColor(getResources().getColor(R.color.icons));



        searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    searchText.setHint("");
                else
                    activityMainBinding.search.setQueryHint("متن مورد نیاز خود را بنویسید");
            }
        });


        adapter= new ListAdapter(arrayList);
        activityMainBinding.listView.setAdapter(adapter);
        activityMainBinding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        activityMainBinding.search.setActivated(true);
        activityMainBinding.search.setQueryHint("متن مورد نیاز خود را بنویسید");
        activityMainBinding.search.onActionViewExpanded();
        activityMainBinding.search.setIconified(false);
        activityMainBinding.search.clearFocus();
        activityMainBinding.search.setBackgroundColor(getResources().getColor(R.color.toolbar_color));
        activityMainBinding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Custom_Text_View custom_text_view = (Custom_Text_View) view.findViewById(R.id.stringName);
                keyword = custom_text_view.getText().toString();
                Toast.makeText(Search_Activity.this, ""+keyword, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Search_Activity.this , Showing_models.class);
                intent.putExtra("choosed" , "search");
                intent.putExtra("keyword" , keyword);
                startActivity(intent);
            }
        });




    }


    private void prepareDate() {

        Intent intent = getIntent();
        choosed = intent.getStringExtra("choosed");

        if (choosed == null)
            choosed = "*";

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        model_list = databaseAccess.getAllinfo(choosed , null);
//        Log.d("after", "prepareDate: "+ model_list.get(0).getName());
        databaseAccess.close();

        for (int i=0; i <model_list.size() ; i++ ){
            arrayList.add(model_list.get(i).getName());
        }
    }

}
