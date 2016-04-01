package com.hung.ofastapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hung.ofastapp.CreateConnection.JSONParser;
import com.hung.ofastapp.CreateConnection.ofastURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login_and_Register extends Activity {
    private static int SPLASH_TIME_OUT = 3000;
    private boolean doubleBackToExitPressedOnce = false;
    public static final String TRYAPP = "LoginByTryApp";
    TextView txtv_tryapp, txtv_loading;

    String pf_username;
    String pf_password;
    LoginOneTimeTask hasLogin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //------------------------------------------------------------------------------------------
        //----------------------Kiểm tra User đã đăng nhập lần nào chưa-----------------------------
        //------------------------------------------------------------------------------------------
        SharedPreferences settings = getSharedPreferences(Login.ONETIMES, 0);
        pf_username = settings.getString("pf_username", null);
        pf_password = settings.getString("pf_password", null);
        //--------------------------------------------------------------------------------------
        //--------------------------Đã Login -> Trang Main--------------------------------------
        //--------------------------------------------------------------------------------------
        if (pf_username != null) {
            {
                hasLogin = new LoginOneTimeTask(pf_username, pf_password);
                hasLogin.execute((Void) null);

            }
        }
        //--------------------------------------------------------------------------------------
        //--------------------------Chưa Login -> Show Giao diện -------------------------------
        //--------------------------------------------------------------------------------------
        else {
            setContentView(R.layout.login_and_register);
            txtv_tryapp = (TextView) findViewById(R.id.txtv_tryapp);
            txtv_tryapp.setPaintFlags(txtv_tryapp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            //--------------------------------------------------------------------------------------
            //-------------------------------------Set Font ----------------------------------------
            //--------------------------------------------------------------------------------------
            Typeface tf = Typeface.createFromAsset(getAssets(), "VKORIN.TTF");
            TextView txtv_title = (TextView) findViewById(R.id.txtv_title);
            txtv_title.setTypeface(tf);
            //--------------------------------------------------------------------------------------
            //----------------------------------Set Animation Fly In--------------------------------
            //--------------------------------------------------------------------------------------

            new Thread(new Runnable() {
                public void run() {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearlayout_forwarding);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flyin);
                    linearLayout.setAnimation(animation);
                }
            }).start();


            //--------------------------------------------------------------------------------------
            //-----------------------------Set Event for Button Login-------------------------------
            //--------------------------------------------------------------------------------------
            Button btn_login = (Button) findViewById(R.id.btn_login);
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                            Intent intent = new Intent(Login_and_Register.this, Login.class);
                            startActivity(intent);
                }
            });
            //--------------------------------------------------------------------------------------
            //-----------------------------Set Event for Button Register----------------------------
            //--------------------------------------------------------------------------------------
            Button btn_register = (Button) findViewById(R.id.btn_register);
            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Login_and_Register.this, Register.class);
                    startActivity(intent);
                }
            });
            //--------------------------------------------------------------------------------------
            //----------------------------Set Event for Just Try the App----------------------------
            //--------------------------------------------------------------------------------------
            txtv_tryapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setContentView(R.layout.launcher_loading);

                    LoadingApp();
                    SharedPreferences setting = getSharedPreferences(Login_and_Register.TRYAPP, 0);
                    SharedPreferences.Editor editor = setting.edit();
                    editor.putBoolean("HasLoginByTryApp", true);
                    editor.commit();

                }
            });

        }


    }
    //----------------------------------------------------------------------------------------------
    //----------------------Hiển thị màn hình chờ nếu đã Login hoặc Try App-------------------------
    //----------------------------------------------------------------------------------------------

    public void LoadingApp() {
        setContentView(R.layout.launcher_loading);
        //--------------------------------------------------------------------------------------
        //------------------------------------Set Font------------------------------------------
        //--------------------------------------------------------------------------------------
        txtv_loading = (TextView) findViewById(R.id.txtv_loading);
        Typeface tf = Typeface.createFromAsset(getAssets(), "VKORIN.TTF");
        txtv_loading.setTypeface(tf);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Login_and_Register.this, Home.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    //------------------------------------------------------------------------------------------
    //----------------------Set sự kiện thoát App bằng 2 lần Back-----------------------------
    //------------------------------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        this.doubleBackToExitPressedOnce = false;
    }
    @Override
    public void onBackPressed() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 1000);
        if (doubleBackToExitPressedOnce) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
        else {
            Toast.makeText(this, "Press Again to Exit", Toast.LENGTH_SHORT).show();
            this.doubleBackToExitPressedOnce = true;
        }

    }
    //----------------------------------------------------------------------------------------------
    //----------------------Kết nối Server và Xử lý Đăng nhập Ẩn---------------------------------------
    //----------------------------------------------------------------------------------------------
    public class LoginOneTimeTask extends AsyncTask<Void, Void, JSONObject> {
        ProgressDialog dialog;
        private final String mUsername;
        private final String mPassword;
        JSONParser jsonParser = new JSONParser();
        private String LOGIN_URL = ofastURL.login;
        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";
        private static final String TAG_ERROR = "error";

        LoginOneTimeTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            setContentView(R.layout.launcher_loading);
            //--------------------------------------------------------------------------------------
            //------------------------------------Set Font------------------------------------------
            //--------------------------------------------------------------------------------------
            txtv_loading = (TextView) findViewById(R.id.txtv_loading);
            Typeface tf = Typeface.createFromAsset(getAssets(), "VKORIN.TTF");
            txtv_loading.setTypeface(tf);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("AppLoginForm[username]", mUsername);
                data.put("AppLoginForm[password]", mPassword);
                data.put("AppLoginForm[rememberMe]", "1");
                Log.d("request", "starting");
                //-----------------------------MakeHttpRequest-----------------------------------------
                //---------------Gửi yêu cầu lên server theo đường dẫn URL, phương thức POST, dữ liệu
                //---------------truyền vào là data------------------------------------------------
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", data);

                if (json != null) {
                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(final JSONObject result) {

//            Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
            if (result != null) {
                try {

                    String success = result.getString(TAG_SUCCESS);
                    String message = result.getString(TAG_MESSAGE);
                    String error = result.getString(TAG_ERROR);
                    Log.d("success", success + message + error);


                    if (!success.equals("true")) {
                        if (error.equals("E122")) {
                            Toast.makeText(getApplicationContext(), "Password đã thay đổi, vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login_and_Register.this, Login.class);
                            startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(Login_and_Register.this, Home.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Kết nối tới Sever thất bại!", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected void onCancelled() {
            hasLogin = null;
        }
    }
    /*Kiểm tra kết nối mạng*/
    public boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Login.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

//    public Runnable aaa = new Runnable()
//    {
//        @Override
//        public void run()
//        {
//            while (true)
//            {
//                // TODO add code to refresh in background
//                try
//                {
//
//                    Thread.sleep(1000);// sleeps 1 second
//
//                } catch (InterruptedException e){
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    };

}
