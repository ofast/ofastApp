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
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
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

public class Search extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    SearchTask eSearchTask = null;
    ArrayList<com.hung.ofastapp.Objects.Product>  arrayList;
    ArrayList<com.hung.ofastapp.Objects.Product>  FavoriteList = new ArrayList<Product>();
    ArrayList<com.hung.ofastapp.Objects.Product>  tempFavoriteList = new ArrayList<Product>();
    Product product;
    Search_CustomListView adapter;
    ListView lv_search;
    LinearLayout lnlo_kq;
    TextView txtv_kq;
    TextView txtv_noproduct;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        lv_search = (ListView) findViewById(R.id.lv_search);
        lnlo_kq = (LinearLayout) findViewById(R.id.lnlo_kq);
        txtv_kq = (TextView) findViewById(R.id.txtv_kq);
        txtv_noproduct = (TextView) findViewById(R.id.txtv_noproduct);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(CheckContainShare()==true)
        {
            SharedPreferences aaa = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String json = aaa.getString("ListProduct", null);
            Type type = new TypeToken<ArrayList<Product>>() {
            }.getType();
            //Lưu vào brraylist
            FavoriteList = gson.fromJson(json, type);
        }



    }
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
                Log.d("onFocusChange","ON");
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("onQueryTextSubmit","ON");
                Log.d("String INPUT", query);
                String searchOutput  = getResources().getString(R.string.result) + " \"" + "<b>" + query +"</b>"+ "\":";
                lnlo_kq.setVisibility(View.VISIBLE);
                txtv_kq.setText(Html.fromHtml(searchOutput ));
                arrayList = new ArrayList<Product>();
                eSearchTask = new SearchTask(query,"");
                eSearchTask.execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                Log.d("onQueryTextChange","ON");
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //Sự kiện khi nhấn back trong icon Search
                onBackPressed();
                return false;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Sự kiện khi ấn vào icon search

                return true;  // Return true to expand action view
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }



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


    public  class SearchTask extends AsyncTask<Void, Void, JSONArray> {
        private final String mTitle;
        private final String mDescription;

        JSONParser jsonParser = new JSONParser();
        private  String SEARCH_URL = ofastURL.search;

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
        protected void onPreExecute() {

        }
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
            if(result!= null)
            {
                try {
                    Log.d("Arrayyyy",result.toString());
//
                    for (int i = 0; i < result.length(); i++)
                    {
                        Log.d("AAAAAAAAAAAAA","BBBBBBBBBBBBBBBb");
                        JSONObject item = result.getJSONObject(i);
                        Log.d("Object",item.toString());
//                        String id = item.getString(TAG_ID);
                        String title = item.getString(TAG_TITLE);
                        String price = item.getString(TAG_PRICE);
                        String image = item.getString(TAG_IMG);
//                        String category_id = item.getString(TAG_CATEGORY_ID);
                         product = new Product(ofastURL.frontend_Web_image +image, title,price);
                        arrayList.add(product);
                    }
                    if(arrayList.size() != 0)
                    {
                        ShowList(true);
                        adapter = new Search_CustomListView(getApplicationContext(), R.layout.search_item, arrayList,Search.this);
                        lv_search.setAdapter(adapter);
                        lv_search.invalidate();
                        for(int i = 0; i< arrayList.size(); i++)
                        {
                            CheckAndAddFavorite(arrayList.get(i));
                        }

                        adapter.notifyDataSetChanged();


                    }
                    else {
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

    public void ShowList(boolean a)
    {
        if(a == true)
        {
            lv_search.setVisibility(View.VISIBLE);
            txtv_noproduct.setVisibility(View.GONE);
        }
        else {
            lv_search.setVisibility(View.GONE);
            txtv_noproduct.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onBackPressed() {
        SharedPreferences favoriteList = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor prefsEditor = favoriteList.edit();
        Gson gson = new Gson();
        String json = gson.toJson(FavoriteList);
        prefsEditor.putString("FavoriteList", json);
        prefsEditor.commit();
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), Home.class);
        hideSoftKeyboard(Search.this);
        startActivity(intent);
    }
    public void CheckAndAddFavorite(Product product)
    {
            if(product.isChecked())
            {
                FavoriteList.add(product);
            }else {
                FavoriteList.remove(product);
            }
        if(CheckContainShare() == false)
        {
            SharedPreferences favoriteList = PreferenceManager
                    .getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor prefsEditor = favoriteList.edit();
            Gson gson = new Gson();
            String json = gson.toJson(FavoriteList);
            prefsEditor.putString("FavoriteList", json);
            prefsEditor.commit();

        }
        Log.d("SIZE FAVORITE",String.valueOf(FavoriteList.size()));
    }
    private boolean CheckContainShare() {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("FavoriteList", "");
        if(json.isEmpty() == false)
        {
            return true;
        }
        else return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("OnResume",":::TRUE");
        if(CheckContainShare()==true)
        {
            SharedPreferences aaa = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String json = aaa.getString("ListProduct", null);
            Type type = new TypeToken<ArrayList<Product>>() {
            }.getType();
            //Lưu vào brraylist
            FavoriteList = gson.fromJson(json, type);
        }
    }

    /*---------------------------------------------------------------------------------------------|
    |-------------------------------Hàm ẩn Keyboard cho 1 Activity---------------------------------|
    |---------------------------------------------------------------------------------------------*/
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
