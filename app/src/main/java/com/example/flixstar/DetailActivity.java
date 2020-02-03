package com.example.flixstar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import models.Movie;
import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {
    private static final String YOUTUBE_API_KEY="AIzaSyD9SO6MkU1AIHJwhqMLueher27d4X9ia_c";
    TextView tvTitle;
    TextView tvOverview;
    RatingBar ratingBar;
    YouTubePlayerView youTubePlayerView;
    final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle=findViewById(R.id.tvTitle);
        tvOverview=findViewById(R.id.tvOverview);
        ratingBar=findViewById(R.id.ratingBar);
        youTubePlayerView=findViewById(R.id.player);

        Movie movie= Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        assert movie != null;
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingBar.setRating((float)movie.getRating());

        AsyncHttpClient client=new AsyncHttpClient();

      client.get(String.format(VIDEOS_URL, movie.getMovieId()), new JsonHttpResponseHandler() {
          @Override
          public void onSuccess(int statusCode, Headers headers, JSON json) {
              try {
                  JSONArray results = json.jsonObject.getJSONArray("results");
                  if (results.length() == 0) {
                      return;
                  }
                  String youTubeKey = results.getJSONObject(0).getString("key");
                  Log.d("DetailsActivity", youTubeKey);
                  initializeYoutube(youTubeKey);
              } catch (JSONException e) {
                  e.printStackTrace();
                  Log.e("DetailsActivity", "Failed JSON");
              }
          }

          @Override
          public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

          }
      });


    }

    private void initializeYoutube(final String youTubeKey) {
        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("DetailActivity", "onInitializationSuccess: ");
                youTubePlayer.cueVideo(youTubeKey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("", "onInitializationFailure: ");
                Log.d("DetailActivity", "onInitializationFailure: ");
            }
        });
    }
}
