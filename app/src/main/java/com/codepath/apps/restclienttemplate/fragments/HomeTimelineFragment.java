package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

public class HomeTimelineFragment extends TweetsListFragment {
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void getTimeline(long sinceId, long maxId) {
    Log.d("HOME", sinceId + "," + maxId);
    client.getHomeTimeline(getHandler(), sinceId, maxId);
  }
}
