package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.codepath.apps.restclienttemplate.DividerItemDecoration;
import com.codepath.apps.restclienttemplate.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetsAdapter;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity {

  private TwitterClient client;
  private ArrayList<Tweet> tweets;
  private TweetsAdapter aTweets;
  private String profileUrl;
  private long minUid;
  private long maxUid;
  public final int REQUEST_CODE = 20;
  SwipeRefreshLayout swipeContainer;
  @Bind(R.id.rvTweets) RecyclerView rvTweets;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
        i.putExtra("prof_url", profileUrl);
        startActivityForResult(i, REQUEST_CODE);
      }
    });

    ButterKnife.bind(this);

    minUid= 0;
    maxUid = 0;
    tweets = new ArrayList<>();
    aTweets = new TweetsAdapter(tweets);
    rvTweets.setAdapter(aTweets);
    LinearLayoutManager m = new LinearLayoutManager(this);
    rvTweets.setLayoutManager(m);
    rvTweets.addItemDecoration(new DividerItemDecoration(getResources().
      getDrawable(R.drawable.abc_list_divider_mtrl_alpha)));
    client = TwitterApplication.getRestClient();
    rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(m) {
      @Override
      public void onLoadMore(int page, int totalItemsCount) {
        populateTimeline(false);
      }
    });

    swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        minUid = 0;
        maxUid = 0;
        populateTimeline(false);
      }
    });
    // Configure the refreshing colors
    swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
      android.R.color.holo_green_light,
      android.R.color.holo_orange_light,
      android.R.color.holo_red_light);
    getProfileUrl();
    populateTimeline(false);
  }

  private void getProfileUrl() {
    client.getProfilePic(new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
          TimelineActivity.this.profileUrl = response.getString("profile_image_url");
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });
  }

  private void populateTimeline(boolean newPostOnly) {
    long sinceId = 0;
    long maxId = 0;
    if (newPostOnly) {
      sinceId = maxUid + 1;
    } else {
      maxId = minUid - 1;
    }
    client.getHomeTimeline(new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        ArrayList<Tweet> tweets = Tweet.fromJSONArray(response);
        for (int i = 0; i < tweets.size(); i++) {
          long uid = tweets.get(i).getUid();
          if (minUid == 0) {
            minUid = uid;
          }
          if (uid < minUid) {
            minUid = uid;
          }
          if (uid > maxUid) {
            maxUid = uid;
          }
        }
        aTweets.addAll(Tweet.fromJSONArray(response));
        swipeContainer.setRefreshing(false);
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Log.d("DEBUG FAILURE", errorResponse.toString());
        swipeContainer.setRefreshing(false);
      }
    }, sinceId, maxId);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
      String text = data.getStringExtra("tweet");
      client.tweet(text, new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
          Tweet t = Tweet.fromJSON(response);
          tweets.add(0, t);
          aTweets.notifyItemInserted(0);
          rvTweets.scrollToPosition(0);
        }
      });
    }
  }
}
