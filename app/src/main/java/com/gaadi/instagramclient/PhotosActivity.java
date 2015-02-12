package com.gaadi.instagramclient;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends ActionBarActivity {

    public static final String CLIENT_ID = "85d5a24f67614fce8410e10d47363346";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        // SEND OUT API REQUEST to POPULAR PHOTOS
        photos = new ArrayList<>();
        // 1. Create the adapter linking it to source
        aPhotos = new InstagramPhotosAdapter(this, photos);
        // 2. Find the ListView from the layout
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        // 3. Set the adapter binding it to the ListView
        lvPhotos.setAdapter(aPhotos);
        // Fetch the popular photos
        fetchPopularPhotos();
    }

    // Trigger API request
    private void fetchPopularPhotos() {
        /*
        - Client ID: 85d5a24f67614fce8410e10d47363346
        - media/popular: https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
         - Response
        */

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        // Create the network client
        AsyncHttpClient client = new AsyncHttpClient();
        // Trigger the GET request
        client.get(url, null, new JsonHttpResponseHandler() {
            // OnSuccess (worked, 200)

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Expecting a JSON object
                // Type: {"data" => [x] => "type"}
                // Log.i("DEBUG: ", response.toString());
                // Iterate each of the photo items and decode the item into a java object
                JSONArray photosJSON;
                try {
                    photosJSON = response.getJSONArray("data");
                    // Iterate array of posts
                    for (int i = 0; i < photosJSON.length(); i++) {
                        // get the JSON object at that position
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        // decode the attributes of the json into a data model
                        InstagramPhoto photo = new InstagramPhoto();
                        // Author: {"data" => [x] => "user" => "username"}
                        if (photoJSON.optJSONObject("user") != null) {
                            photo.username = photoJSON.getJSONObject("user").getString("username");
                        }
                        // Caption: {"data" => [x] => "caption" => "text"}
                        if (photoJSON.optJSONObject("caption") != null) {
                            photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        }
                        // Image attributes
                        if (photoJSON.optJSONObject("images") != null) {
                            // Image URL: {"data" => [x] => "images" => "standard_resolution" => "url"}
                            photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                            // Image Height: {"data" => [x] => "images" => "standard_resolution" => "height"}
                            photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        }
                        // Likes Count: {"data" => [x] => "likes" => "count"}
                        if (photoJSON.optJSONObject("likes") != null) {
                            photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        }
                        // Add decoded object to photos array
                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // callback
                aPhotos.notifyDataSetChanged();
            }

            // onFailure (failed)

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // DO SOMETHING
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
