package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by qi_zhu on 2/26/16.
 */
public class UserTimelineFragment extends TweetsListFragment {


  public static UserTimelineFragment newInstance(long userId) {
    UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
    Bundle args = new Bundle();
    args.putLong("user_id", userId);
    userTimelineFragment.setArguments(args);
    return userTimelineFragment;
  }



  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void getTimeline(long sinceId, long maxId) {
    long userId = getArguments().getLong("user_id");
    client.getUserTimeline(getHandler(), null, userId, sinceId, maxId);
  }
}
