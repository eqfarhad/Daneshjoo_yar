package ir.daneshjou_yaar.location_address.Showing_Detail_Category;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import ir.daneshjou_yaar.Custom_Text_View;
import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.map.MapsActivity;

public class Larger_image_dialog extends Activity {

    private ImageView mImageView;
    private LinearLayout mLinearLayout ;
    private ImageView mCloseButton;
    private Context mContext;
    private Custom_Text_View address;
    private Custom_Text_View descripton;
    private boolean direction = false;
    private Custom_Text_View text_btn;
    private Intent intent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_larger_image_dialog);

        mImageView = (ImageView) findViewById(R.id.large_image_showing_detail_activity);
        mCloseButton = (ImageView) findViewById(R.id.close_btn_large_image);
        mLinearLayout = (LinearLayout) findViewById(R.id.go_to_googlemap_large_image);

        address = (Custom_Text_View) findViewById(R.id.large_image_detail_address);
        descripton = (Custom_Text_View) findViewById(R.id.large_image_detail_description);
        text_btn = ( Custom_Text_View) findViewById(R.id.go_to_googlemap_text);



         intent  = getIntent();

        Picasso.with(this).load(intent.getStringExtra("model_list_image"))
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(mImageView);

        address.setText(intent.getStringExtra("model_list_address"));
        descripton.setText(intent.getStringExtra("model_list_description"));

        if (intent.getBooleanExtra("direction" , false)){
            direction = true;
            text_btn.setText("مسیر یابی");
        }

        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String longitude = intent.getStringExtra("model_list_longitude");
                String latidue = intent.getStringExtra("model_list_latitude");
                String name = intent.getStringExtra("model_list_name");

                if (direction){
                     Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<"+latidue+">,<"+longitude+">?q=<"+latidue+">,<"+longitude+">("+name+")"));
                    startActivity(intent1);
                }else {

                    Intent intent1 = new Intent(Larger_image_dialog.this , MapsActivity.class);
                    intent1.putExtra("model_list_longitude" , longitude);
                    intent1.putExtra("model_list_latitude" , latidue);
                    startActivity(intent1);
                }

            }
        });

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
