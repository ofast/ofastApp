package com.hung.ofastapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hung.ofastapp.CreateConnection.JSONParser;
import com.hung.ofastapp.CreateConnection.ofastURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class Register extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{
    UserRegisterTask eRegisterTask;
    Typeface tf1, tf2;
    ImageButton imgbtn_back;
    TextView txtv_title;
    EditText edt_username,edt_password, edt_numberphone;
    Button btn_login, btn_register,btn_register_box;
    View focusView;
    LinearLayout linear_registerbox;
    SpotsDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        //------------------------------------------------------------------------------------------
        //---------------------------Animation for Box Register-------------------------------------
        //------------------------------------------------------------------------------------------
        linear_registerbox = (LinearLayout) findViewById(R.id.linear_registerbox);
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
         linear_registerbox.startAnimation(rotation);
        //------------------------------------------------------------------------------------------
        //-------------------------------------Ánh xạ---------------------------------------------
        //------------------------------------------------------------------------------------------
        imgbtn_back = (ImageButton)findViewById(R.id.imgbtn_back);
        txtv_title = (TextView)findViewById(R.id.txtv_title);
        edt_username = (EditText)findViewById(R.id.edt_username);
        edt_password =(EditText)findViewById(R.id.edt_password);
        edt_numberphone =(EditText)findViewById(R.id.edt_numberphone);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_register = (Button)findViewById(R.id.btn_register);
        btn_register_box = (Button)findViewById(R.id.btn_register_box);
        progressDialog = new SpotsDialog(Register.this, R.style.progress_register);

        //------------------------------------------------------------------------------------------
        //-------------------------------------Set font---------------------------------------------
        //------------------------------------------------------------------------------------------
        tf1 = Typeface.createFromAsset(getAssets(),"VNF-Futura Regular.ttf");
        tf2 = Typeface.createFromAsset(getAssets(),"VKORIN.TTF");
        edt_username.setTypeface(tf1);
        edt_password.setTypeface(tf1);
        edt_numberphone.setTypeface(tf1);
        txtv_title.setTypeface(tf2);
        btn_register_box.setTypeface(tf2);
        //------------------------------------------------------------------------------------------
        //----------------------------Bỏ gạch chân cho EditText-------------------------------------
        //------------------------------------------------------------------------------------------
        edt_username.setBackgroundColor(Color.TRANSPARENT);
        edt_password.setBackgroundColor(Color.TRANSPARENT);
        edt_numberphone.setBackgroundColor(Color.TRANSPARENT);
        //------------------------------------------------------------------------------------------
        //-----------------------------Set Event for ImageButton------------------------------------
        //------------------------------------------------------------------------------------------
        imgbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login_and_Register.class);
                startActivity(intent);
            }
        });
        //------------------------------------------------------------------------------------------
        //-----------------------------Set Event for btn_login--------------------------------------
        //------------------------------------------------------------------------------------------
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
        //------------------------------------------------------------------------------------------
        //-------------------------Set Event for btn_register_box-----------------------------------
        //------------------------------------------------------------------------------------------
        btn_register_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected() == true) {
                    attemptLogin();

                } else {
                    Toast.makeText(getApplication(), "Vui Lòng Kiểm Tra Kết Nối Mạng", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
    //----------------------------------------------------------------------------------------------
    //-------------------------------Kiểm tra sự hợp lệ ở App---------------------------------------
    //--------------------------------đối với dữ liệu nhập vào--------------------------------------
    //----------------------------------------------------------------------------------------------
    private void attemptLogin() {
        if (eRegisterTask != null) {
            return;
        }

        // Reset errors.
        edt_username.setError(null);
        edt_numberphone.setError(null);
        edt_password.setError(null);

        // Store values at the time of the login attempt.
        String username = edt_username.getText().toString();
        String password = edt_password.getText().toString();
        String numberphone = edt_numberphone.getText().toString();

        boolean cancel = false;
         focusView = null;
        // Kiểm tra trống Pass
        if (TextUtils.isEmpty(password) ) {
            edt_password.setError(getString(R.string.error_field_required));
            focusView = edt_password;
            cancel = true;
        }
        // Kiểm tra trống NumberPhone
        if (TextUtils.isEmpty(numberphone) ) {
            edt_numberphone.setError(getString(R.string.error_field_required));
            focusView = edt_numberphone;
            cancel = true;
        }
        // Kiểm tra trống Username
        if (TextUtils.isEmpty(username)) {
            edt_username.setError(getString(R.string.error_field_required));
            focusView = edt_username;
            cancel = true;
        }
        // Kiểm tra Độ dài Username
        if (CheckLenght(username)==true) {
            edt_username.setError(getString(R.string.error_lenght));
            focusView = edt_username;
            cancel = true;
        }
        // Kiểm tra Độ dài Password
        if (CheckLenght(password)==true) {
            edt_password.setError(getString(R.string.error_lenght));
            focusView = edt_username;
            cancel = true;
        }
        // Kiểm tra Kiểu dữ liệu nhập vào của Username
        if(CheckTypeInput(username) == false)
        {
            edt_username.setError("Type input: a-z,0-9");
            focusView = edt_username;
            cancel = true;
        }
        // Kiểm tra Kiểu dữ liệu nhập vào của Password
        if(CheckTypeInput(password) == false)
        {
            edt_password.setError("Type input: a-z,0-9");
            focusView = edt_password;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            eRegisterTask = new UserRegisterTask(username, password,numberphone);
            eRegisterTask.execute((Void) null);
        }
    }
    //----------------------------------------------------------------------------------------------
    //------------------------------Kiểm tra độ dài nhập vào----------------------------------------
    //----------------------------------------------------------------------------------------------
    public static boolean CheckLenght(String string)
    {
       return (string.length()>0&&string.length()<6);
    }
    //----------------------------------------------------------------------------------------------
    //----------------------------Kiểm tra kiểu dữ liệu nhập vào------------------------------------
    //----------------------------------------------------------------------------------------------
    public static boolean CheckTypeInput(String string)
    {
        String[] type = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s",
                            "t","u","v","w","x","y","z","0","1","2","3","4","5","6","7","8","9"};
        String[] input = string.split("");
        for(int i=1; i<input.length;i++)
        {
            if (!Arrays.asList(type).contains(input[i]))
            {
                return false;
            }
        }
       return true;
    }

    //----------------------------------------------------------------------------------------------
    //----------------------Override LoaderManager.LoaderCallbacks<Cursor>--------------------------
    //----------------------------------------------------------------------------------------------
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<String> emails = new ArrayList<String>();
        data.moveToFirst();
        while (!data.isAfterLast()) {
            emails.add(data.getString(ProfileQuery.ADDRESS));
            data.moveToNext();
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    //----------------------------------------------------------------------------------------------
    //----------------------Kết nối Server và Xử lý Đăng ký-----------------------------------------
    //----------------------------------------------------------------------------------------------
    public class UserRegisterTask extends AsyncTask<Void, Void, JSONObject> {
        private final String mUsername;
        private final String mPassword;
        private final String mNumberphone;

        JSONParser jsonParser = new JSONParser();
        private  String REGISTER_URL = ofastURL.register;

        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";
        private static final String TAG_ERROR = "error";
                        /*Hàm Khởi tạo của Xử lý đăng kí*/
        UserRegisterTask(String username, String password, String numberphone) {
            mUsername = username;
            mPassword = password;
            mNumberphone =  numberphone;
        }
                        /*Hàm trước khi Xử lý đăng kí*/
        protected void onPreExecute() {
            progressDialog.show();
        }
                     /*Hàm kết nối tới Server \*/
        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("AppSignupForm[username]", mUsername);
                data.put("AppSignupForm[password]", mPassword);
                data.put("AppSignupForm[phone]",mNumberphone);
                Log.d("request", "starting");
                /*-----------------------------MakeHttpRequest-----------------------------------------
                ---------------Gửi yêu cầu lên server theo đường dẫn URL, phương thức POST, dữ liệu
                ---------------truyền vào là data------------------------------------------------*/
                JSONObject json = jsonParser.makeHttpRequest(
                        REGISTER_URL, "POST", data);

                if (json != null) {
                    return json;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
                     /*Nhận kết quả từ Server, bắt đầu xử lý dữ liệu trả về*/
        @Override
        protected void onPostExecute(final JSONObject result) {
            progressDialog.dismiss();

            eRegisterTask = null;
            if (result != null) {
                try {
                    Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_SHORT).show();
                    String success = result.getString(TAG_SUCCESS);
                    String message = result.getString(TAG_MESSAGE);
                    String error = result.getString(TAG_ERROR);
                    Log.d("success", success + message + error);
                    if (!success.equals("true")) {

                        if (error.equals("E111")) {
                            edt_username.setError(message);
                            focusView = edt_username;
                        }
                        if (error.equals("E112")) {

                            edt_numberphone.setError(message);
                            focusView = edt_numberphone;
                        }
                        if (error.equals("E113")) {

                           Toast.makeText(getApplicationContext(),"Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                       Intent intent = new Intent(Register.this, Login.class);
                        intent.putExtra("hasSignUp_username",edt_username.getText().toString());
                        intent.putExtra("hasSignUp_password",edt_password.getText().toString());
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Kết nối tới server thất bại", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            eRegisterTask = null;

        }
    }
    //----------------------------------------------------------------------------------------------
    //------------------------------Hàm kiểm tra kết nối Internet-----------------------------------
    //----------------------------------------------------------------------------------------------
    public boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Login.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
