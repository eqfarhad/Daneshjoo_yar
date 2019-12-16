package ir.daneshjou_yaar.daneshjo_need.profile;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import ir.daneshjou_yaar.Custom_TextInput_Layout;
import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.daneshjo_need.DownloadStatus;

public class Profile_sign_up extends AppCompatActivity implements Checking_username_existence_sign_up.onDataAvailable{

    private Custom_TextInput_Layout mTextInputLayout_Name;
    private EditText mEditText_name;
    private Custom_TextInput_Layout mTextInputLayout_Family;
    private EditText mEditText_Family;
    private Custom_TextInput_Layout mTextInputLayout_Username;
    private EditText mEditText_Username;
    private Custom_TextInput_Layout mTextInputLayout_Email;
    private EditText mEditText_Email;
    private Custom_TextInput_Layout mTextInputLayout_Password;
    private EditText mEditText_Password;
    private Custom_TextInput_Layout mTextInputLayout_Password_Again;
    private EditText mEditText_Password_Again;
    private Custom_TextInput_Layout mTextInputLayout_Telegram;
    private EditText mEditText_Telegram;
    private Custom_TextInput_Layout mTextInputLayout_Phone;
    private EditText mEditText_Phone;

    private CheckBox mCheckBox_Agreement;
    private Button mButton_register;
    private static final String TAG = "Profile_sign_up";

    private boolean existence = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_sign_up);

        mTextInputLayout_Name = (Custom_TextInput_Layout) findViewById(R.id.sign_up_form_name_layout);
        mEditText_name = (EditText) findViewById(R.id.sign_up_form_name);

        mTextInputLayout_Family = (Custom_TextInput_Layout) findViewById(R.id.sign_up_form_family_layout);
        mEditText_Family = (EditText) findViewById(R.id.sign_up_form_family);

        mTextInputLayout_Username = (Custom_TextInput_Layout) findViewById(R.id.sign_up_form_username_layout);
        mEditText_Username = (EditText) findViewById(R.id.sign_up_form_username);

        mTextInputLayout_Password = (Custom_TextInput_Layout) findViewById(R.id.sign_up_form_password_layout);
        mEditText_Password = (EditText) findViewById(R.id.sign_up_form_password);

        mTextInputLayout_Password_Again = (Custom_TextInput_Layout) findViewById(R.id.sign_up_form_password_again_layout);
        mEditText_Password_Again = (EditText) findViewById(R.id.sign_up_form_password_again);

        mTextInputLayout_Email = (Custom_TextInput_Layout) findViewById(R.id.sign_up_form_email_layout);
        mEditText_Email = (EditText) findViewById(R.id.sign_up_form_email);

        mTextInputLayout_Telegram = (Custom_TextInput_Layout) findViewById(R.id.sign_up_form_telegram_layout);
        mEditText_Telegram = (EditText) findViewById(R.id.sign_up_form_telegram);

        mTextInputLayout_Phone = (Custom_TextInput_Layout) findViewById(R.id.sign_up_form_phone_layout);
        mEditText_Phone = (EditText) findViewById(R.id.sign_up_form_phone);

        mCheckBox_Agreement = (CheckBox) findViewById(R.id.sign_up_form_agreement_checkbox);
        mButton_register = (Button) findViewById(R.id.sign_up_form_register_btn);


        mEditText_name.addTextChangedListener(new MyTextWatcher(mEditText_name));
        mEditText_Family.addTextChangedListener(new MyTextWatcher(mEditText_Family));
        mEditText_Email.addTextChangedListener(new MyTextWatcher(mEditText_Email));
        mEditText_Password.addTextChangedListener(new MyTextWatcher(mEditText_Password));


        mButton_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

    }

    private void submitForm() {

        if (!validateName()) {
            return;
        }
        if (!validateFamily()) {
            return;
        }
        if (!validateUsername()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
        if (!validatePasswordAgain()) {
            return;
        }
        if (!validateTelegram_Phone()) {
            return;
        }
        if (!validateAgreement()) {
            return;
        }

       /* if (existence){
            mTextInputLayout_Username.setError("این نام کاربری موجود نمی باشد !");
            requestFocus(mEditText_Username);
            existence = false;
            return;
        }*/

        String name = mEditText_name.getText().toString();
        String family = mEditText_Family.getText().toString();
        String username = mEditText_Username.getText().toString();
        //get_user.php?u_users=username dorost shod bayad samte android kamel she
        String email = mEditText_Email.getText().toString();
        String telegram = mEditText_Telegram.getText().toString();
        String phone = mEditText_Phone.getText().toString();

        //pass hash
        String  originalPassword = mEditText_Password.getText().toString();
        String generatedSecuredPasswordHash = null;
        try {
            generatedSecuredPasswordHash = Hashing_password.generateStorngPasswordHash(originalPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "submitForm: "+generatedSecuredPasswordHash);
        //end hash pass


        String method = "register";
        BackgroundTask_Sign_Up backgroundTaskSignUp = new BackgroundTask_Sign_Up(Profile_sign_up.this);
        backgroundTaskSignUp.execute(method, name, family, username, generatedSecuredPasswordHash, email, telegram, phone);
        //------------------------to shared prefrence zzakhire mikonim baraye badan karbar omad to barname zakhir shode bashe -------
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("signed_in", "signed_up").apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("user_signed_up_name", new StringBuilder().append(name).append(" ").append(family).toString()).apply();
        //---------------------inja ham Ok mifrestim be fragment ta ui taghir kone -----------------------
        Intent intent = new Intent();
        intent.putExtra("signed_in","signed_in");
        intent.putExtra("user_name", username);
     //   intent.putExtra("user_signed_up_name" , "کاربر گرامی : "+name+" "+family);
        setResult(20, intent);
        finish();

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

    private boolean validateUsername() {
        Checking_username_existence_sign_up check = new Checking_username_existence_sign_up("http://eqtech.ir/get_user.php",mEditText_Username.getText().toString() ,this);
        check.execute("");
        if (mEditText_Username.getText().toString().trim().isEmpty()) {
            //mTextInputLayout_Name.setError(getString(R.string.err_msg_name));
            mTextInputLayout_Username.setError("نام کاربری مورد نظر خود را وارد کنید !");
            requestFocus(mEditText_Username);
            return false;
        }else if(mEditText_Username.getText().toString().contains("%") || mEditText_Username.getText().toString().contains("/") || mEditText_Username.getText().toString().contains("$")){
            mTextInputLayout_Username.setError("کاراکتر های / و %  و $ مجاز نمی  باشد!");
            requestFocus(mEditText_Username);
            return false;
        }else if (Checking_username_existence_sign_up.User_Exist()) {
            mTextInputLayout_Username.setError("این نام کاربری قبلا ایجاد شده است !");
            requestFocus(mEditText_Username);
            return false;
        }else{
            mTextInputLayout_Username.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePasswordAgain() {
        if (mEditText_Password_Again.getText().toString().trim().isEmpty()) {
            // mTextInputLayout_Password.setError(getString(R.string.err_msg_password));
            mTextInputLayout_Password_Again.setError("این فیلد نمیتواند خالی باشد!");
            requestFocus(mEditText_Password);
            return false;
        }else if (!(mEditText_Password_Again.getText().toString().equalsIgnoreCase(mEditText_Password.getText().toString())) ){
            mTextInputLayout_Password_Again.setError("پسورد های وارد شده مطابق یکدیگر نمی باشد!");
            requestFocus(mEditText_Password_Again);
            return false;
        }
        else {
            mTextInputLayout_Password_Again.setErrorEnabled(false);
        }
        return true;
    }


    private boolean validateName() {
        if (mEditText_name.getText().toString().trim().isEmpty()) {
            //mTextInputLayout_Name.setError(getString(R.string.err_msg_name));
            mTextInputLayout_Name.setError("نام خود را وارد کنید!");
            requestFocus(mEditText_name);
            return false;
        } else {
            mTextInputLayout_Name.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateFamily() {
        if (mEditText_Family.getText().toString().trim().isEmpty()) {
            //mTextInputLayout_Name.setError(getString(R.string.err_msg_name));
            mTextInputLayout_Family.setError("این فیلد خالی نمی تواند باشد!");
            requestFocus(mEditText_Family);
            return false;
        } else {
            mTextInputLayout_Family.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail() {
        String email = mEditText_Email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
          //  mTextInputLayout_Email.setError(getString(R.string.err_msg_email));
            mTextInputLayout_Email.setError("ایمیل معتبر نمی باشد!");
            requestFocus(mEditText_Email);
            return false;
        } else {
            mTextInputLayout_Email.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (mEditText_Password.getText().toString().trim().isEmpty()) {
           // mTextInputLayout_Password.setError(getString(R.string.err_msg_password));
            mTextInputLayout_Password.setError("این فیلد نمیتواند خالی باشد!");
            requestFocus(mEditText_Password);
            return false;
        }else if (mEditText_Password.getText().toString().trim().length() < 6 ){
            mTextInputLayout_Password.setError("پسورد باید بیش از 6 کاراکتر باشد!");
            requestFocus(mEditText_Password);
            return false;
        }
        else {
            mTextInputLayout_Password.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateTelegram_Phone() {
        if (mEditText_Telegram.getText().toString().trim().isEmpty() && mEditText_Phone.getText().toString().trim().isEmpty()) {
            //mTextInputLayout_Name.setError(getString(R.string.err_msg_name));
            mTextInputLayout_Telegram.setError("آیدی تلگرام خود و یا شماره تلفن خود را وارد کنید!");
            mTextInputLayout_Phone.setError("آیدی تلگرام خود و یا شماره تلفن خود را وارد کنید!");
            requestFocus(mEditText_Telegram);
            return false;
        } else {
            mTextInputLayout_Telegram.setErrorEnabled(false);
            mTextInputLayout_Phone.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onDataAvailable(String data, DownloadStatus status) {
       //TODO: inam dorost konim o spinner bezarim bad az inke fahimidim account tekrari nist besazim

       /* if (data.equalsIgnoreCase("1"))
            existence = true;
        else{
            existence = false;
            mTextInputLayout_Username.setErrorEnabled(false);
        }*/
    }

    private class MyTextWatcher implements TextWatcher {

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
                    validateName();
                    break;
                case R.id.sign_up_form_family:
                    validateFamily();
                    break;
                case R.id.sign_up_form_email:
                    validateEmail();
                    break;
                case R.id.sign_up_form_password:
                    validatePassword();
                    break;
            }
        }
    }
}
