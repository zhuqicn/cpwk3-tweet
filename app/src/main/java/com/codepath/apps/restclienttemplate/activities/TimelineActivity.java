package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.MentionsTimelineFragment;

import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity {

  public final int REQUEST_CODE = 20;

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

    ViewPager vpPager = (ViewPager)findViewById(R.id.viewpager);
    vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
    PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabs);
    tabStrip.setViewPager(vpPager);



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

  public class TweetsPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = { "Home", "Mentions" };

    public TweetsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      if (position == 0) {
        return new HomeTimelineFragment();
      } else if (position == 1) {
        return new MentionsTimelineFragment();
      } else {
        return null;
      }
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return tabTitles[position];
    }

    @Override
    public int getCount() {
      return tabTitles.length;
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    return super.onOptionsItemSelected(item);
  }

  public void onProfileView(MenuItem mi) {
    Intent i = new Intent(this, ProfileActivity.class);
    startActivity(i);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_timeline, menu);
    return true;
  }
}
