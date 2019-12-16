package ir.daneshjou_yaar.location_address.Showing_Detail_Category;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.daneshjou_yaar.Custom_Text_View;
import ir.daneshjou_yaar.First_Menu_Activity;
import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.Search.Search_Activity;
import ir.daneshjou_yaar.location_address.DatabaseAccess;
import ir.daneshjou_yaar.location_address.Object_model;

public class Showing_models extends AppCompatActivity implements  RecyclerItemClickListener.OnRecyclerClickListener{

    private static final String TAG = "Showing_Models";
    private List<Object_model> model_list = new ArrayList<>();
    private RecyclerView recyclerView;
    private Recycle_view_Showing_model_adapter mAdapter;

    private Custom_Text_View toolbar_header_txt;
    private ImageView toolbar_search_btn;

    private String choosed;
    private String keyword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_models);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this , recyclerView ,this));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        prepareDate();

        toolbar_drawer();

    }


    private void prepareDate() {

        Intent intent = getIntent();
        choosed = intent.getStringExtra("choosed");
        keyword = intent.getStringExtra("keyword");

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        model_list = databaseAccess.getAllinfo(choosed , keyword);
//        Log.d("after", "prepareDate: "+ model_list.get(0).getName());
        databaseAccess.close();
        mAdapter = new Recycle_view_Showing_model_adapter(model_list , this);
        recyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

    }


    @Override
    public void onItemClick(View view, final int position) {
        Log.d(TAG, "onItemClick: starts");
        Intent intent = new Intent(Showing_models.this , Larger_image_dialog.class);
        intent.putExtra("model_list_image" , model_list.get(position).getImg());
        intent.putExtra("model_list_longitude" , model_list.get(position).getLongitude());
        intent.putExtra("model_list_latitude" , model_list.get(position).getLatitude());
        intent.putExtra("model_list_name" , model_list.get(position).getName());
        intent.putExtra("model_list_address" , model_list.get(position).getAddress());
        intent.putExtra("model_list_description" , model_list.get(position).getInfo());
        startActivity(intent);

    }

    @Override
    public void onItemLongClick(View view, int position) {
        String name = model_list.get(position).getName();
        String longitude = model_list.get(position).getLongitude();
        String latidue = model_list.get(position).getLatitude();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<"+latidue+">,<"+longitude+">?q=<"+latidue+">,<"+longitude+">("+name+")"));
        startActivity(intent);

    }



    private void toolbar_drawer() {

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent));     rang matn

        toolbar_search_btn = (ImageView) findViewById(R.id.search_head_toolbar) ;
        toolbar_header_txt = (Custom_Text_View) findViewById(R.id.text_head_toolbar);
      //  toolbar_header_txt.setText("رستوان");


        if (choosed.equalsIgnoreCase("resturant")){
            toolbar_header_txt.setText("رستوران");
        }
        else if (choosed.equalsIgnoreCase("restroom")){
            toolbar_header_txt.setText("خوابگاه ها");
        }
        else if (choosed.equalsIgnoreCase("offical")){
            toolbar_header_txt.setText("مراکز دولتی");
        }
        else if (choosed.equalsIgnoreCase("water")){
            toolbar_header_txt.setText("تصفیه آب");
        }
        else if (choosed.equalsIgnoreCase("bookshop")){
            toolbar_header_txt.setText("کتاب فروشی");
        }
        else if (choosed.equalsIgnoreCase("shops")){
            toolbar_header_txt.setText("فروشگاه ها");
        }
        else if (choosed.equalsIgnoreCase("house")){
            toolbar_header_txt.setText("مشاوره املاک");
        }
        else if (choosed.equalsIgnoreCase("hospital")){
            toolbar_header_txt.setText("بیمارستان ها");
        }
        else if (choosed.equalsIgnoreCase("banks")){
            toolbar_header_txt.setText("بانک ها");
        }
        else if (choosed.equalsIgnoreCase("taxi")){
            toolbar_header_txt.setText("تاکسی");
        }
        else if (choosed.equalsIgnoreCase("etc")){
            toolbar_header_txt.setText("متفرقه");
        }
        else if (choosed.equalsIgnoreCase("search")){
            toolbar_header_txt.setText(keyword);
        }


//navigation drawer nemikhaym inja fln
       /* final ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);*/

      /*  DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerToggle.syncState();


        //click menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuitem1:
                        Toast.makeText(Showing_models.this, "صفحه اصلی کلیک شده است", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Showing_models.this , First_Menu_Activity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.menuitem2:
                        Toast.makeText(Showing_models.this, "خروج کلیک شده است", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
*/
        //end navigation


        //start search

        toolbar_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Showing_models.this , Search_Activity.class);
                intent.putExtra("choosed", choosed);
                startActivity(intent);
            }
        });

    }

}
