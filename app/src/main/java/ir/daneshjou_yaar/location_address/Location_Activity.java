package ir.daneshjou_yaar.location_address;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import ir.daneshjou_yaar.Custom_Text_View;
import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.Search.Search_Activity;
import ir.daneshjou_yaar.location_address.Showing_Detail_Category.Showing_models;

public class Location_Activity  extends DrawerActivity_location_address{

    private Custom_Text_View toolbar_header_txt;
    private ImageView toolbar_search_btn;
    private GridView location_gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent));     rang matn

        toolbar_search_btn = (ImageView) findViewById(R.id.search_head_toolbar) ;
        toolbar_header_txt = (Custom_Text_View) findViewById(R.id.text_head_toolbar);
        toolbar_header_txt.setText("اماکن و آدرس ها");




        final ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout_location_address);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerToggle.syncState();


        //click menu
       /* NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuitem1:
                        Toast.makeText(Location_Activity.this, "صفحه اصلی کلیک شده است", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Location_Activity.this , First_Menu_Activity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.menuitem2:
                        Toast.makeText(Location_Activity.this, "خروج کلیک شده است", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });*/

        //end navigation


        //start search

        toolbar_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Location_Activity.this , Search_Activity.class);
                intent.putExtra("choosed" , "*");
                startActivity(intent);
            }
        });


        //setting gridview

        location_gridview = (GridView) findViewById(R.id.location_gridview);

        String amaken[] = {
                "خوابگاه ها" ,
                "ادارات",
                "تصفیه آب",
                "رستوران ها",
                "کتاب فروشی",
                "فروشگاه ها",
                "مشاوره املاک",
                "بیمارستان ها",
                "بانک ها",
                "تاکسی",
                "متفرقه"

        };

        int icons[] = {
                R.drawable.ic_location_city_white_48dp,
                R.drawable.ic_account_balance_white_48dp,
                R.drawable.ic_opacity_white_48dp,
                R.drawable.ic_restaurant_menu_white_48dp,
                R.drawable.ic_import_contacts_white_48dp,
                R.drawable.ic_add_shopping_cart_white_48dp,
                R.drawable.ic_home_white_48dp,
                R.drawable.ic_local_hospital_white_48dp,
                R.drawable.ic_attach_money_white_48dp,
                R.drawable.ic_directions_car_white_48dp,
                R.drawable.ic_more_horiz_white_48dp
        };

        grid_adapter adapter = new grid_adapter(amaken ,icons , this);

        location_gridview.setAdapter(adapter);


        location_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Location_Activity.this, Showing_models.class);

                if (position == 0) {
                    intent.putExtra("choosed" , "restroom");
                }else if (position == 1) {
                    intent.putExtra("choosed", "offical");
                }else if (position == 2){
                    intent.putExtra("choosed" , "water");
                }else if (position == 3){
                    intent.putExtra("choosed" , "resturant");
                }else if (position == 4){
                    intent.putExtra("choosed" , "bookshop");
                }else if (position == 5){
                    intent.putExtra("choosed" , "shops");
                }else if (position == 6){
                    intent.putExtra("choosed" , "house");
                }else if (position == 7){
                    intent.putExtra("choosed" , "hospital");
                }else if (position == 8){
                    intent.putExtra("choosed" , "banks");
                }else if ( position == 9){
                    intent.putExtra("choosed", "taxi");
                }else if (position == 10){
                    intent.putExtra("choosed" , "etc");
                }

                startActivity(intent);
            }
        });

    }



}
