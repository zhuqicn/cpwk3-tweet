package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codepath.apps.restclienttemplate.models.Tweet;

public class HomeTimelineFragment extends TweetsListFragment {
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void getTimeline(long sinceId, long maxId) {
    client.getHomeTimeline(getHandler(), sinceId, maxId);
  }

  public void tweeted(Tweet t) {
    tweets.add(0, t);
    aTweets.notifyItemInserted(0);
    rvTweets.scrollToPosition(0);
  }
}
