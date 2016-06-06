package com.hung.ofastapp.CreateConnection;

import android.util.Log;

import com.hung.ofastapp.Objects.Comment;
import com.hung.ofastapp.Objects.Product;
import com.hung.ofastapp.Objects.ThuongHieu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hung on 11/24/2015.
 */

public class JSONParser{
    String charset = "UTF-8";
    HttpURLConnection urlConnection;
    StringBuilder result = new StringBuilder();
    URL urlObj;
    JSONObject jObj = null;
    JSONArray jArr = null;
    StringBuilder sbParams;
    String paramsString;
    //Biến currentOffset hỗ trợ cho việc Loadmore
    static int currentOffset = 0;


    //POST dữ liệu lên, nhận về 1 JSONObject kết quả.
    public JSONObject makeHttpRequest(String url, String method,
                                      HashMap<String, String> params) {
        paramsString = makePostObj(params);
        if (method.equals("POST")) {
            doPost(url);
        } else if (method.equals("GET")) {
            doGet(url);
        }else if(method.equals("JSONPOST")){
            doPostJson(url);
        }

        try {
            //Receive the response from the server
            int httpResult = urlConnection.getResponseCode();
            Log.d("JSON Parser", "HTTP Code: " + httpResult + " " );
            if(httpResult == HttpURLConnection.HTTP_OK) {

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                Log.d("JSON Parser", "result: " + result.toString());
                reader.close();
            }

            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(result.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON Object
        return jObj;
    }


    //POST dữ liệu lên, nhận về 1 JSONARRAY kết quả.
    public JSONArray makeHttpRequests(String url, String method,
                                      HashMap<String, String> params) {
        paramsString = makePostObj(params);
        if (method.equals("POST")) {
            doPost(url);
        } else if (method.equals("GET")) {
            doGet(url);
        }else if(method.equals("JSONPOST")){
            doPostJson(url);
        }

        try {
            //Receive the response from the server
            int httpResult = urlConnection.getResponseCode();
            Log.d("JSON Parser", "HTTP Code: " + httpResult + " " );
            if(httpResult == HttpURLConnection.HTTP_OK) {

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                Log.d("JSON Parser", "result: " + result.toString());
                reader.close();
            }

            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }



        // try parse the string to a JSON object
        try {
            jArr = new JSONArray(result.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON Array
        return jArr;
    }

    /* =================================================================================
       ======================== Make Http Request to Post Json =========================
       ========================== Add by Khang-va 21-04-2016 ===========================
       =================================================================================*/
    public JSONObject makeJsonHttpRequest(String url, JSONObject data) {

        try {
            urlObj = new URL(url);

            urlConnection = (HttpURLConnection) urlObj.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            urlConnection.connect();

            OutputStreamWriter os = new OutputStreamWriter(urlConnection.getOutputStream());
            os.write(data.toString());
            os.flush();
            os.close();
            os.close();

            //Receive the response from the server
            int httpResult = urlConnection.getResponseCode();
            Log.d("JSON Parser", "HTTP Code: " + httpResult + " " );
            if(httpResult == HttpURLConnection.HTTP_OK) {

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                Log.d("JSON Parser", "result: " + result.toString());
                reader.close();
            }

            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(result.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // return JSON Object
        return jObj;
    }

    public void doPost(String url){
        // request method is POST
        try {
            urlObj = new URL(url);

            urlConnection = (HttpURLConnection) urlObj.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.connect();

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            Log.d("Send:" , paramsString);
            writer.write(paramsString);
            writer.flush();
            writer.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("DoPost", "post error");
        }
    }

    public void doGet(String url){
        // request method is GET

        if (sbParams.length() != 0) {
            url += "?" + paramsString;
        }

        try {
            urlObj = new URL(url);

            urlConnection = (HttpURLConnection) urlObj.openConnection();

            urlConnection.setDoOutput(false);

            urlConnection.setRequestMethod("GET");

            urlConnection.setRequestProperty("Accept-Charset", charset);

            urlConnection.setConnectTimeout(15000);

            urlConnection.connect();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /* =================================================================================
       =================== Trash function for Post Json Request ========================
       ========================== Add by Khang-va 21-04-2016 ===========================
       ======================== Copyright  makeJsonHttpRequest =========================
       =================================================================================*/

    public void doPostJson(String url){
        // request method is POST
        try {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("khang", "25 da thanh cong nha");
            jsonParam.put("description", "Real");
            jsonParam.put("enable", "true");

            urlObj = new URL(url);

            urlConnection = (HttpURLConnection) urlObj.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();

            OutputStreamWriter os = new OutputStreamWriter(urlConnection.getOutputStream());
            os.write(jsonParam.toString());
            os.flush();
            os.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("DoPost", "post error");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String makePostObj(HashMap<String, String> params) {

        sbParams = new StringBuilder();
        try {
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    sbParams.append("&");

                sbParams.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                sbParams.append("=");
                sbParams.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return sbParams.toString();

        } catch (Exception e) {
            Log.d("makePostObj", e.toString());
        }

        return null;
    }

    //Hàm đọc file Js để get Thương HIệu
    public ArrayList<ThuongHieu> Parse(String json){

        try {
            ThuongHieu thuongHieu;
            JSONArray jsonArray = new JSONArray(json);
            List<String> tenthuonghieu = new ArrayList<String>();
            List<String> linkImage = new ArrayList<String>();
            List<String> img = new ArrayList<String>();
            ArrayList<ThuongHieu> arrayList = new ArrayList<ThuongHieu>();
            ArrayList<ThuongHieu> brraylist = new ArrayList<ThuongHieu>();
            List<String> id_thuonghieu = new ArrayList<String>();

            //Hàm lấy một dãy trong 1 mảng để show
            linkImage = loadImageName(jsonArray, currentOffset, 12);

            for(int i=currentOffset; i<linkImage.size()+currentOffset ;i++)
            {
                JSONObject jb = jsonArray.getJSONObject(i);
                jb.getString("brand_name");
                tenthuonghieu.add(jb.getString("brand_name"));
                jb.getString("id");
                id_thuonghieu.add(jb.getString("id"));
            }
            Log.d("id thuong hieu ", id_thuonghieu.toString());
            currentOffset += linkImage.size();
            for(int i = 0; i<linkImage.size(); i++)
            {
                String a = (ofastURL.brand_image + linkImage.get(i));
                img.add(a);
                thuongHieu = new ThuongHieu(img.get(i),tenthuonghieu.get(i),id_thuonghieu.get(i));
                arrayList.add(thuongHieu);

            }
            brraylist = arrayList;
           if(arrayList.size() == jsonArray.length())
           {
               currentOffset =0;
//               arrayList.clear();
           }
           Log.d("jkahsdkjhasd", String.valueOf(jsonArray.length()));
            Log.d("ssssssssss", String.valueOf(arrayList.size()));

            return brraylist;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    // Hàm đọc JSON để get Product
    public ArrayList<Product> getProduct(String json){

        try {
            Product product;
            ArrayList<Product> arrayList= new ArrayList<Product>();
            List<Integer> id = new ArrayList<Integer>();
            List<String> link_images = new ArrayList<String>();
            List<String> images = new ArrayList<String>();
            List<String> name = new ArrayList<String>();
            List<String> price = new ArrayList<String>();
            List<String> detail = new ArrayList<String>();
            JSONArray jsonArray = new JSONArray(json);
            Log.d("JSON TỪ WEB:::", json);
            //Get Link ảnh
            for(int i=0; i<jsonArray.length();i++)
            {
                JSONObject jb = jsonArray.getJSONObject(i);
                jb.getString("id");
                id.add(jb.getInt("id"));
                jb.getString("images");
                link_images.add(jb.getString("images"));
                jb.getString("title");
                name.add(jb.getString("title"));
                jb.getString("price");
                price.add(jb.getString("price"));
                jb.getString("detail");
                detail.add(jb.getString("detail"));
            }

            //Get product
            for(int i = 0; i<link_images.size(); i++)
            {
                String a = (ofastURL.frontend_Web_image + link_images.get(i));
                images.add(a);
                product = new Product(id.get(i),images.get(i),name.get(i),price.get(i), detail.get(i));
                arrayList.add(product);

            }
            Log.d("aaaaaaaaaaaaaaaaaaaa", String.valueOf(arrayList.size()));

            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    // Hàm đọc JSON để get Product Detail
    public ArrayList<Product> getProductDetail(String json){

        try {
            Product product;
            ArrayList<Product> arrayList= new ArrayList<Product>();
            List<Integer> id = new ArrayList<Integer>();
            List<String> link_images = new ArrayList<String>();
            List<String> images = new ArrayList<String>();
            List<String> name = new ArrayList<String>();
            List<String> price = new ArrayList<String>();

            JSONArray jsonArray = new JSONArray(json);
            for(int i=0; i<jsonArray.length();i++)
            {
                JSONObject jb = jsonArray.getJSONObject(i);
                jb.getString("id");
                id.add(jb.getInt("id"));
                jb.getString("images");
                link_images.add(jb.getString("images"));
                jb.getString("title");
                name.add(jb.getString("title"));
                jb.getString("price");
                price.add(jb.getString("price"));
            }

            //Get product
            for(int i = 0; i<link_images.size(); i++)
            {
                String a = (ofastURL.frontend_Web_image + link_images.get(i));
                images.add(a);
                product = new Product(id.get(i),images.get(i),name.get(i),price.get(i));
                arrayList.add(product);
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    // Hàm đọc JSON để get Comment
    public ArrayList<Comment> getComment(String json){

        try {
            Comment mComment;
            ArrayList<Comment> arrayList= new ArrayList<Comment>();
            List<Integer> author_id = new ArrayList<Integer>();
            List<String> comment = new ArrayList<String>();
            List<String> subComment = new ArrayList<String>();

            JSONArray jsonArray = new JSONArray(json);
            Log.d("JSON TỪ WEB:::", json);
            if(jsonArray == null)
            {
                arrayList = null;
            }
            else {
                for(int i=0; i<jsonArray.length();i++)
                {
                    JSONObject jb = jsonArray.getJSONObject(i);
                    jb.getString("author_id");
                    author_id.add(jb.getInt("author_id"));
                    jb.getString("comment");
                    comment.add(jb.getString("comment"));
                    jb.getString("subComment");
                    subComment.add(jb.getString("subComment"));
                }

                //Get product
                for(int i = 0; i<comment.size(); i++)
                {
                    mComment = new Comment(comment.get(i), author_id.get(i),subComment.get(i));
                    arrayList.add(mComment);
                }
            }
            //Get Link ảnh

            return arrayList;
        } catch (JSONException e) {
            Log.d("Lỗi rồi con à","Thì thôi vậy!");
            e.printStackTrace();
            return null;
        }
    }


    //Đọc JSON từ WEB với Địa chỉ có sẳn
    public static String getDatafromURL(String stringUrl) {
        BufferedReader reader = null;
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            StringBuilder sb = new StringBuilder();

            InputStreamReader is = new InputStreamReader(connection.getInputStream());

            reader = new BufferedReader(is);

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            Log.d("SBBBBBBB STRING:", sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //Hàm lấy từ vị trí offset tới vị trí number + offset!
    public List<String> loadImageName(JSONArray jsonArray, int offset, int number) throws JSONException {
        List<String> linkImage = new ArrayList<String>();
        for (int i = offset; i < offset + number; i++) {
            if(i<jsonArray.length()){
                JSONObject jb = jsonArray.getJSONObject(i);
                jb.getString("image");
                linkImage.add(jb.getString("image"));
            } else break;
        }
        return linkImage;

    }


}

