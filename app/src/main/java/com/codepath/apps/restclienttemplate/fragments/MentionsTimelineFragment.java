package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

public class MentionsTimelineFragment extends TweetsListFragment {
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void getTimeline(long sinceId, long maxId) {
    client.getMentionsTimeline(getHandler(), sinceId, maxId);
  }
}
