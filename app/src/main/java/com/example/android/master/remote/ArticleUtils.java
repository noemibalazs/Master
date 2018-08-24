package com.example.android.master.remote;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.master.data.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ArticleUtils {

    private static final String TAG = ArticleUtils.class.getSimpleName();

    private static final String ARTICLE_ID = "id";
    private static final String ARTICLE_TITLE = "title";
    private static final String ARTICLE_AUTHOR = "author";
    private static final String ARTICLE_BODY = "body";
    private static final String ARTICLE_THUMB = "thumb";
    private static final String ARTICLE_PHOTO = "photo";
    private static final String ARTICLE_DATE = "published_date";

    public ArticleUtils(){}

    public static List<Article> fetchData(String string){

        URL url = createUrl(string);
        String jsonRequest = null;
        try{
            jsonRequest = makeHttpRequest(url);
        } catch (IOException e) {
            Log.v(TAG, "Error making HTTP request" + e);
        }

        List<Article> articles = extractDataFromJson(jsonRequest);
        return  articles;

    }

    private static URL createUrl(String string){
        URL url = null;
        try {
            url = new URL(string);
        } catch (MalformedURLException e) {
            Log.v(TAG, "Error building url" + e);
        }

        return url;
    }

    private static String makeHttpRequest(URL url)throws IOException{
        String jsonResponse = "";
        if (url == null){
            return  jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.v(TAG, "Error getting response code" + urlConnection.getResponseCode());
            }

        } catch (IOException e){
            Log.v(TAG, "Error retrieving Article results" + e);
        } finally {
            if (urlConnection!= null){
                urlConnection.disconnect();
            } if (inputStream != null){
                inputStream.close();
            }
        }

        return jsonResponse;

    }

    private static String readFromStream (InputStream inputStream) throws IOException{
        StringBuilder builder = new StringBuilder();
        if (inputStream != null){
            InputStreamReader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while (line!=null){
                builder.append(line);
                line = bufferedReader.readLine();
            }
        }

        return builder.toString();
    }

    private static List<Article> extractDataFromJson(String stringJson){

        if (TextUtils.isEmpty(stringJson)){
            return  null;
        }

        List<Article> articles = new ArrayList<>();

        try{

            JSONArray array = new JSONArray(stringJson);

            for (int i = 0; i < array.length(); i++){

                JSONObject object = array.getJSONObject(i);

                int id = object.getInt(ARTICLE_ID);
                String title = object.getString(ARTICLE_TITLE);
                String author = object.getString(ARTICLE_AUTHOR);
                String body = object.getString(ARTICLE_BODY);
                String thumb = object.getString(ARTICLE_THUMB);
                String photo = object.getString(ARTICLE_PHOTO);
                String date = object.getString(ARTICLE_DATE);

                Article art = new Article(id, title, author, body, thumb, photo, date);
                articles.add(art);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return articles;
    }
}
