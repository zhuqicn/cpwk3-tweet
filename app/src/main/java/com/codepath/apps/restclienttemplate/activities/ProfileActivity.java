package com.codepath.apps.restclienttemplate.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {
  private long userId;
  private TwitterClient client;
  private User user;

  @Bind(R.id.ivProfileImage) ImageView ivProfileImage;
  @Bind(R.id.tvName) TextView tvName;
  @Bind(R.id.tvTagline) TextView tvTagline;
  @Bind(R.id.tvFollowing) TextView tvFollowing;
  @Bind(R.id.tvFolowers) TextView tvFollowers;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    ButterKnife.bind(this);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    client = TwitterApplication.getRestClient();
    userId = getIntent().getLongExtra("user_id", 0);
    AsyncHttpResponseHandler handler = new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        user = User.fromJSON(response);
        getSupportActionBar().setTitle("@" + user.getScreenName());
        tvName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowers() + " Followers");
        tvFollowing.setText(user.getFriends() + " Following");
        Glide.with(getApplicationContext()).load(user.getProfileImageUrl()).into(ivProfileImage);
      }
    };
    if (userId == 0) {
      client.getProfile(handler);
    } else {
      client.getUserInfo(handler, userId, null);
    }
    if (savedInstanceState == null) {
      UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(userId);
      getSupportFragmentManager().
        beginTransaction().
        replace(R.id.flUserTimeline, userTimelineFragment).
        commit();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return true;
  }
}
