package ir.daneshjou_yaar.location_address;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.map.MapStyleManager;

public class Map_location_user extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    MarkerOptions marker;

    LinearLayout cancel_btn;
    LinearLayout agree_btn;

    double latitude;
    double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map_location_user);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_location_user);
        mapFragment.getMapAsync(this);
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
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        cancel_btn = (LinearLayout) findViewById(R.id.activity_map_location_user_cancel_btn);
        agree_btn = (LinearLayout) findViewById(R.id.activity_map_location_user_agree_btn);


        MapStyleManager styleManager = MapStyleManager.attachToMap(this, mMap);
        styleManager.addStyle(10, R.raw.map_style_plain_sparse);
        // Add a marker in Sydney and move the camera
        LatLngBounds semnan = new LatLngBounds(
                new LatLng(35.55, 53.36), new LatLng(35.60, 53.54));
        LatLng semnan_center = new LatLng(35.583222 ,53.388897);
// Constrain the camera target to the Adelaide bounds.
        mMap.setLatLngBoundsForCameraTarget(semnan);
        //mMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom( semnan_center, 14));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker != null){
                    mMap.clear();
                }
                latitude = latLng.latitude;
                longitude = latLng.longitude ;
                marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("این موقعیت برای شغل شما ثبت خواهد شد ");
                mMap.addMarker(marker);
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });


        agree_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude", longitude);
                //   intent.putExtra("user_signed_up_name" , "کاربر گرامی : "+name+" "+family);
                setResult(16, intent);
                finish();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("latitude",0);
                intent.putExtra("longitude", 0);
                //   intent.putExtra("user_signed_up_name" , "کاربر گرامی : "+name+" "+family);
                setResult(16, intent);
                finish();
            }
        });

    }
}
