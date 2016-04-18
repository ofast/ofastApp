package com.hung.ofastapp;


import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.hung.ofastapp.Adapter.Product_CustomListviewDetail;
import com.hung.ofastapp.CreateConnection.JSONParser;
import com.hung.ofastapp.CreateConnection.ofastURL;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Order extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    ListView lv_dathang;
    TextView txtv_tongtien;
    TextView title;
    Button btn_dathang;
    ArrayList<com.hung.ofastapp.Objects.Product> arrayList = new ArrayList<>();
    Product_CustomListviewDetail adapter;
    EditText edt_phone;
    EditText edt_email;
    EditText edt_notes;
    View focusView;
    float tongtien = 0;
    final Context context = this;
    OrderTask eOrderTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dathang);
        Typeface  tf1 = Typeface.createFromAsset(getAssets(), "VKORIN.TTF");
          /* Khai báo toolbar và set button Back */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

            /* Ánh xạ các thuộc tính */
        lv_dathang = (ListView) findViewById(R.id.lv_dathang);
        lv_dathang = (ListView) findViewById(R.id.lv_dathang);
        txtv_tongtien = (TextView) findViewById(R.id.txtv_tongtien);
        btn_dathang = (Button) findViewById(R.id.btn_dathang);
        title = (TextView) findViewById(R.id.txtv_info);
        title.setTypeface(tf1);


        /* =======================================================================================
          Nhận đối tượng được chọn từ Product.java kết hợp với số lượng để tạo thành 1 ListView Order
        ========================================================================================*/
        Bundle bd = getIntent().getExtras();
        arrayList = (ArrayList<com.hung.ofastapp.Objects.Product>) bd.getSerializable("LISTORDER");
        adapter = new Product_CustomListviewDetail(this, R.layout.product_custom_listview_detail, arrayList);
        lv_dathang.setAdapter(adapter);
        for (com.hung.ofastapp.Objects.Product prod : arrayList
             ) {
            if(prod.isPicked()){
                tongtien = tongtien +  prod.getNum_order() * Float.parseFloat(prod.getPrice_product());
            }
        }


        /* =======================================================================================
                                         Set tổng tiền
        ========================================================================================*/
        txtv_tongtien.setText(String.valueOf(tongtien)+ "00VNĐ");


        /* =======================================================================================
                                         SỰ KIỆN KHI NHẤN NÚT ĐẶT HÀNG
        ========================================================================================*/
        btn_dathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                            .setPositiveButton("OK",null)
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    final AlertDialog aaa = alertDialogBuilder.create();

                aaa.setOnShowListener(new DialogInterface.OnShowListener() {

                        @Override
                        public void onShow(final DialogInterface dialog) {
//Khi sử dụng .setPositiveButton("OK",null) thì phải sử dụng hàm ở dưới để không bị ẩn dialog khi ấn vào nút OK,
// vì trong quá trình ấn OK, chúng ta phải kiểm tra nhập có hợp lệ hay không, nếu không hợp lệ thì focus vào phần
// nhập không hợp lệ đó đồng thời không được tắt dialog. sử dụng dialog.dimiss để tắt dilag lúc cần thiết.
//
                            Button b = aaa.getButton(AlertDialog.BUTTON_POSITIVE);
                            b.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    String email =  edt_email.getText().toString();
                                    String phone = edt_phone.getText().toString();
                                    String notes = edt_notes.getText().toString();

                                   boolean he = CheckInput(email, phone);
                                    if(he ==true)
                                    {
                                        eOrderTask = new OrderTask(email,phone,notes);
                                        eOrderTask.execute((Void) null);
                                        dialog.dismiss();
                                    }
                                    else {
                                        focusView.requestFocus();
                                    }

                                }
                            });
                        }
                    });
                    // show it
                    aaa.show();
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
            edt_email.setError(getString(R.string.error_field_required));
            focusView =  edt_email;
            cancel = true;
        }
        // Kiểm tra trống NumberPhone
        if (TextUtils.isEmpty(phone) ) {
            edt_phone.setError(getString(R.string.error_field_required));
            focusView = edt_phone;
            cancel = true;
        }
        if (CheckLenght(edt_phone.getText().toString()) == true) {
            edt_phone.setError("SĐT không đúng, yêu cầu nhập lại!");
            focusView = edt_phone;
            cancel = true;
        }
        if(isValidEmail(email) == false)
        {
            edt_email.setError("Vui lòng nhập Email của bạn!");
            focusView =edt_email;
            cancel = true;
        }

        if (cancel) {

            return false;
        } else {
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

        }
        /*Hàm kết nối tới Server \*/
        //Order[email] , Order[phone], Order[notes], Oder[product]
        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("Order[email]", mEmail);
                data.put("Order[phone]", mPhone);
                data.put("Order[notes]", mNotes);
                Log.d("request", "starting");
                /*-----------------------------MakeHttpRequest-----------------------------------------
                ---------------Gửi yêu cầu lên server theo đường dẫn URL, phương thức POST, dữ liệu
                ---------------truyền vào là data------------------------------------------------*/
                JSONObject json = jsonParser.makeHttpRequest(
                        ORDER_URL, "POST", data);

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


                        Toast.makeText(getApplicationContext(),"Kết nối tới Server thất bại, vui lòng thử lại sau giây lát!",Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(),mEmail,Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(),mPhone,Toast.LENGTH_SHORT).show();
//
                    } else {
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
        return true;
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
}