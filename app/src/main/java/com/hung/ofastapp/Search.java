package com.hung.ofastapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hung.ofastapp.Adapter.Search_CustomListView;
import com.hung.ofastapp.CreateConnection.JSONParser;
import com.hung.ofastapp.CreateConnection.ofastURL;
import com.hung.ofastapp.Objects.Product;
import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Search extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    //Giao diện
    TextView txtv_kq;
    TextView txtv_noproduct;
    LinearLayout lnlo_kq;
    //ListView
    ListView lv_search;
    Context context = this;
    ArrayList<com.hung.ofastapp.Objects.Product> arrayList;
    Search_CustomListView adapter;
   //Task
    SearchTask eSearchTask = null;
    Product product;
    //ArrayList Hỗ trợ
    ArrayList<com.hung.ofastapp.Objects.Product> favoriteList = new ArrayList<Product>();
    ArrayList<com.hung.ofastapp.Objects.Product> checkcontainList = new ArrayList<Product>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        /*-----------------------------------------------------------------------------------------|
        |-----------------------------Khai báo các thuộc tính--------------------------------------|
        |------------------------------------------------------------------------------------------*/
        lv_search = (ListView) findViewById(R.id.lv_search);
        lnlo_kq = (LinearLayout) findViewById(R.id.lnlo_kq);
        txtv_kq = (TextView) findViewById(R.id.txtv_kq);
        txtv_noproduct = (TextView) findViewById(R.id.txtv_noproduct);
                                 /*Toolbar*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        /*-----------------------------------------------------------------------------------------|
        |------------Kiểm tra nếu đã tồn tại Share thì lưu vào favoriteList------------------------|
        |------------------------------------------------------------------------------------------*/
        if(CheckContainShare()==true)
        {
            SharedPreferences aaa = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String json = aaa.getString("FavoriteList", null);
            Type type = new TypeToken<ArrayList<Product>>() {}.getType();
            //Lưu vào favoriteList
            favoriteList = gson.fromJson(json, type);
            Log.d("AAAAAA", String.valueOf(favoriteList.size()));
        }


    }
    /*---------------------------------------------------------------------------------------------|
    |---------Hàm set nội dung có trên thanh Toobar, được lấy từ menu_searchview-------------------|
    |---------------------------------------------------------------------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_searchview, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.search));
        searchItem.expandActionView();
        //*** setOnQueryTextFocusChangeListener ***
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("onFocusChange", "ON");
            }
        });
        /*-----------------------------------------------------------------------------------------|
        |--------------------------------Sự kiện cho bàn phím nhập---------------------------------|
        |------------------------------------------------------------------------------------------*/
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //Khi gõ
            @Override
            public boolean onQueryTextChange(String searchQuery) {
                Log.d("onQueryTextChange", "ON");
                return true;
            }
            //Khi ấn Submit Search
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("onQueryTextSubmit", "ON");
                Log.d("String INPUT", query);
                String searchOutput = getResources().getString(R.string.result) + " \"" + "<b>" + query + "</b>" + "\":";
                lnlo_kq.setVisibility(View.VISIBLE);
                txtv_kq.setText(Html.fromHtml(searchOutput));
                arrayList = new ArrayList<Product>();
                eSearchTask = new SearchTask(query, "");
                eSearchTask.execute();
                return false;
            }
        });
        /*-----------------------------------------------------------------------------------------|
        |--------------------------------Sự kiện cho Searchview------------------------------------|
        |-----------------------------------------------------------------------------------------*/
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            //Sự kiện khi nhấn back trong icon Search
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                onBackPressed();
                return false;  // Return true to collapse action view
            }
            // Sự kiện khi ấn vào icon search
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;  // Return true to expand action view
            }
        });
        return true;
    }
    /*----------------------------------------------------------------------------------------------|
    |--------Gửi chuỗi được nhập từ SearchView lên Server, sau đó nhận kết quả trả về---------------|
    |--------------------là mỗi ArrayList<Product>--------------------------------------------------*/
    public class SearchTask extends AsyncTask<Void, Void, JSONArray> {
        private final String mTitle;
        private final String mDescription;

        JSONParser jsonParser = new JSONParser();
        private String SEARCH_URL = ofastURL.search;
        private static final String TAG_ID = "id";
        private static final String TAG_TITLE = "title";
        private static final String TAG_PRICE = "price";
        private static final String TAG_IMG = "images";
        private static final String TAG_CATEGORY_ID = "category_id";
        /*Hàm Khởi tạo của Search*/
        SearchTask(String mTitle, String mDescription) {
            this.mTitle = mTitle;
            this.mDescription = mDescription;
        }
        /*Hàm trước khi Xử lý Search*/
        protected void onPreExecute() {}
        /*Hàm kết nối tới Server */
        @Override
        protected JSONArray doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("ProductSearch[title]", mTitle);
                data.put("ProductSearch[description]", mDescription);
                Log.d("request", "starting");
                //-----------------------------MakeHttpRequest-----------------------------------------
                //---------------Gửi yêu cầu lên server theo đường dẫn URL, phương thức POST, dữ liệu
                //---------------truyền vào là data------------------------------------------------
                JSONArray json = jsonParser.makeHttpRequests(
                        SEARCH_URL, "POST", data);
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
        protected void onPostExecute(JSONArray result) {
            eSearchTask = null;
            if (result != null) {
                try {
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject item = result.getJSONObject(i);
                        Log.d("Object", item.toString());
                        String id = item.getString(TAG_ID);
                        String category_id = item.getString(TAG_CATEGORY_ID);
                        String title = item.getString(TAG_TITLE);
                        String price = item.getString(TAG_PRICE);
                        String image = item.getString(TAG_IMG);
                        product = new Product(
                                Integer.parseInt(id),
                                title,
                                price,
                                ofastURL.frontend_Web_image + image,
                                Integer.parseInt(category_id)
                        );
                        arrayList.add(product);
                    }
                    if (arrayList.size() != 0) {
                        ShowList(true);
                        adapter = new Search_CustomListView(getApplicationContext(), R.layout.search_item, arrayList, Search.this);
                        lv_search.setAdapter(adapter);
                        CheckContainProduct();
                        lv_search.invalidate();
                        adapter.notifyDataSetChanged();
                    } else {
                        ShowList(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Kết nối tới Server thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /*----------------------------------------------------------------------------------------------|
    |--------Nếu listview không có phần tử thì ẩn Listview đồng thời show TextView có sẳn ra--------|
    |----------------------------------------------------------------------------------------------*/
    public void ShowList(boolean a) {
        if (a == true) {
            lv_search.setVisibility(View.VISIBLE);
            txtv_noproduct.setVisibility(View.GONE);
        } else {
            lv_search.setVisibility(View.GONE);
            txtv_noproduct.setVisibility(View.VISIBLE);
        }
    }
    /*----------------------------------------------------------------------------------------------|
    |-------------------------------------On Resume-------------------------------------------------|
    |----------------------------------------------------------------------------------------------*/
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("OnResume",":::TRUE");
        if(CheckContainShare()==true)
        {
            SharedPreferences aaa = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String json = aaa.getString("FavoriteList", null);
            Type type = new TypeToken<ArrayList<Product>>() {
            }.getType();
            //Lưu vào favoriteList
            favoriteList = gson.fromJson(json, type);
        }
    }
    /*----------------------------------------------------------------------------------------------|
    |-------------------------------------OnBackPress-----------------------------------------------|
    |----------------------------------------------------------------------------------------------*/
    @Override
    public void onBackPressed() {
        SharedPreferences onBackPressed = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor prefsEditor = onBackPressed.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favoriteList);
        prefsEditor.putString("FavoriteList", json);
        prefsEditor.commit();
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), Home.class);
        hideSoftKeyboard(Search.this);
        startActivity(intent);
    }
    /*---------------------------------------------------------------------------------------------|
    |----------------------------Kiểm tra sự tồn tại SharePreference-------------------------------|
    |----------------------Nếu có trả về True, còn không trả về False-----------------------------*/
    private boolean CheckContainShare() {
        SharedPreferences aaa = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = aaa.getString("FavoriteList", null);
        Type type = new TypeToken<ArrayList<Product>>() {}.getType();
        //Lưu vào brraylist
        checkcontainList= gson.fromJson(json, type);
        if(checkcontainList == null)
        {
            return false;
        }
        else return true;
    }
    /*---------------------------------------------------------------------------------------------|
    |---------------------Kiểm tra sự tồn tại của 1 Product trong một list-------------------------|
    |----------------------Nếu có thì setChecked cho phần tử đó là True---------------------------*/
    public void CheckContainProduct()
    {
        for (int i = 0; i<favoriteList.size(); i++)
        {
            for(int h = 0; h<arrayList.size(); h = h+1)
            {
                if(favoriteList.get(i).getId_product() == arrayList.get(h).getId_product())
                {
                    Log.d("CheckContainProduct","TRUE");
                    arrayList.get(h).setChecked(true);
                }
            }
        }
    }
    /*---------------------------------------------------------------------------------------------|
    |--------Kiểm tra sự tồn tại của 1 Product trong một list, nếu có rồi thì khỏi ADD-------------|
    |---------------------------------------------------------------------------------------------*/
    public boolean CheckAndAdd(ArrayList<com.hung.ofastapp.Objects.Product> aaa, com.hung.ofastapp.Objects.Product bbb)
    {
        if(CheckContainShare()==true)
        {
            for (int i = 0; i<aaa.size(); i++)
            {
                if(aaa.get(i).getId_product() == bbb.getId_product())
                {
                    return true;
                }
            }
        }
        return false;
    }
    /*---------------------------------------------------------------------------------------------|
    |--------Kiểm tra sự tồn tại của 1 Product trong một list, nếu có rồi thì xóa Product----------|
    |---------------------------------------------------------------------------------------------*/
    public void CheckAndRemove(com.hung.ofastapp.Objects.Product bbb)
    {
        Log.d("CheckAndRemove","ON");
        if(CheckContainShare()==true)
        {
            for (int i = 0; i<favoriteList.size(); i++)
            {
                if(favoriteList.get(i).getId_product() == bbb.getId_product())
                {
                    favoriteList.remove(i);
                }
            }
        }
    }
    /*---------------------------------------------------------------------------------------------|
    |-------------------------------Hàm ẩn Keyboard cho 1 Activity---------------------------------|
    |---------------------------------------------------------------------------------------------*/
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    /*---------------------------------------------------------------------------------------------|
    |-------------Hàm này sử dụng cho Adapter, Kiểm tra nếu Product đã Checked thì ----------------|
    |----------------Khi nhấn vào sẽ Unchecked + Remove Product đó và ngược lại--------------------|
    |--------------------------Sau đó lưu List mới cập nhập và Share------------------------------*/
    public void RemoveAndAddFavorite(Product product)
    {
        if (product.isChecked()) {
            product.setChecked(false);
            CheckAndRemove(product);

        } else {
            product.setChecked(true);
            if(CheckAndAdd(favoriteList,product) == true)
            {

            }else {
                favoriteList.add(product);
            }
        }
        SharedPreferences CheckAndAddFavorite = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor prefsEditor = CheckAndAddFavorite.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favoriteList);
        prefsEditor.putString("FavoriteList", json);
        prefsEditor.commit();
        Log.d("SIZE FAVORITE",String.valueOf(favoriteList.size()));
    }
    //HÀM KHÔNG BAO GIỜ DÙNG TỚI
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
    public void onLoaderReset(Loader<Cursor> loader) {}
    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };
        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
}
