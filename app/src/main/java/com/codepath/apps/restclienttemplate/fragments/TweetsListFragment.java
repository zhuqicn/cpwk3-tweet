package com.codepath.apps.restclienttemplate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.utils.DividerItemDecoration;
import com.codepath.apps.restclienttemplate.utils.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.utils.ItemClickSupport;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.adapters.TweetsAdapter;
import com.codepath.apps.restclienttemplate.networks.TwitterApplication;
import com.codepath.apps.restclienttemplate.networks.TwitterClient;
import com.codepath.apps.restclienttemplate.activities.ProfileActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

abstract public class TweetsListFragment extends Fragment {
  protected TwitterClient client;
  private String profileUrl;
  private long minUid;
  private long maxUid;
  protected ArrayList<Tweet> tweets;
  protected TweetsAdapter aTweets;

  @Bind(R.id.rvTweets) RecyclerView rvTweets;
  SwipeRefreshLayout swipeContainer;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    tweets = new ArrayList<>();
    aTweets = new TweetsAdapter(tweets);
    minUid= 0;
    maxUid = 0;
    client = TwitterApplication.getRestClient();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
    ButterKnife.bind(this, v);



    LinearLayoutManager m = new LinearLayoutManager(getContext());
    rvTweets.setLayoutManager(m);
    rvTweets.addItemDecoration(new DividerItemDecoration(getResources().
      getDrawable(R.drawable.abc_list_divider_mtrl_alpha)));
    rvTweets.setAdapter(aTweets);

    rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(m) {
      @Override
      public void onLoadMore(int page, int totalItemsCount) {
        populateTimeline(false);
      }
    });

    ItemClickSupport.addTo(rvTweets).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
      @Override
      public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        long userId = tweets.get(position).getUser().getUid();
        Intent i = new Intent(getContext(), ProfileActivity.class);
        i.putExtra("user_id", userId);
        startActivity(i);
      }
    });

    swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        minUid = 0;
        maxUid = 0;
        aTweets.clear();
        populateTimeline(false);
      }
    });
    // Configure the refreshing colors
    swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
      android.R.color.holo_green_light,
      android.R.color.holo_orange_light,
      android.R.color.holo_red_light);
    populateTimeline(false);
    return v;
  }

  private void getProfileUrl() {
    client.getProfile(new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
          TweetsListFragment.this.profileUrl = response.getString("profile_image_url");
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });
  }

  protected AsyncHttpResponseHandler getHandler() {
    return new JsonHttpResponseHandler() {
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
        hideProgressBar();
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        swipeContainer.setRefreshing(false);
        hideProgressBar();
      }
    };
  }

  protected abstract void getTimeline(long sicneId, long maxId);

  private void populateTimeline(boolean newPostOnly) {
    showProgressBar();
    long sinceId = 0;
    long maxId = 0;
    if (newPostOnly) {
      sinceId = maxUid + 1;
    } else {
      maxId = minUid - 1;
    }
    getTimeline(sinceId, maxId);
  }

  public interface iProgressBar {
    void showProgressBar();
    void hideProgressBar();
  }

  private void showProgressBar() {
    FragmentActivity activity = getActivity();
    if (activity instanceof iProgressBar) {
      ((iProgressBar)activity).showProgressBar();
    }
  }

  private void hideProgressBar() {
    FragmentActivity activity = getActivity();
    if (activity instanceof iProgressBar) {
      ((iProgressBar)activity).hideProgressBar();
    }
  }
}
