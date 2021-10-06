package com.example.findbooks;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class BookUtils {

    private static String LOG_TAG = "BookUtils";

    public BookUtils(){

    }
    public static List<Book> fetchGoogleBooksData(String requestUrl){
        URL url =createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG,"Problem making the Http request",e);
        }
        List<Book> books = extractBooks(jsonResponse);
        return books;
    }

    private static URL createUrl(String requestUrl){
        URL url = null;
        try{
            url = new URL(requestUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Problem building the Url ",e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse="";

        if(url==null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection =null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode()==200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else{
                Log.e(LOG_TAG,"Error response Code "+urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line!=null){
                output.append(line);
                line=reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Book> extractBooks(String bookJSON){
        if(TextUtils.isEmpty(bookJSON)){
            return null;
        }
        List<Book> googleBooks = new ArrayList<>();
        try{
            JSONObject root = new JSONObject(bookJSON);
            int count = root.getInt("totalItems");
            if(count!=0) {
                JSONArray array = root.optJSONArray("items");
                for(int i=0;i<array.length();i++) {
                    String author="",publishedDate="",buy="",publisher="",language="",description="",image="";
                    double price=0;
                    JSONObject item = array.optJSONObject(i);
                    JSONObject info = item.optJSONObject("volumeInfo");
                    String title = info.getString("title");
                    if(info.has("authors")){
                        JSONArray author_names = info.optJSONArray("authors");
                        author = author_names.getString(0);
                        for (int j = 1; j < author_names.length(); j++) {
                            author += "," + author_names.getString(j);
                        }
                    }else{
                        author="--------";
                    }
                    if(info.has("publisher")){
                        publisher = info.getString("publisher");
                    }else{
                        publisher="--------";
                    }
                    if(info.has("publishedDate")){
                        publishedDate = info.getString("publishedDate");
                    }else{
                        publishedDate="--------";
                    }
                    if(info.has("description")){
                        description = "        "+info.getString("description");
                    }else{
                        description="--------";
                    }
                    if(info.has("language")){
                        language = info.getString("language");
                    }
                    else{
                        language="English";
                    }
                    if(info.has("imageLinks")){
                        JSONObject imageLinks = info.optJSONObject("imageLinks");
                        image = imageLinks.getString("thumbnail");
                    }
                    JSONObject saleInfo = item.optJSONObject("saleInfo");
                    String saleability = saleInfo.getString("saleability");
                    if (saleability.equals("FOR_SALE")) {
                        JSONObject retailPrice = saleInfo.optJSONObject("retailPrice");
                        price = retailPrice.getDouble("amount");
                        buy =saleInfo.getString("buyLink");
                    }
                    JSONObject access = item.optJSONObject("accessInfo");
                    String read = access.getString("webReaderLink");
                    googleBooks.add(new Book(image, title, author, publishedDate, publisher,language, description, read, buy, price));
                }
            }
        }catch (JSONException e){
            Log.e(LOG_TAG,"Problem parsing the googleBooks information ",e);
        }
        return googleBooks;
    }

}
