package ir.daneshjou_yaar.map;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import ir.daneshjou_yaar.R;

/**
 * Created by iqfarhad on 8/18/2018.
 */

public class CustomClusterRender extends DefaultClusterRenderer<MapsActivity.StringClusterItem> {

    private final Context mContext;

    public CustomClusterRender(Context context, GoogleMap map,
                                 ClusterManager<MapsActivity.StringClusterItem> clusterManager) {
        super(context, map, clusterManager);

        mContext = context;
    }

    @Override protected void onBeforeClusterItemRendered(MapsActivity.StringClusterItem item,
                                                         MarkerOptions markerOptions) {
        final BitmapDescriptor markerDescriptor;

        //final BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
       // markerDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_account_balance_black_48dp);
        if (item.getCategory().equalsIgnoreCase("restroom")){
            markerDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_city_black_48dp);
        }else if (item.getCategory().equalsIgnoreCase("official")){
            markerDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_account_balance_black_48dp);
        }else if (item.getCategory().equalsIgnoreCase("water")){
            markerDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_opacity_black_48dp);
        }else if (item.getCategory().equalsIgnoreCase("resturant")){
            markerDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant_menu_black_48dp);
        }else if (item.getCategory().equalsIgnoreCase("bookshop")){
            markerDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_import_contacts_black_48dp);
        }else if (item.getCategory().equalsIgnoreCase("shops")){
            markerDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_add_shopping_cart_black_48dp);
        }else if (item.getCategory().equalsIgnoreCase("house")){
            markerDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_home_black_48dp);
        }else if (item.getCategory().equalsIgnoreCase("hospital")){
            markerDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_local_hospital_black_48dp);
        }else if (item.getCategory().equalsIgnoreCase("banks")){
            markerDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_attach_money_black_48dp);
        }else if (item.getCategory().equalsIgnoreCase("taxi")){
            markerDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_car_black_48dp);
        }else if (item.getCategory().equalsIgnoreCase("etc")){
            markerDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_more_horiz_black_48dp);
        }else{
            markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
        }

        markerOptions.icon(markerDescriptor).snippet(item.title);
    }
}