package ir.daneshjou_yaar.location_address;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.io.ByteArrayOutputStream;

import ir.daneshjou_yaar.Custom_TextInput_Layout;
import ir.daneshjou_yaar.Custom_Text_View;
import ir.daneshjou_yaar.R;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Created by iqfarhad on 8/11/2018.
 */

public class DrawerActivity_location_address extends AppCompatActivity {
    private static final String TAG = "DrawerActivity";

    static DrawerLayout mDrawer_right = null;
    private ActionBarDrawerToggle mDrawerToggle;

    private Custom_TextInput_Layout title_news_input;
    private EditText title_news_edittxt;
    private Custom_TextInput_Layout description_news_input;
    private EditText description_news_edittxt;
    private Custom_TextInput_Layout address_location_input;
    private EditText address_location_edittxt;
    private Spinner category_news_spinner;
    private Custom_Text_View choosing_photo_news_txt_view;
    private LinearLayout camera_linear_news;
    private LinearLayout gallery_linear_news;
    private ImageView photo_choosed_news;
    private Button register_news_btn;

    private Button get_user_location;

    private CheckBox mCheckBox_Agreement;

    private String selected_cat_news;
    private String title_choosed_news;
    private String description_choosed_news;
    private String address_choosed_news;

    private  LatLng user_location = null;

    private String[] cat_list = {
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

    //---------variables for photo-------------
    private int my_requestCode = 1;
    private int my_requestCode_Gallery = 2;
    private Bitmap my_bitmap;

    private Uri imageUri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mDrawer_right = (DrawerLayout) findViewById(R.id.drawerlayout_location_address);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer_right, 0, 0);
        mDrawer_right.setDrawerListener(mDrawerToggle);


        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }



        //---------------------------------------binding views to their id -------------------------
        title_news_input = (Custom_TextInput_Layout) findViewById(R.id.right_drawer_new_location_title_inputlayout);
        title_news_edittxt = (EditText) findViewById(R.id.right_drawer_new_location_title_edittxt);
        description_news_input = (Custom_TextInput_Layout) findViewById(R.id.right_drawer_new_location_description_inputlayout);
        description_news_edittxt = (EditText) findViewById(R.id.right_drawer_new_location_description_edittxt);
        address_location_input = (Custom_TextInput_Layout) findViewById(R.id.right_drawer_new_location_address_inputlayout);
        address_location_edittxt = (EditText) findViewById(R.id.right_drawer_new_location_address_edittxt);
        category_news_spinner = (Spinner) findViewById(R.id.right_drawer_new_location_categories_spinner);
        choosing_photo_news_txt_view = (Custom_Text_View) findViewById(R.id.right_drawer_new_location_choosing_photo_text_view);
        camera_linear_news = (LinearLayout) findViewById(R.id.right_drawer_new_location_photo_camera_linear);
        gallery_linear_news = (LinearLayout) findViewById(R.id.right_drawer_new_location_photo_library_linear);
        photo_choosed_news = (ImageView) findViewById(R.id.right_drawer_new_location_photo_choosed);
        register_news_btn = (Button) findViewById(R.id.right_drawer_new_location_register_btn);

        get_user_location = (Button) findViewById(R.id.right_drawer_new_location_map_btn);

        mCheckBox_Agreement = (CheckBox) findViewById(R.id.right_drawer_new_location_agreement_checkbox);

        //-------------------------------------getting category list names--------------------------

       /* String[] items = new String[cat_list.size()];
        for (int i = 0; i < cat_list.size(); i++) {
            items[i] = cat_list.get(i).getName();
        }*/
        //  ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_spinner_category, cat_list);
        adapter.setDropDownViewResource(R.layout.custom_spinner_category);
        category_news_spinner.setAdapter(adapter);

        category_news_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(DrawerActivity.this, "" + cat_list.get(i).getId(), Toast.LENGTH_SHORT).show();
                selected_cat_news = i+1+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //----------------------------------------photo choosing linear clicklistener---------------



        camera_linear_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {


                    if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "New Picture");
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");

// TODO: 7/9/2018 bayad check permisson konim o motmaen shim be storage dasteresi dare , hamchenin resolution axe ro ke migigirim bayad kochick konim o befrestim ke inghadr bozorg zakhire nashe
                        //joftesh hal shod :))))))))))) 8/13/2018

                        imageUri = getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                        startActivityForResult(intent, my_requestCode);

                   /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent , my_requestCode);*/

                    } else {
                        new StyleableToast.Builder(DrawerActivity_location_address.this).text("متاسفانه امکان استفاده از دوربین  نیست !").textColor(Color.WHITE).backgroundColor(getResources().getColor(R.color.red_map)).cornerRadius(5).show();

                      //  Toast.makeText(DrawerActivity_location_address.this, "متاسفانه امکان استفاده از دوربین موجود نیست !", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    requestPermission();
                }

            }
        });

        gallery_linear_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent , my_requestCode_Gallery);

            }
        });



        get_user_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DrawerActivity_location_address.this , Map_location_user.class);
                startActivityForResult(intent , 16);
            }
        });






        //----------------------------------------register btn clicked------------------------------

        register_news_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Submit_Ad();
            }
        });



    }


    public void Submit_Ad() {
        if (!validateTitle()) {
            return;
        }
        if (!validateDescription()) {
            return;
        }
        if (!validateLocation()) {
            return;
        }
        if (!validateAddress()) {
            return;
        }

        if (!validatePhoto()) {
            return;
        }
        if (!validateAgreement()) {
            return;
        }

        //register_news_btn.setVisibility(View.GONE);

        //user_username
        //selected_cat
        //my_final_Image
        title_choosed_news = title_news_edittxt.getText().toString();
        description_choosed_news = description_news_edittxt.getText().toString();
        address_choosed_news = address_location_edittxt.getText().toString();

        Bitmap image_big = ((BitmapDrawable) photo_choosed_news.getDrawable() ).getBitmap();
        Bitmap image = getResizedBitmap(image_big, 500);
        //encoding image
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG , 40 , outputStream);
        String encodedImage = Base64.encodeToString(outputStream.toByteArray() , Base64.DEFAULT);

        //sending data to BackgroundTask_Register_Ad
        String method = "register_location";
        String str_latitude = String.valueOf(user_location.latitude);
        String str_longitude = String.valueOf(user_location.longitude);
        BackgroundTask_Register_User_location backgroundTask_register_user_location = new BackgroundTask_Register_User_location(DrawerActivity_location_address.this);
        backgroundTask_register_user_location.execute( method , title_choosed_news , description_choosed_news , address_choosed_news,  selected_cat_news , encodedImage ,  str_latitude , str_longitude);

        //cleaning data
        title_news_edittxt.setText("");
        description_news_edittxt.setText("");
        address_location_edittxt.setText("");
        photo_choosed_news.setVisibility(View.GONE);
        user_location = null;
        mCheckBox_Agreement.setChecked(false);

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }

    //-------------------------------field's Validation ----------------------------------------------

    private boolean validateLocation() {

        if (user_location == null || user_location.latitude == 0 || user_location.longitude == 0) {
            //mTextInputLayout_Name.setError(getString(R.string.err_msg_name));
            get_user_location.setBackgroundColor(getResources().getColor(R.color.red_map));
            requestFocus(get_user_location);
            Toast.makeText(this, "مکان شغل خود را انتخاب فرمایید", Toast.LENGTH_SHORT).show();
            new StyleableToast.Builder(this).text("مکان شغل خود را باید انتخاب فرمایید").textColor(Color.WHITE).backgroundColor(getResources().getColor(R.color.red_map)).cornerRadius(5).show();

            return false;
        } else {
            get_user_location.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        return true;
    }

    private boolean validateTitle() {
        if (title_news_edittxt.getText().toString().trim().isEmpty()) {
            //mTextInputLayout_Name.setError(getString(R.string.err_msg_name));
            title_news_input.setError("عنوان نمی تواند خالی باشد!");
            requestFocus(title_news_input);
            return false;
        } else {
            title_news_input.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateDescription() {
        if (description_news_edittxt.getText().toString().trim().isEmpty()) {
            //mTextInputLayout_Name.setError(getString(R.string.err_msg_name));
            description_news_input.setError("توضیحات نمی تواند خالی باشد!");
            requestFocus(description_news_input);
            return false;
        } else {
            description_news_input.setErrorEnabled(false);
        }
        return true;
    }


    private boolean validateAddress() {
        if (address_location_edittxt.getText().toString().trim().isEmpty()) {
            //mTextInputLayout_Name.setError(getString(R.string.err_msg_name));
            address_location_input.setError("آدرس الزامی می باشد!");
            requestFocus(address_location_input);
            return false;
        }
        else {
            address_location_input.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePhoto() {
        if (photo_choosed_news.getDrawable() == null) {
            //mTextInputLayout_Name.setError(getString(R.string.err_msg_name));
            choosing_photo_news_txt_view.setText("لطفا برای شغل خود تصویر انتخاب کنید :");
            choosing_photo_news_txt_view.setTextColor(Color.RED);
            requestFocus(choosing_photo_news_txt_view);
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        // baraye validtate name lazeme o bayad bashe
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateAgreement() {
        if (mCheckBox_Agreement.isChecked()) {
            mCheckBox_Agreement.setError(null);
            return true;
        } else {
            mCheckBox_Agreement.setError("شرایط را فراموش کردید !");
            requestFocus(mCheckBox_Agreement);
            return false;
        }
    }

    //------------------------------------photo getting from camera---------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //---------------gereftan pic az camera-----------------------------------------------------
        if ( requestCode == my_requestCode && resultCode==RESULT_OK){
            try {
               /* Bundle e = data.getExtras();
                my_bitmap = (Bitmap) e.get( "data" );
*/
                my_bitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imageUri);
                photo_choosed_news.setImageBitmap(my_bitmap);
                photo_choosed_news.setVisibility(View.VISIBLE);
                choosing_photo_news_txt_view.setText("تصویر انتخاب شده شما :");
                choosing_photo_news_txt_view.setTextColor(getResources().getColor(R.color.icons));


               /* my_bitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imageUri);
                photo_choosed.setImageBitmap(my_bitmap);*/

                String imageurl = getRealPathFromURI(imageUri);
                Log.d(TAG, "onActivityResult: image"+ imageurl);




            }catch (Exception e){
                Log.d(TAG, "onActivityResult: "+ e.toString());
            }
        }
        else if ( requestCode == my_requestCode_Gallery && resultCode==RESULT_OK){

            Uri image = data.getData();


            photo_choosed_news.setImageURI(image);
            photo_choosed_news.setVisibility(View.VISIBLE);
            choosing_photo_news_txt_view.setText("تصویر انتخاب شده شما :");
            choosing_photo_news_txt_view.setTextColor(getResources().getColor(R.color.icons));

        }
        else if ((requestCode == my_requestCode && resultCode!=RESULT_OK) || (requestCode == my_requestCode_Gallery && resultCode!=RESULT_OK)){
           // Toast.makeText(this, "دریافت عکس با خطا مواجه شد !", Toast.LENGTH_SHORT).show();
            new StyleableToast.Builder(this).text("عکس دریافت نشد !").textColor(Color.WHITE).backgroundColor(getResources().getColor(R.color.red_map)).cornerRadius(5).show();

        }
        else if (resultCode == 16){
            Double latitude = data.getDoubleExtra("latitude" ,0);
            Double longitude = data.getDoubleExtra("longitude" ,0);
            Log.d(TAG, "onActivityResult: "+ latitude +"--" + longitude);
            user_location  = new LatLng(latitude , longitude);
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    //-----------------------------resize imgae
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    //-------------------------------cheking permission--------------------

    public static final int RequestPermissionCode = 1;
    private void requestPermission() {

        ActivityCompat.requestPermissions(DrawerActivity_location_address.this, new String[]
                {
                        READ_EXTERNAL_STORAGE
                }, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;


                    if (CameraPermission  ) {
                        new StyleableToast.Builder(DrawerActivity_location_address.this).text("حالا میتوانید از دوربین استفاده نمایید.").textColor(Color.WHITE).backgroundColor(getResources().getColor(R.color.colorPrimary)).cornerRadius(5).show();

                        Toast.makeText(DrawerActivity_location_address.this, "حالا میتوانید از دوربین استفاده نمایید", Toast.LENGTH_LONG).show();
                    }
                    else {
                        //Toast.makeText(DrawerActivity_location_address.this,"متاسفانه اجازه ندادید!",Toast.LENGTH_LONG).show();
                        new StyleableToast.Builder(DrawerActivity_location_address.this).text("متاسفانه اجازه ندادید!").textColor(Color.WHITE).backgroundColor(getResources().getColor(R.color.red_map)).cornerRadius(5).show();

                    }
                }

                break;
        }
    }

    public boolean checkPermission() {


        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return SecondPermissionResult == PackageManager.PERMISSION_GRANTED ;
    }


}