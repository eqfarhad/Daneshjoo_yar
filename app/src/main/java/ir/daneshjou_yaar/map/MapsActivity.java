package ir.daneshjou_yaar.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.location_address.DatabaseAccess;
import ir.daneshjou_yaar.location_address.Object_model;
import ir.daneshjou_yaar.location_address.Showing_Detail_Category.Larger_image_dialog;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private ClusterManager<StringClusterItem> mClusterManager;
    private List<Object_model> model_list = new ArrayList<>();

    private GridView map_gridview;



    private List<Object_model> invisible_list = new ArrayList<>();

    final String amaken[] = {
            "نمایش همه",
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

    final String amaken_eng[] = {
            "all",
            "restroom",
            "official",
            "water",
            "resturant",
            "bookshop",
            "shops",
            "house",
            "hospital",
            "banks",
            "taxi",
            "etc"
    };

    int icons[] = {
            R.drawable.baseline_layers_white_48,
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        locations_data();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setPadding(0, 0 , 0 , 150);

        // Add a marker in Sydney and move the camera
    /*    LatLng semnan = new LatLng(35.57, 53.39);
        mMap.addMarker(new MarkerOptions().position(semnan).title("Marker in semnan"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(semnan));
*/
        MapStyleManager styleManager = MapStyleManager.attachToMap(this, mMap);
        styleManager.addStyle(10, R.raw.map_style_plain_sparse);


         LatLngBounds semnan = new LatLngBounds(
                new LatLng(35.55, 53.36), new LatLng(35.60, 53.54));
         LatLng semnan_center = new LatLng(35.583222 ,53.388897);
// Constrain the camera target to the Adelaide bounds.
        mMap.setLatLngBoundsForCameraTarget(semnan);
        //mMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom( semnan_center, 12));



/*
        CameraUpdate camereUpdate = CameraUpdateFactory.newLatLng(semnan);
        mMap.moveCamera(camereUpdate);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(7));*/

        mClusterManager = new ClusterManager<>(this, mMap);
        mMap.setOnCameraIdleListener(mClusterManager);

        for (int i=0 ; i < model_list.size();i++){
            Double Lati = Double.valueOf(model_list.get(i).getLatitude());
            Double Lang = Double.valueOf(model_list.get(i).getLongitude());
            final LatLng latLng = new LatLng( Lati , Lang);
            mClusterManager.addItem(new StringClusterItem(model_list.get(i).getName(), latLng , model_list.get(i).getCategory(),  model_list.get(i).getImg(), model_list.get(i).getInfo(), model_list.get(i).getAddress()));
        }
        mClusterManager.cluster();

        final CustomClusterRender renderer = new CustomClusterRender(this, mMap, mClusterManager);

        mClusterManager.setRenderer(renderer);

        //---------listener-------------
        mClusterManager.setOnClusterClickListener(
                new ClusterManager.OnClusterClickListener<StringClusterItem>() {
                    @Override public boolean onClusterClick(Cluster<StringClusterItem> cluster) {

                       // Toast.makeText(MapsActivity.this, "Cluster click", Toast.LENGTH_SHORT).show();

                        // if true, do not move camera

                        return false;
                    }
                });

        mClusterManager.setOnClusterItemClickListener(
                new ClusterManager.OnClusterItemClickListener<StringClusterItem>() {
                    @Override public boolean onClusterItemClick(StringClusterItem clusterItem) {

                      //  Toast.makeText(MapsActivity.this, "Cluster item click", Toast.LENGTH_SHORT).show();

                        // if true, click handling stops here and do not show info view, do not move camera
                        // you can avoid this by calling:
                        // renderer.getMarker(clusterItem).showInfoWindow();

                        return false;
                    }
                });

        mMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager.getMarkerCollection()
                .setOnInfoWindowAdapter(new CustomInfoViewAdapter(LayoutInflater.from(this)));

        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());


        //-----------item_tapped_listener-----------
        mClusterManager.setOnClusterItemInfoWindowClickListener(
                new ClusterManager.OnClusterItemInfoWindowClickListener<StringClusterItem>() {
                    @Override public void onClusterItemInfoWindowClick(StringClusterItem stringClusterItem) {
                      //  Toast.makeText(MapsActivity.this, "Clicked info window: " + stringClusterItem.title, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MapsActivity.this , Larger_image_dialog.class);
                        intent.putExtra("model_list_image" ,stringClusterItem.getImg());
                        intent.putExtra("model_list_longitude" , stringClusterItem.getPosition().longitude);
                        intent.putExtra("model_list_latitude" , stringClusterItem.getPosition().latitude);
                        intent.putExtra("model_list_name" , stringClusterItem.getTitle());
                        intent.putExtra("model_list_address" , stringClusterItem.getAddres());
                        intent.putExtra("model_list_description" , stringClusterItem.getDescrip());
                        intent.putExtra("direction", true);
                        startActivity(intent);
                    }
                });

        mMap.setOnInfoWindowClickListener(mClusterManager);

        //------------------gridview------------------

        map_gridview = (GridView) findViewById(R.id.map_gridview);

        grid_adapter_map adapter = new grid_adapter_map(amaken ,icons , this);

        map_gridview.setAdapter(adapter);

        map_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                LinearLayout linear = (LinearLayout) view;

                int color = Color.TRANSPARENT;
                Drawable background = view.getBackground();
                if (background instanceof ColorDrawable)
                    color =((ColorDrawable) background).getColor();



                if (color == Color.GRAY) {
                    linear.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    if (position==0){
                        for (int i = 1 ; i<amaken_eng.length ; i++){
                            showing_data_or_remove(amaken_eng[i] , Boolean.FALSE );
                        }
                    }else{
                        showing_data_or_remove(amaken_eng[position] , Boolean.FALSE );
                    }

                }else{
                    if (position==0) {
                        for (int i = 1; i < amaken_eng.length; i++) {
                            showing_data_or_remove (amaken_eng[i] , Boolean.TRUE );
                            // TODO: changing other grid buttons color


                            /*LinearLayout linear2 = (LinearLayout) adapterView.getItemAtPosition(i);



                            int color2 = Color.TRANSPARENT;
                            Drawable background2 = adapterView.getChildAt(i).getBackground();
                            if (background2 instanceof ColorDrawable)
                                color2 =((ColorDrawable) background2).getColor();
                            linear2.setBackgroundColor(Color.GRAY);*/

                        }
                    }else{
                        showing_data_or_remove (amaken_eng[position] , Boolean.TRUE );
                    }
                    linear.setBackgroundColor(Color.GRAY);
                }

            }
        });


        //-----------getting intent from location_address---------------
        Intent intent = getIntent();
        if (intent.getStringExtra("model_list_latitude") != null){
            Double intent_latidue = Double.valueOf(intent.getStringExtra("model_list_latitude"));
            Double intent_longitude = Double.valueOf(intent.getStringExtra("model_list_longitude"));
            LatLng intent_chosed = new LatLng(intent_latidue ,intent_longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom( intent_chosed, 16));
        }


    }

    private void locations_data() {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        model_list = databaseAccess.getAllinfo("*" , "");
//        Log.d("after", "prepareDate: "+ model_list.get(0).getName());
        databaseAccess.close();

    }

    static class StringClusterItem implements ClusterItem {
        final String title;
        final String category;
        final String img;
        final LatLng latLng;
        final String descrip;
        final String addres;
        StringClusterItem(String title, LatLng latLng, String category, String img, String descrip, String addres) {
            this.title = title;
            this.latLng = latLng;
            this.category = category;
            this.img = img;
            this.descrip = descrip;
            this.addres = addres;
        }
        @Override public LatLng getPosition() {
            return latLng;
        }

        @Override
        public String getTitle() {
            return null;
        }

        public String getImg() {
            return img;
        }

        @Override
        public String getSnippet() {
            return null;
        }

        String getCategory() {
            return category;
        }

        public String getDescrip() {
            return descrip;
        }

        public String getAddres() {
            return addres;
        }
    }




    //--------------------method baraye namayesh ya adam namayesh -------------------------------


   private void showing_data_or_remove (String cat_name , Boolean visibility){
        if (visibility){
            Iterator<Object_model> itr = model_list.iterator();
            while (itr.hasNext()){
                Object_model model = itr.next();
                if (model.getCategory().equalsIgnoreCase(cat_name) ) {
                    itr.remove();
                    if (!invisible_list.contains(model)) {
                        invisible_list.add(model);
                    }
                }
            }
            mMap.clear();
            mClusterManager.clearItems();
            for (int i=0 ; i < model_list.size();i++){
                Double Lati = Double.valueOf(model_list.get(i).getLatitude());
                Double Lang = Double.valueOf(model_list.get(i).getLongitude());
                final LatLng latLng = new LatLng( Lati , Lang);
                mClusterManager.addItem(new StringClusterItem(model_list.get(i).getName(), latLng , model_list.get(i).getCategory(), model_list.get(i).getImg(), model_list.get(i).getInfo(), model_list.get(i).getAddress()));
            }
            mClusterManager.cluster();
           /* if (cat_name.equalsIgnoreCase("banks")){
                visi = false;
              //  banks.setColorNormal(Color.GRAY);
            }else if (cat_name.equalsIgnoreCase("resturant")){
                visible_resturant = false;
               // resturant.setColorNormal(Color.GRAY);
            }else if (cat_name.equalsIgnoreCase("taxi")){
                visible_taxi = false;
               // taxi.setColorNormal(Color.GRAY);
            }else if (cat_name.equalsIgnoreCase("bookshop")){
                visible_bookshop = false;
              //  bookshop.setColorNormal(Color.GRAY);
            }*/

        }else{
            mMap.clear();
            mClusterManager.clearItems();
            //model_list.addAll(remove_banks_list);
            for (Object_model a:invisible_list) {
                if (a.getCategory().equalsIgnoreCase(cat_name)){
                    model_list.add(a);
                }
            }
            for (int i=0 ; i < model_list.size();i++){
                Double Lati = Double.valueOf(model_list.get(i).getLatitude());
                Double Lang = Double.valueOf(model_list.get(i).getLongitude());
                final LatLng latLng = new LatLng( Lati , Lang);
                mClusterManager.addItem(new StringClusterItem(model_list.get(i).getName(), latLng , model_list.get(i).getCategory(),  model_list.get(i).getImg(), model_list.get(i).getInfo(), model_list.get(i).getAddress()));
            }
            mClusterManager.cluster();
            /*if (cat_name.equalsIgnoreCase("banks")){
                visible_bank = true;
            }*/
           // banks.setColorNormal(getResources().getColor(R.color.red_map));
        }
    }













}
