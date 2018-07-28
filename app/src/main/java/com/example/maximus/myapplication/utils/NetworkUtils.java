package com.example.maximus.myapplication.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.maximus.myapplication.model.GitHubModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private final static String GITHUB_BASE_URL =
            "https://api.github.com/search/repositories";
    private final static String PARAM_QUERY = "q";
    private final static String PARAM_SORT = "sort";
    private static String sortByStars = "stars";




//    public List<GitHubModel> buildUrl() {
//        List<GitHubModel> items = new ArrayList<>();
//
//        try {
//        String buildUri = Uri.parse(GITHUB_BASE_URL)
//                .buildUpon()
//                .appendQueryParameter(PARAM_QUERY, "android")
//                .appendQueryParameter(PARAM_SORT, sortByStars)
//                .build().toString();
//
//
//            String jsonString = getUrlString(buildUri);
//            JSONObject jsonBody = new JSONObject(jsonString);
//            parseJson(items, jsonBody);
//
//            Log.i(TAG, "Received JSON: " + jsonString);
//        } catch (IOException e) {
//            Log.e(TAG, "Failed to fetch items", e);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return items;
//    }



    public static URL buildUrl(String githubSearchQuery) {
        Uri buildUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, githubSearchQuery)
                .appendQueryParameter(PARAM_SORT, sortByStars)
                .build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException ex) {
            Log.e(TAG, "buildUrl: ", ex);
        }

        return url;
    }

    //Took is from SunshineApp App!
    public static List<GitHubModel> parseJSON(Context context, String JSONStr) throws JSONException {


        final String ITEMS = "items";

        final String FULL_NAME = "full_name";
        final String DESCRIPTION = "description";


        List<GitHubModel> gitHubModelArray = new ArrayList<>();
        GitHubModel gitHubModel;


        JSONObject jsonObject = new JSONObject(JSONStr);

        JSONArray itemsArray = jsonObject.getJSONArray(ITEMS);




        for (int i = 0; i < itemsArray.length(); i++) {
            String description;
            String fullName;
            String numberOfStars;

            JSONObject jsonObject1 = itemsArray.getJSONObject(i);

            description = jsonObject1.getString(DESCRIPTION);
            fullName = jsonObject1.getString(FULL_NAME);
            numberOfStars = jsonObject1.getString("stargazers_count");

            gitHubModel = new GitHubModel(fullName, numberOfStars, description);


            gitHubModelArray.add(gitHubModel);




        }


        return gitHubModelArray;
    }


//
//    public void parseJson(List<GitHubModel> itemsArray, JSONObject jsonBody) throws JSONException {
//        JSONArray itemsJsonArray = jsonBody.getJSONArray("items");
//
//        String jsonArray = itemsJsonArray.toString();
//
//        Type type = new TypeToken<ArrayList<GitHubModel>>() {
//        }.getType();
//
//        List<GitHubModel> gitHubModels = new Gson().fromJson(jsonArray, type);
//
//        itemsArray.addAll(gitHubModels);
//    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in).useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        } finally {
            urlConnection.disconnect();
        }
    }

}
