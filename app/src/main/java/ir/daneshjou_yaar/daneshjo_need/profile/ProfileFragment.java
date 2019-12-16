package ir.daneshjou_yaar.daneshjo_need.profile;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import ir.daneshjou_yaar.Custom_Text_View;
import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.daneshjo_need.DownloadStatus;
import ir.daneshjou_yaar.daneshjo_need.profile.user_posts.Userposts_Activity;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by iqfarhad on 1/28/2018.
 */

public class ProfileFragment extends Fragment implements Getting_user_hashed_pass.onDataAvailable ,Recycler_Item_Click_Listener.OnRecyclerClickListener {

    private ImageView mProfile_image;

    private LinearLayout mFirst_linear_layout;
    private Button mLogin_btn;
    private Button mSignup_btn;

    private LinearLayout mSecond_linear_layout;
    private TextInputLayout mInput_username;
    private EditText mLogin_username;
    private TextInputLayout mInput_password;
    private EditText mLogin_password;
    private Button mLogin__signin_btn;
    private Button mLogin_return_btn;

    private RecyclerView mRecyclerView_list;

    private LinearLayout mProfile_not_signed_in;
    private LinearLayout mUser_signedin_linear;
    private Custom_Text_View mUser_signedin_name;
    private Button mUser_sign_out_btn;

    private String signed_in;
    private String user_signed_up_name ="کاربر گرامی خوش آمدید :";
    private static final String TAG = "ProfileFragment";

    private boolean matched = false;

    private LinearLayout sign_in_btn_linear;
    private LinearLayout sign_in_progressbar_linear;
    private ProgressBar sign_in_progressbar;

    private String  username ;

    public static Boolean user_signed_in = false;

    public static ProfileFragment newInstance() {
        ProfileFragment fm = new ProfileFragment();
        return fm;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

           View v = inflater.inflate(R.layout.fragment_daneshjo_need_profile, container, false);


        username =  PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("username_user", "");


//------------------------------------seting layput and their id ------------------------------------------

        mProfile_image = (ImageView) v.findViewById(R.id.profile_image);

        //-------------------first view -------------------------------------------------------

        mFirst_linear_layout = (LinearLayout) v.findViewById(R.id.linear_layout_first_login_signup);
        mLogin_btn = (Button) v.findViewById(R.id.login_btn);
        mSignup_btn = (Button) v.findViewById(R.id.sign_up_btn);

        //-----------------second layout variables define --------------------------------------


        mSecond_linear_layout = (LinearLayout) v.findViewById(R.id.linear_layout_second_login);
        mInput_username = (TextInputLayout) v.findViewById(R.id.inputLayout_username);
        mLogin_username = (EditText) v.findViewById(R.id.linear_layout_second_login_username);
        mInput_password = (TextInputLayout) v.findViewById(R.id.inputLayout_password);
        mLogin_password = (EditText) v.findViewById(R.id.linear_layout_second_login_password);
        mLogin__signin_btn = (Button) v.findViewById(R.id.sign_in_profile_btn);
        mLogin_return_btn = (Button) v.findViewById(R.id.return_to_first_linear_layout_btn);

        mLogin_username.addTextChangedListener(new MyTextWatcher(mLogin_username));
        mLogin_password.addTextChangedListener(new MyTextWatcher(mLogin_password));

        //---------------recycler view bottom of signin layout---------------------------------

        mRecyclerView_list = (RecyclerView) v.findViewById(R.id.fragment_profile_recyclerview);
        mRecyclerView_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        //------------signed in ui layout ----------------------

        mProfile_not_signed_in = (LinearLayout) v.findViewById(R.id.profile_linear_layout_not_signed_in);
        mUser_signedin_linear = (LinearLayout) v.findViewById(R.id.profile_user_signed_in_linear);
        mUser_signedin_name = (Custom_Text_View) v.findViewById(R.id.profile_user_name);
        mUser_sign_out_btn = (Button) v.findViewById(R.id.profile_sign_out_btn);

        //-----------------------sign_in staff----------------------

        sign_in_btn_linear = (LinearLayout) v.findViewById(R.id.sign_in_login_btn_linear);
        sign_in_progressbar = (ProgressBar) v.findViewById(R.id.sign_in_progressBar);
        sign_in_progressbar_linear = (LinearLayout) v.findViewById(R.id.sign_in_progressbar_linear);

//----------------------------------chaning visibilty of layout -----------------------------------------

        /*if (signed_in.equalsIgnoreCase("signed_up") ) {
            mProfile_not_signed_in.setVisibility(View.GONE);
            mFirst_linear_layout.setVisibility(View.GONE);
            mSecond_linear_layout.setVisibility(View.GONE);
            mUser_signedin_linear.setVisibility(View.VISIBLE);
            mUser_signedin_name.setText(user_signed_up_name);
        } else if (signed_in.equalsIgnoreCase("signed_in") && user_signed_in) {
            mProfile_not_signed_in.setVisibility(View.GONE);
            mFirst_linear_layout.setVisibility(View.GONE);
            mSecond_linear_layout.setVisibility(View.GONE);
            mUser_signedin_linear.setVisibility(View.VISIBLE);
            mUser_signedin_name.setText(username);
            user_signed_in = true;
        }*/

        if (signed_in.equalsIgnoreCase("signed_in") || user_signed_in) {
            mProfile_not_signed_in.setVisibility(View.GONE);
            mFirst_linear_layout.setVisibility(View.GONE);
            mSecond_linear_layout.setVisibility(View.GONE);
            mUser_signedin_linear.setVisibility(View.VISIBLE);
            mUser_signedin_name.setText(username);
            user_signed_in = true;
        }
        /*if (username.equalsIgnoreCase("")){
            mProfile_not_signed_in.setVisibility(View.VISIBLE);
            mFirst_linear_layout.setVisibility(View.VISIBLE);
            mSecond_linear_layout.setVisibility(View.GONE);
            mUser_signedin_linear.setVisibility(View.GONE);
        }*/

            mLogin_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mUser_signedin_linear.setVisibility(View.GONE);
                    mFirst_linear_layout.setVisibility(View.GONE);
                    mProfile_image.setVisibility(View.GONE);
                    mProfile_not_signed_in.setVisibility(View.VISIBLE);
                    mSecond_linear_layout.setVisibility(View.VISIBLE);
                }
            });

            mLogin_return_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mUser_signedin_linear.setVisibility(View.GONE);
                    mSecond_linear_layout.setVisibility(View.GONE);
                    mProfile_image.setVisibility(View.VISIBLE);
                    mProfile_not_signed_in.setVisibility(View.VISIBLE);
                    mFirst_linear_layout.setVisibility(View.VISIBLE);
                }
            });

//------------------------------------intent to sign up actiity -------------------------------------------

        mSignup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Profile_sign_up.class);
                startActivityForResult(intent, 1);
            }
        });

        
        
//---------------------------------------Sign out of user btn ---------------------------------------------
        mUser_sign_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("signed_in", "signed_out").apply();
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("user_signed_up_name", "").apply();
               // Toast.makeText(getActivity(), "شما از حساب کاربری خود خارج شدید", Toast.LENGTH_SHORT).show();
                new StyleableToast.Builder(getActivity()).text("شما از حساب کاربری خود خارج شدید").textColor(Color.WHITE).backgroundColor(getResources().getColor(R.color.colorPrimary)).cornerRadius(5).show();
                /*FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(ProfileFragment.this).attach(ProfileFragment.this).commit();*/
                signed_in = "signed_out";

                sign_in_btn_linear.setVisibility(View.VISIBLE);
                sign_in_progressbar_linear.setVisibility(View.GONE);
                sign_in_progressbar.setVisibility(View.GONE);

                mUser_signedin_linear.setVisibility(View.GONE);
                mSecond_linear_layout.setVisibility(View.GONE);
                mProfile_image.setVisibility(View.VISIBLE);
                mProfile_not_signed_in.setVisibility(View.VISIBLE);
                mFirst_linear_layout.setVisibility(View.VISIBLE);
                user_signed_in = false;

            }
        });
        
        
//--------------------------------------Sign in user pass and id checking ----------------------------------
        
        mLogin__signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                sign_in_btn_linear.setVisibility(View.GONE);
                sign_in_progressbar_linear.setVisibility(View.VISIBLE);
                sign_in_progressbar.setVisibility(View.VISIBLE);

                if (submitForm()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!validateLogin()){
                               // Toast.makeText(getActivity(), "اطلاعات درست نمی باشد", Toast.LENGTH_SHORT).show();
                                new StyleableToast.Builder(getActivity()).text("اطلاعات درست نمی باشد !").textColor(Color.WHITE).backgroundColor(getResources().getColor(R.color.red_map)).cornerRadius(5).show();
                                sign_in_progressbar_linear.setVisibility(View.GONE);
                                sign_in_progressbar.setVisibility(View.GONE);
                                sign_in_btn_linear.setVisibility(View.VISIBLE);
                                user_signed_in = false;
                            }else{
                                user_signed_in = true;
                                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("signed_in", "signed_in").apply();
                                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("username_user", username).apply();
                                mUser_signedin_name.setText(username);
                                sign_in_progressbar_linear.setVisibility(View.GONE);
                                sign_in_progressbar.setVisibility(View.GONE);
                                mProfile_not_signed_in.setVisibility(View.GONE);
                                mFirst_linear_layout.setVisibility(View.GONE);
                                mSecond_linear_layout.setVisibility(View.GONE);
                                mUser_signedin_linear.setVisibility(View.VISIBLE);

                            }
                        }
                    }, 2000);


                }else
                {
                    //Toast.makeText(getActivity(), "اطلاعات درست نمی باشد", Toast.LENGTH_SHORT).show();
                    new StyleableToast.Builder(getActivity()).text("اطلاعات درست نمی باشد !").textColor(Color.WHITE).backgroundColor(getResources().getColor(R.color.red_map)).cornerRadius(5).show();
                    sign_in_progressbar_linear.setVisibility(View.GONE);
                    sign_in_progressbar.setVisibility(View.GONE);
                    sign_in_btn_linear.setVisibility(View.VISIBLE);
                    user_signed_in = false;

                }
            }
        });


        //مشکل تداخل زمانی چک کردن و گرفتن پسورد
        
        
//------------------------------------recyclerview menu item -----------------------------------------------

        ArrayList names = new ArrayList();
        names.add("درج آگهی");
        names.add("آگهی های من");
        //names.add("تنظیمات");
        //badan behesh ezafe konim

        ArrayList image = new ArrayList();
        image.add(R.drawable.ic_note_add_white_48dp);
        image.add(R.drawable.ic_bookmark_border_white_48dp);
       // image.add(R.drawable.ic_settings_white_48dp);

        Recycler_view_profile_Adapter recycler_view_profile_adapter = new Recycler_view_profile_Adapter(getActivity(), names, image);
        mRecyclerView_list.setAdapter(recycler_view_profile_adapter);

        mRecyclerView_list.addOnItemTouchListener(new Recycler_Item_Click_Listener(getActivity() , mRecyclerView_list , this));


        return v;
    }//END of onCreateview

    @Override
    public void onResume() {
        signed_in = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("signed_in", "");
        user_signed_up_name =PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("user_signed_up_name", "");

        //bayad taviz view o refresh shodan fragment baraye khondan sharedprefrence ro dorost konam (taghriban dorost shod
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        signed_in = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("signed_in", "");
        user_signed_up_name =PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("user_signed_up_name", "");
        super.onAttach(context);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //--------------------------karbar register kone , request code 1 == Ok barmigarde o ui taghir mikone -----------------------
        if (requestCode == 1) {
            if (resultCode == 20) {
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("signed_in", "signed_in").apply();
               // PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("user_signed_up_name",data.getStringExtra("user_name") ).apply();
                user_signed_in = true;
                mProfile_not_signed_in.setVisibility(View.GONE);
                mFirst_linear_layout.setVisibility(View.GONE);
                mSecond_linear_layout.setVisibility(View.GONE);
                mUser_signedin_linear.setVisibility(View.VISIBLE);
                mUser_signedin_name.setText(data.getStringExtra("user_name"));
                username = data.getStringExtra("user_name");
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("username_user", username).apply();
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean submitForm() {
        //validate kardan etelaat vorodi o sign in user
        username = mLogin_username.getText().toString();
        if (!validateUserName()) {
            return false;
        }
        if (!validatePassword()) {
            return false;
        }
        Getting_user_hashed_pass getpass = new Getting_user_hashed_pass("http://eqtech.ir/get_pass.php" , username , this);
        getpass.execute("");

            return true;


       /* String generatedSecuredPasswordHash = null;
        try {
            generatedSecuredPasswordHash = Hashing_password.generateStorngPasswordHash(originalPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "submitForm: "+generatedSecuredPasswordHash);*/

        /*boolean matched = false;
        try {
            matched = Hashing_password.validatePassword(originalPassword, generatedSecuredPasswordHash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "submitForm: "+matched);

        try {
            matched = Hashing_password.validatePassword("farhad", generatedSecuredPasswordHash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "submitForm: "+matched);*/

    }

    private boolean validateLogin() {
        Log.d(TAG, "validateLogin: starts");

        if (!matched){
            //Toast.makeText(getActivity(), "خطا", Toast.LENGTH_SHORT).show();
            new StyleableToast.Builder(getActivity()).text("خطا ! مجددا تلاش نمایید.").textColor(Color.WHITE).backgroundColor(getResources().getColor(R.color.red_map)).cornerRadius(5).show();
            return false;
        }
        else return true;
    }

    @Override
    public void onDataAvailable(String data, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable : starts");

        if (status == DownloadStatus.OK && data!=null){

            Log.e(TAG, "onDataAvailable: successful" );

            String generatedSecuredPasswordHash = data;
            String  originalPassword = mLogin_password.getText().toString();

            try {
                matched = Hashing_password.validatePassword(originalPassword, generatedSecuredPasswordHash);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "submitForm: " + matched);


        }else{
            //download or processing failed
            Log.e(TAG, "onDataAvailavle: failed with status" + status );
        }

        Log.d(TAG, "onDataAvailable: ends");
    }

    @Override
    public void onItemClick(View view, int position) {
        if ( (signed_in.equalsIgnoreCase("signed_in") && user_signed_in) || (signed_in.equalsIgnoreCase("signed_up") && user_signed_in )  || user_signed_in ){
            if (position==0){
               // Toast.makeText(getActivity(), "درج آگهی", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity() , User_Posting_New_Ad.class);
                intent.putExtra("username" , username);
                startActivity(intent);
            }
            else if (position == 1){
               // Toast.makeText(getActivity(), "آگهی های من", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity() , Userposts_Activity.class);
                intent.putExtra("id" , username);
                startActivity(intent);
            }
            else if (position == 2){
                Toast.makeText(getActivity(), "تنظیمات", Toast.LENGTH_SHORT).show();
            }
        }
        else{
           // Toast.makeText(getActivity(), "ابتدا لطفا وارد اکانت خود شوید !", Toast.LENGTH_SHORT).show();
            new StyleableToast.Builder(getActivity()).text("ابتدا لطفا وارد اکانت خود شوید.").textColor(Color.WHITE).backgroundColor(getResources().getColor(R.color.colorPrimary)).cornerRadius(5).show();

        }
    }

    @Override
    public void OnItemLongClick(View view, int position) {

    }

    private class MyTextWatcher implements TextWatcher {
        //baraye validate kardan user o pass niaz ast
        private View view;
        private MyTextWatcher(View view) {
            this.view = view;
        }
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.sign_up_form_name:
                    validateUserName();
                    break;
                case R.id.sign_up_form_family:
                    validatePassword();
                    break;
            }
        }
    }

    private void requestFocus(View view) {
        //baraye validate kardan niaze
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateUserName() {
        if (mLogin_username.getText().toString().trim().isEmpty()) {
            //mTextInputLayout_Name.setError(getString(R.string.err_msg_name));
            mInput_username.setError("نام خود را وارد کنید!");
            requestFocus(mLogin_username);
            return false;
        } else {
            mInput_username.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        if (mLogin_password.getText().toString().trim().isEmpty()) {
            // mTextInputLayout_Password.setError(getString(R.string.err_msg_password));
            mInput_password.setError("این فیلد نمیتواند خالی باشد!");
            requestFocus(mLogin_password);
            return false;
        }else if (mLogin_password.getText().toString().trim().length() < 6 ){
            mInput_password.setError("پسورد باید بیش از 6 کاراکتر باشد!");
            requestFocus(mLogin_password);
            return false;
        }
        else {
            mInput_password.setErrorEnabled(false);
        }

        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
