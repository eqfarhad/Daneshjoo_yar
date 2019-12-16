package ir.daneshjou_yaar.daneshjo_need.profile;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.io.ByteArrayOutputStream;
import java.util.List;

import ir.daneshjou_yaar.Custom_TextInput_Layout;
import ir.daneshjou_yaar.Custom_Text_View;
import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.daneshjo_need.categories.Category_Model;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static ir.daneshjou_yaar.daneshjo_need.categories.CategoriesFragment.categories_list;

public class User_Posting_New_Ad extends AppCompatActivity {
    private static final String TAG = "User_Posting_New_Ad";

    private String user_username;

    private Custom_TextInput_Layout title_input;
    private EditText title_edittxt;
    private Custom_TextInput_Layout description_input;
    private EditText description_edittxt;
    private Custom_TextInput_Layout price_input;
    private EditText price_edittxt;
    private Spinner category_spinner;
    private Custom_Text_View choosing_photo_txt_view;
    private LinearLayout camera_linear;
    private LinearLayout gallery_linear;
    private ImageView photo_choosed;
    private Button register_ad_btn;

    private String selected_cat;
    private String title_choosed;
    private String description_choosed;
    private String price_choosed;

    private int counter_price_error = 1;
    private List<Category_Model> cat_list;

    //---------variables for photo-------------
    private int my_requestCode = 1;
    private int my_requestCode_Gallery = 2;
    private Bitmap my_bitmap;
    private String my_final_Image;

    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_posting_new_ad);

        //------------------getting username of  the user that wants to post this advertise---------

        Intent intent = getIntent();
        user_username = intent.getStringExtra("username");
        Log.d(TAG, "onCreate: username intent =" + user_username);

        //---------------------------------------binding views to their id -------------------------
        title_input = (Custom_TextInput_Layout) findViewById(R.id.new_ad_title_inputlayout);
        title_edittxt = (EditText) findViewById(R.id.new_ad_title_edittxt);
        description_input = (Custom_TextInput_Layout) findViewById(R.id.new_ad_description_inputlayout);
        description_edittxt = (EditText) findViewById(R.id.new_ad_description_edittxt);
        price_input = (Custom_TextInput_Layout) findViewById(R.id.new_ad_price_inputlayout);
        price_edittxt = (EditText) findViewById(R.id.new_ad_price_edittxt);
        category_spinner = (Spinner) findViewById(R.id.new_ad_categories_spinner);
        choosing_photo_txt_view = (Custom_Text_View) findViewById(R.id.new_ad_choosing_photo_text_view);
        camera_linear = (LinearLayout) findViewById(R.id.new_ad_photo_camera_linear);
        gallery_linear = (LinearLayout) findViewById(R.id.new_ad_photo_library_linear);
        photo_choosed = (ImageView) findViewById(R.id.new_ad_photo_choosed);
        register_ad_btn = (Button) findViewById(R.id.new_ad_register_btn);


        //-------------------------------------getting category list names--------------------------

        cat_list = categories_list;
        String[] items = new String[cat_list.size()];
        for (int i = 0; i < cat_list.size(); i++) {
            items[i] = cat_list.get(i).getName();
        }
      //  ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_spinner_category, items);
        adapter.setDropDownViewResource(R.layout.custom_spinner_category);
        category_spinner.setAdapter(adapter);



        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               // Toast.makeText(User_Posting_New_Ad.this, "" + cat_list.get(i).getId(), Toast.LENGTH_SHORT).show();
                selected_cat = cat_list.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //----------------------------------------photo choosing linear clicklistener---------------



            camera_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkPermission()) {


                        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "New Picture");
                            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");

// TODO: 7/9/2018 khate baadi ro bekhon :) bayad check permisson konim o motmaen shim be storage dasteresi dare , hamchenin resolution axe ro ke migigirim bayad kochick konim o befrestim ke inghadr bozorg zakhire nashe
                            //joftesh hal shod :))))))))))) 8/13/2018

                            imageUri = getContentResolver().insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                            startActivityForResult(intent, my_requestCode);

                   /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent , my_requestCode);*/

                        } else {
                            new StyleableToast.Builder(User_Posting_New_Ad.this).text("متاسفانه امکان استفاده از دوربین  نیست !").textColor(Color.WHITE).backgroundColor(getResources().getColor(R.color.red_map)).cornerRadius(5).show();
                          //  Toast.makeText(User_Posting_New_Ad.this, "متاسفانه دوربین موجود نیست !", Toast.LENGTH_SHORT).show();

                        }

                    } else {
                        requestPermission();
                    }

                }
            });

            gallery_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent , my_requestCode_Gallery);

                }
            });







        //----------------------------------------register btn clicked------------------------------

        register_ad_btn.setOnClickListener(new View.OnClickListener() {
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
        if (!validatePrice() && counter_price_error==1) {
            return;
        }
        counter_price_error =1;
        price_input.setErrorEnabled(false);

        if (!validatePhoto()) {
            return;
        }

        register_ad_btn.setVisibility(View.GONE);

        //user_username
        //selected_cat
        //my_final_Image
        title_choosed = title_edittxt.getText().toString();
        description_choosed = description_edittxt.getText().toString();
        price_choosed = price_edittxt.getText().toString();

        Bitmap image_big = ((BitmapDrawable) photo_choosed.getDrawable() ).getBitmap();
        Bitmap image = getResizedBitmap(image_big, 500);
        //encoding image
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG , 40 , outputStream);
        String encodedImage = Base64.encodeToString(outputStream.toByteArray() , Base64.DEFAULT);

        //sending data to BackgroundTask_Register_Ad
        String method = "register_ad";
        BackgroundTask_Register_Ad backgroundTask_register_ad = new BackgroundTask_Register_Ad(User_Posting_New_Ad.this);
        backgroundTask_register_ad.execute(method , user_username , title_choosed , description_choosed , price_choosed ,selected_cat , encodedImage);


    }

    //-------------------------------field's Validation ----------------------------------------------

    private boolean validateTitle() {
        if (title_edittxt.getText().toString().trim().isEmpty()) {
            //mTextInputLayout_Name.setError(getString(R.string.err_msg_name));
            title_input.setError("عنوان نمی تواند خالی باشد!");
            requestFocus(title_input);
            return false;
        } else {
            title_input.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateDescription() {
        if (description_edittxt.getText().toString().trim().isEmpty()) {
            //mTextInputLayout_Name.setError(getString(R.string.err_msg_name));
            description_input.setError("توضیحات نمی تواند خالی باشد!");
            requestFocus(description_input);
            return false;
        } else {
            description_input.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePrice() {
        if (price_edittxt.getText().toString().trim().isEmpty()) {
            //mTextInputLayout_Name.setError(getString(R.string.err_msg_name));
            price_input.setError("مبلغ در صورت خالی بودن توافقی در آگهی درج خواهد شد");
            counter_price_error--;
            requestFocus(price_input);
            return false;
        } else {
            price_input.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePhoto() {
        if (photo_choosed.getDrawable() == null) {
            //mTextInputLayout_Name.setError(getString(R.string.err_msg_name));
            choosing_photo_txt_view.setText("لطفا برای اگهی خود تصویر انتخاب کنید :");
            choosing_photo_txt_view.setTextColor(Color.RED);
            requestFocus(choosing_photo_txt_view);
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
                photo_choosed.setImageBitmap(my_bitmap);
                photo_choosed.setVisibility(View.VISIBLE);
                choosing_photo_txt_view.setText("تصویر انتخاب شده شما :");
                choosing_photo_txt_view.setTextColor(getResources().getColor(R.color.icons));


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


            photo_choosed.setImageURI(image);
            photo_choosed.setVisibility(View.VISIBLE);
            choosing_photo_txt_view.setText("تصویر انتخاب شده شما :");
            choosing_photo_txt_view.setTextColor(getResources().getColor(R.color.icons));

        }
        else{
            //Toast.makeText(this, "دریافت عکس با خطا مواجه شد !", Toast.LENGTH_SHORT).show();
            new StyleableToast.Builder(User_Posting_New_Ad.this).text("عکس دریافت نشد !").textColor(Color.WHITE).backgroundColor(getResources().getColor(R.color.red_map)).cornerRadius(5).show();



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

        ActivityCompat.requestPermissions(User_Posting_New_Ad.this, new String[]
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

                        //Toast.makeText(User_Posting_New_Ad.this, "اجازه داده شد", Toast.LENGTH_LONG).show();
                        new StyleableToast.Builder(User_Posting_New_Ad.this).text("حالا میتوانید از دوربین استفاده نمایید.").textColor(Color.WHITE).backgroundColor(getResources().getColor(R.color.colorPrimary)).cornerRadius(5).show();

                    }
                    else {
                        //Toast.makeText(User_Posting_New_Ad.this,"اجازه دسترسی وجود ندارد!",Toast.LENGTH_LONG).show();
                        new StyleableToast.Builder(User_Posting_New_Ad.this).text("متاسفانه اجازه ندادید!").textColor(Color.WHITE).backgroundColor(getResources().getColor(R.color.red_map)).cornerRadius(5).show();


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
