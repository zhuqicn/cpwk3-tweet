package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;

import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity {

  public final int REQUEST_CODE = 20;
  private TweetsListFragment tweetsListFragment;

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
        /*
        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
        i.putExtra("prof_url", profileUrl);
        startActivityForResult(i, REQUEST_CODE);
        */
      }
    });

    ButterKnife.bind(this);


    if (savedInstanceState == null) {
      tweetsListFragment = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    /*
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
    */
  }
}
