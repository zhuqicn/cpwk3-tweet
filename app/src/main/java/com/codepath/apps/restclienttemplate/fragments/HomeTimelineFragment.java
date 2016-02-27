package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

public class HomeTimelineFragment extends TweetsListFragment {
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void getTimeline(long sinceId, long maxId) {
    client.getHomeTimeline(getHandler(), sinceId, maxId);
  }
}
