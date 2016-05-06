package com.hung.ofastapp;


import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hung.ofastapp.Adapter.Product_CustomListviewDetail;
import com.hung.ofastapp.CreateConnection.JSONParser;
import com.hung.ofastapp.CreateConnection.ofastURL;
import com.hung.ofastapp.Listener.SwipeDetector;
import com.hung.ofastapp.Objects.*;
import com.hung.ofastapp.Objects.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class Order extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ListView lv_dathang;
     TextView txtv_tongtien ;
    TextView title;
    TextView txtv_noproduct;
    Button btn_dathang;
    ArrayList<com.hung.ofastapp.Objects.Product> arrayList = new ArrayList<>();
    ArrayList<com.hung.ofastapp.Objects.Product> brrayList = new ArrayList<>();
    Product_CustomListviewDetail adapter;
    EditText edt_phone;
    EditText edt_email;
    EditText edt_notes;
    public int soluong = 0;
    View focusView;
    public float tongtien = 0;
    final Context context = this;
    OrderTask eOrderTask;
    SwipeDetector swipeDetector = new SwipeDetector();
    private AlertDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);
        Typeface tf1 = Typeface.createFromAsset(getAssets(), "VKORIN.TTF");
          /* Khai báo toolbar và set button Back */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

            /* Ánh xạ các thuộc tính */
        lv_dathang = (ListView) findViewById(R.id.lv_dathang);
        lv_dathang = (ListView) findViewById(R.id.lv_dathang);
        txtv_tongtien = (TextView) findViewById(R.id.txtv_tongtien);
        txtv_noproduct = (TextView) findViewById(R.id.txtv_noproduct);
        btn_dathang = (Button) findViewById(R.id.btn_dathang);
        title = (TextView) findViewById(R.id.txtv_info);
        title.setTypeface(tf1);
        progressDialog = new SpotsDialog(Order.this, R.style.Custom);


        /* =======================================================================================
          Nhận đối tượng được chọn từ Product.java kết hợp với số lượng để tạo thành 1 ListView Order
        ========================================================================================*/
        if (CheckContainShare() == true) {
            Log.d("Có tồn tại Share ", "ON");
            SharedPreferences aaa = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String json = aaa.getString("ListProduct", "");
            Type type = new TypeToken<ArrayList<Product>>() {}.getType();
            arrayList = gson.fromJson(json, type);

        }
        adapter = new Product_CustomListviewDetail(this, R.layout.product_custom_listview_detail, arrayList);
        lv_dathang.setAdapter(adapter);
        lv_dathang.invalidate();
        TinhTong(arrayList);
        adapter.notifyDataSetChanged();



        /* =======================================================================================
                                         SỰ KIỆN KHI NHẤN NÚT ĐẶT HÀNG
        ========================================================================================*/
            btn_dathang.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v) {
                    Log.d("NÚT ĐẶT HÀNG ", "ON");
                    if (arrayList.isEmpty()) {
                        Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        LayoutInflater li = LayoutInflater.from(context);
                        View promptsView = li.inflate(R.layout.oder_dialog_info, null);
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                context);
//                alertDialogBuilder.setTitle("Nhập thông tin khách hàng");
                        alertDialogBuilder.setView(promptsView);
                        edt_email = (EditText) promptsView
                                .findViewById(R.id.edt_email);
                        edt_phone = (EditText) promptsView
                                .findViewById(R.id.edt_phone);

                        edt_notes = (EditText) promptsView
                                .findViewById(R.id.edt_notes);

                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("OK", null)
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Log.d("Dialog ", "Cancel");
                                                dialog.cancel();
                                            }
                                        });

                        // create alert dialog
                        final AlertDialog alertDialog = alertDialogBuilder.create();

                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                            @Override
                            public void onShow(final DialogInterface dialog) {
//Khi sử dụng .setPositiveButton("OK",null) thì phải sử dụng hàm ở dưới để không bị ẩn dialog khi ấn vào nút OK,
// vì trong quá trình ấn OK, chúng ta phải kiểm tra nhập có hợp lệ hay không, nếu không hợp lệ thì focus vào phần
// nhập không hợp lệ đó đồng thời không được tắt dialog. sử dụng dialog.dimiss để tắt dilag lúc cần thiết.
//
                                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                button.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        Log.d("Dialog ", "OK");
                                        String email = edt_email.getText().toString();
                                        String phone = edt_phone.getText().toString();
                                        String notes = edt_notes.getText().toString();

                                        boolean checkInput = CheckInput(email, phone);
                                        if (checkInput == true) {
                                            eOrderTask = new OrderTask(email, phone, notes);
                                            eOrderTask.execute((Void) null);
                                            dialog.dismiss();
                                        } else {
                                            focusView.requestFocus();
                                        }

                                    }
                                });
                            }
                        });
                        // show it
                        alertDialog.show();
                    }
                }
            });
        }


    /* =======================================================================================
        Kiểm tra điều kiện nhập liệu trước khi gửi lên Server
        ========================================================================================*/
    private boolean CheckInput(String email, String phone) {
        // Reset errors.
        edt_email.setError(null);
        edt_phone.setError(null);
        boolean cancel = false;
        focusView = null;
        // Kiểm tra trống Pass
        if (TextUtils.isEmpty(email) ) {
            Log.d("Lỗi Email", "Trống");
            edt_email.setError(getString(R.string.error_field_required));
            focusView =  edt_email;
            cancel = true;
        }
        // Kiểm tra trống NumberPhone
        if (TextUtils.isEmpty(phone) ) {
            Log.d("Lỗi SĐT", "Trống");
            edt_phone.setError(getString(R.string.error_field_required));
            focusView = edt_phone;
            cancel = true;
        }
        //Kiểm tra độ dài của SĐT
        if (CheckLenght(edt_phone.getText().toString()) == true) {
            Log.d("Lỗi SĐT", "FALSE");
            edt_phone.setError("SĐT không đúng, yêu cầu nhập lại!");
            focusView = edt_phone;
            cancel = true;
        }
        //Kiểm tra đúng sai của Email, tồn tại @ vào .
        if(isValidEmail(email) == false)
        {
            Log.d("Lỗi Email", "FALSE");
            edt_email.setError("Vui lòng nhập Email của bạn!");
            focusView =edt_email;
            cancel = true;
        }
        //Nếu có lỗi thì trả về false, còn không thì trả về true
        if (cancel) {
            Log.d("Lỗi", "FALSE");
            return false;
        } else {
            Log.d("KHông có lỗi", "TRUE");
            return true;
        }
    }


    /* =======================================================================================
                            Override phần tạo kết nối với Server
           ========================================================================================*/
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


    /* =======================================================================================
        Xử lý POST dữ liệu lên Server và kiểm tra kết quả trả về!
        ========================================================================================*/
    public class OrderTask extends AsyncTask<Void, Void, JSONObject> {
        private final String mEmail;
        private final String mPhone;
        private final String mNotes;

        JSONParser jsonParser = new JSONParser();
        private String ORDER_URL = ofastURL.order;

        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";
        private static final String TAG_ERROR = "error";

        /*Hàm Khởi tạo của Xử lý đăng kí*/
        OrderTask(String email, String phone, String notes) {
            mEmail = email;
            mPhone = phone;
            mNotes = notes;
        }

        /*Hàm trước khi Xử lý đăng kí*/
        protected void onPreExecute() {
            progressDialog.show();
        }
        /*Hàm kết nối tới Server \*/
        //Order[email] , Order[phone], Order[notes], Oder[product]
        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {

            /* ----------------------------------------------------------------------------------
               ------------------------ Add by Khang-va : gui json array ------------------------
               ------ Phan products thay bang 1 mang json array voi 2 truong id va quantity -----
               ----------------------------------------------------------------------------------
            */

                // tao 1 jsonObject the hien 1 product
                // tao jsonArray de luu cac product dang jsonObject
                JSONArray jaProduct = new JSONArray();
                //Put arraylist lên server
                for(int i=0; i<arrayList.size(); i++)
                {
                    JSONObject jProducts = new JSONObject();
                    jProducts.put("id",arrayList.get(i).getId_product());
                    //quantity là số lượng sản phẩm được post lên
                    jProducts.put("quantity",arrayList.get(i).getNum_order());
                    jProducts.put("brand_id","1");
                    jaProduct.put(jProducts);
                }
                // tao jsonObject voi cac thong tin co ban de up len server.
                // phan product la 1 mang JsonArray product

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", mEmail);
                jsonParam.put("phone", mPhone);
                jsonParam.put("notes", mNotes);
                jsonParam.put("products", jaProduct);


                // code cu cua hung.
//                HashMap<String, String> data = new HashMap<String, String>();
//                data.put("Order[email]", mEmail);
//                data.put("Order[phone]", mPhone);
//                data.put("Order[notes]", mNotes);

                Log.d("request", "starting");
                /*-----------------------------MakeHttpRequest-----------------------------------------
                ---------------Gửi yêu cầu lên server theo đường dẫn URL, phương thức POST, dữ liệu
                ---------------truyền vào là data------------------------------------------------*/
                JSONObject json = jsonParser.makeJsonHttpRequest(
                        ORDER_URL, jsonParam);

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

            eOrderTask = null;
            if (result != null) {
                try {
                    Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_SHORT).show();
                    String success = result.getString(TAG_SUCCESS);
                    String message = result.getString(TAG_MESSAGE);
                    String error = result.getString(TAG_ERROR);
                    Log.d("success", success + message + error);
                    if (!success.equals("true")) {
                        Log.d("Gửi hàng thành công:  ","ON");
                        Toast.makeText(getApplicationContext(),"Kết nối tới Server thất bại, vui lòng thử lại sau giây lát!",Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(),mEmail,Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(),mPhone,Toast.LENGTH_SHORT).show();
//
                    } else {
                       if(CheckContainShare() == true)
                       {
                        ArrayList<Product> nullarraylist = new ArrayList<Product>();

                           SharedPreferences clearlist = PreferenceManager
                                   .getDefaultSharedPreferences(getApplicationContext());
                           SharedPreferences.Editor prefsEditor = clearlist.edit();
                           Gson gson = new Gson();
                           String json = gson.toJson(nullarraylist);
                           prefsEditor.putString("ListProduct", json);
                           prefsEditor.commit();
                       }
                        progressDialog.dismiss();
                        Intent intent = new Intent(Order.this, Home.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Đã gửi đơn hàng thành công. Chúng tôi sẽ liên hệ bạn trong vài giây tới!", Toast.LENGTH_SHORT).show();
//
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
            eOrderTask = null;

        }

    }


    /* =======================================================================================
                                    Sự kiện khi ấn Back Button
       ========================================================================================*/
    public boolean onOptionsItemSelected(MenuItem item){
        super .onBackPressed();
        Log.d("OptionsItemSelected:  ","ON");
        SharedPreferences onOptionsItemSelected = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor prefsEditor = onOptionsItemSelected.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        prefsEditor.putString("ListProduct", json);
        prefsEditor.commit();
        return true;
    }

    /* =======================================================================================
                                 Sự kiện khi ấn Back từ Device
     ========================================================================================*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("onBackPressed:  ","ON");
        SharedPreferences onBackPressed = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor prefsEditor = onBackPressed.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        prefsEditor.putString("ListProduct", json);
        prefsEditor.commit();
    }
    /* =======================================================================================
                                          onResume
        ========================================================================================*/
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("SIZE ARRAYLIST",String.valueOf(arrayList.size()));
        Log.d("Onresume","ON");

    }


    /* =======================================================================================
                                         Check Lenght SĐT
    ========================================================================================*/
    public static boolean CheckLenght(String string)
    {
        return (string.length()>0&&string.length()<10);
    }

     /* =======================================================================================
                                    Check Email
    ========================================================================================*/
        public final static boolean isValidEmail(CharSequence target) {
            return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    /* =======================================================================================
                        Kiểm tra có tồn tại SharePreference hay không
    ========================================================================================*/
    private boolean CheckContainShare() {
        Log.d("CheckContainShare:  ","ON");
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("ListProduct", "");
        if(json.isEmpty() == false)
        {
            return true;
        }
        else return false;
    }
    /* =======================================================================================
                      Hàm tính tổng tiền cho tất cả các sản phẩm được chọn
    ========================================================================================*/
    public void TinhTong(ArrayList<Product> aaa)
    {
        for(int i = 0; i<arrayList.size(); i++)
        {
            tongtien = tongtien + aaa.get(i).getNum_order() * Float.parseFloat(aaa.get(i).getPrice_product());

        }
        txtv_tongtien.setText(String.valueOf(tongtien) + "00VNĐ");
        Log.d("Tính Tổng:  ",String.valueOf(tongtien));
        tongtien = 0;
        if(arrayList.size() == 0)
        {
            txtv_tongtien.setText("Tổng tiền");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("ON STOP","KIDDING MEEEEE");
//        SharedPreferences onStop = PreferenceManager
//                .getDefaultSharedPreferences(getAoppplicationContext());
//        SharedPreferences.Editor prefsEditor = onStop.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(arrayList);
//        prefsEditor.putString("ListProduct", json);
//        prefsEditor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ON DESTROY", "FUCK FUCK");
    }
}