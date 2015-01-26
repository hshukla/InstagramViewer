package com.example.harshil.instagramviewer;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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

import static android.text.format.DateUtils.SECOND_IN_MILLIS;
import static android.text.format.DateUtils.getRelativeTimeSpanString;
import static java.lang.System.currentTimeMillis;


public class PhotosActivity extends ActionBarActivity {

    public static String CLIENT_ID = "ebf6c242e37c416e8e2ed89db66f8712";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter photosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        fetchPopularPhotos();
    }

    private void fetchPopularPhotos() {
        photos = new ArrayList<>();
        photosAdapter = new InstagramPhotosAdapter(this, photos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(photosAdapter);
        // https://api.instagram.com/v1/media/popular?client_id=ebf6c242e37c416e8e2ed89db66f8712
        // {data => [x] => [user]}

        // setup the popular url endpoint
        String popularUrl = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        // create the network client
        AsyncHttpClient httpClient = new AsyncHttpClient();

        // trigger the network client
        httpClient.get(popularUrl, new JsonHttpResponseHandler() {
            // defines success and failure response
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // url, height, username, caption
                Log.i("INFO", response.toString());
                //super.onSuccess(statusCode, headers, response);
                JSONArray photosJson = null;

                try {
                    photos.clear();
                    photosJson = response.getJSONArray("data");
                    for (int i=0; i < photosJson.length(); i++) {
                        JSONObject photoJson = photosJson.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();

                        photo.userName = photoJson.getJSONObject("user").getString("username");
                        photo.profileImageUrl = photoJson.getJSONObject("user").getString("profile_picture");
                        photo.imageUrl = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.postTimestamp = (String) getRelativeTimeSpanString(photoJson.getLong("created_time")*1000, currentTimeMillis(), SECOND_IN_MILLIS);

                        if (photoJson.optJSONObject("caption") != null) {
                            photo.caption = photoJson.getJSONObject("caption").getString("text");
                        }

                        photos.add(photo);
                    }
                    photosAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
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
