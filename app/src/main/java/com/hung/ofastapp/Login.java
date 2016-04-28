package com.hung.ofastapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class Login extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{


    private UserLoginTask eLoginTask = null;
    private View mProgressView;
    private View mLoginFormView;
    //Typeface kiểu font
    Typeface tf1, tf2;
    TextView txtv_title;
    EditText edt_username,edt_password;
    Button btn_login, btn_register,btn_login_box;
    CheckBox cb_show_and_hide_password;
    ImageButton imgbtn_back;
    TextView txtv_forgot_password;
    LinearLayout layout_login;
    ProgressBar pb_login;
    LinearLayout linear_loginbox ;


    //Khai báo biến ONTETIMES để lưu SharePreference
    public static final String ONETIMES = "LoginOneTimes";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //------------------------------------------------------------------------------------------
        //---------------------------Animation for Box Login-------------------------------------
        //------------------------------------------------------------------------------------------
        linear_loginbox = (LinearLayout) findViewById(R.id.linear_loginbox);
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        linear_loginbox.startAnimation(rotation);
        //------------------------------------------------------------------------------------------
        //---------------------------Ánh xạ các thuộc tính -----------------------------------------
        //------------------------------------------------------------------------------------------
        txtv_title = (TextView)findViewById(R.id.txtv_title);
        edt_username = (EditText)findViewById(R.id.edt_username);
        edt_password = (EditText)findViewById(R.id.edt_password);
        btn_login_box = (Button)findViewById(R.id.btn_login_box);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_register = (Button)findViewById(R.id.btn_register);
        cb_show_and_hide_password = (CheckBox)findViewById(R.id.cb_show_and_hide_password);
        imgbtn_back = (ImageButton)findViewById(R.id.imgbtn_back);
        txtv_forgot_password = (TextView)findViewById(R.id.txtv_forgot_password);
        pb_login = (ProgressBar)findViewById(R.id.pb_login);
        layout_login = (LinearLayout)findViewById(R.id.layout_login);

        //------------------------------------------------------------------------------------------
        //----------------------------Bỏ gạch chân cho EditText-------------------------------------
        //------------------------------------------------------------------------------------------
        edt_username.setBackgroundColor(Color.TRANSPARENT);
        edt_password.setBackgroundColor(Color.TRANSPARENT);
        //------------------------------------------------------------------------------------------
        //---------------------Kiểm tra nếu Đăng ký thành công thì set giá trị ---------------------
        //--------------------------------cho Username và Password----------------------------------
        //------------------------------------------------------------------------------------------
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            edt_username.setText(bundle.getString("hasSignUp_username"));
            edt_password.setText(bundle.getString("hasSignUp_password"));

        }
        //------------------------------------------------------------------------------------------
        //--------------------Không hiện Keyboard khi khởi động ------------------------------------
        //------------------------------------------------------------------------------------------
    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //------------------------------------------------------------------------------------------
        //----------------------------------Set Font------------------------------------------------
        //------------------------------------------------------------------------------------------
        tf1 = Typeface.createFromAsset(getAssets(),"VNF-Futura Regular.ttf");
        tf2 = Typeface.createFromAsset(getAssets(),"VKORIN.TTF");
        txtv_title.setTypeface(tf1);
        edt_username.setTypeface(tf1);
        edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edt_password.setTypeface(tf1);
        btn_login_box.setTypeface(tf1);
        cb_show_and_hide_password.setTypeface(tf2);
        txtv_forgot_password.setTypeface(tf2);

        //------------------------------------------------------------------------------------------
        //----------------------------Set Event Show - Hide Password--------------------------------
        //------------------------------------------------------------------------------------------
        cb_show_and_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int type = isChecked ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_TEXT_VARIATION_PASSWORD;
                edt_password.setInputType(InputType.TYPE_CLASS_TEXT | type);
                edt_password.setTypeface(tf1);
                edt_password.setSelection(edt_password.length());
            }
        });


        //------------------------------------------------------------------------------------------
        //----------------------------Set Event for ImageButton-------------------------------------
        //------------------------------------------------------------------------------------------
        imgbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, Login_and_Register.class);
                startActivity(intent);
            }
        });
        //------------------------------------------------------------------------------------------
        //--------------------------Set Event for Button Đăng Nhập----------------------------------
        //------------------------------------------------------------------------------------------
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        //------------------------------------------------------------------------------------------
        //----------------------------Set Event for Button Register---------------------------------
        //------------------------------------------------------------------------------------------
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
        //------------------------------------------------------------------------------------------
        //----------------------------Tạm thời chưa biết--------------------------------------------
        //------------------------------------------------------------------------------------------
        edt_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == R.id.edt_password || actionId == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        //------------------------------------------------------------------------------------------
        //--------------------------Set Event for Button Login in Box-------------------------------
        //------------------------------------------------------------------------------------------
        btn_login_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Nếu kết nối mạng đã bật, chạy hàm attempLogin, không thì show đoạn Toast yêu cầu mở Internet
                if (isConnected() == true) {
                        hideSoftKeyboard(Login.this);
                        attemptLogin();
                    getCurrentFocus().clearFocus();
                } else {
                    Toast.makeText(getApplication(), "Vui Lòng Kiểm Tra Kết Nối Mạng", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }






    //----------------------------------------------------------------------------------------------
    //------------------------Kiểm tra dữ liệu đầu vào từ App đã hợp lệ chưa------------------------
    //----------------------------------------------------------------------------------------------
    private void attemptLogin() {
        if (eLoginTask != null) {
            return;
        }
        edt_username.setError(null);
        edt_password.setError(null);

        // Store values at the time of the login attempt.
        String username = edt_username.getText().toString();
        String password = edt_password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        /*Kiểm tra Password có bị trống không*/
        if (TextUtils.isEmpty(password) ) {
            edt_password.setError(getString(R.string.error_field_required));
            focusView = edt_password;
            cancel = true;
        }

         /*Kiểm tra Username có bị trống không*/
        if (TextUtils.isEmpty(username)) {
            edt_username.setError(getString(R.string.error_field_required));
            focusView = edt_username;
            cancel = true;
        }
         /*Kiểm tra Độ dài của Username*/
        if(CheckLenght(username)== true)
        {
            edt_username.setError(getString(R.string.error_lenght));
            focusView = edt_username;
            cancel = true;
        }
        /*Kiểm tra Độ dài của Password*/
        if(CheckLenght(password)== true)
        {
            edt_password.setError(getString(R.string.error_lenght));
            focusView = edt_password;
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
            eLoginTask = new UserLoginTask(username, password);
            eLoginTask.execute((Void) null);
        }
    }
    //----------------------------------------------------------------------------------------------
    //----------------------------Hàm kiểm tra Độ dài của dữ liệu đầu vào---------------------------
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
    //------------------------Hàm chạy Screen Laucher khi đợi kết quả từ Server------------------------
    //----------------------------------------------------------------------------------------------
//    @TargetApi(Build.VERSION_CODES.)
    private void showProgress(final boolean show) {

            if (show == true) {

                setupUI(findViewById(R.id.layout_login));
                pb_login.setVisibility(View.VISIBLE);
                   layout_login.setBackgroundResource(R.drawable.ofast_background);


            }
            else
            {


                pb_login.setVisibility(View.GONE);
                layout_login.setBackgroundResource(R.drawable.ofast_background_login_register);
            }


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
    //----------------------Kết nối Server và Xử lý Đăng nhập---------------------------------------
    //----------------------------------------------------------------------------------------------
    public class UserLoginTask extends AsyncTask<Void, Void, JSONObject> {
        private final String mUsername;
        private final String mPassword;
        JSONParser jsonParser = new JSONParser();

        private  String LOGIN_URL = ofastURL.login;

        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";
        private static final String TAG_ERROR = "error";
        /*Hàm Khởi tạo của Xử lý đăng nhập*/
        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }
        /*Hàm trước khi Xử lý đăng nhập*/
        protected void onPreExecute() {
            showProgress(true);
        }
        /*Hàm kết nối tới Server */
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
        /*Nhận kết quả từ Server, bắt đầu xử lý dữ liệu trả về*/
        @Override
        protected void onPostExecute(final JSONObject result) {

            eLoginTask = null;

                if (result != null) {
//
                    try {
                        String success = result.getString(TAG_SUCCESS);
                        String message = result.getString(TAG_MESSAGE);
                        String error = result.getString(TAG_ERROR);
                        Log.d("success", success + message + error);


                        if (!success.equals("true")) {
                            showProgress(false);
                            if (error.equals("E121")) {

                                edt_username.setError(message);
                                edt_username.requestFocus();
                            }

                            if (error.equals("E122")) {

                                edt_password.setError(message);
                                edt_password.requestFocus();
                            }
                        } else {
                            /*Lưu thông tin ID và Password khi đăng nhập thành công vào SharePreferencd trên máy,
                            *  Lần sau khi mở app, sẽ là quá trình đăng nhập ẩn với username và password đã lưu ở đây*/
                            SharedPreferences setting = getSharedPreferences(Login.ONETIMES,0);
                            SharedPreferences.Editor editor = setting.edit();
                            editor.putString("pf_username",edt_username.getText().toString());
                            editor.putString("pf_password", edt_password.getText().toString());
                            editor.apply();
                            Intent intent = new Intent(Login.this, Home.class);
                            startActivity(intent);
                            finish();
                            Log.d("success", "true");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Kết nối tới Server thất bại", Toast.LENGTH_SHORT).show();
                    showProgress(false);
                }


        }
        @Override
        protected void onCancelled() {
            eLoginTask = null;

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

    //----------------------------------------------------------------------------------------------
    //------------------------------Hàm ẩn Keyboard cho 1 Activity----------------------------------
    //----------------------------------------------------------------------------------------------
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    //----------------------------------------------------------------------------------------------
    //------------------------------Hàm ẩn Keyboard khi chạm vào màn hình --------------------------
    //--------------------------------hoặc 1 layout gì đó-------------------------------------------
    //----------------------------------------------------------------------------------------------
    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(Login.this);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }
}
