package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
  private List<Tweet> mTweets;
  // Pass in the contact array into the constructor
  public TweetsAdapter(List<Tweet> tweets) {
    mTweets = tweets;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    View tweetView = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
    ViewHolder viewHolder = new ViewHolder(tweetView);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    Tweet t = mTweets.get(position);
    holder.tvText.setText(t.getBody());
    holder.tvUser.setText(t.getUser().getDisplay());
    holder.tvTime.setText(t.getRelativeTimeAgo());
    holder.ivProfile.setImageResource(0);
    holder.ivImage.setVisibility(View.GONE);

    Glide.with(holder.itemView.getContext()).load(t.getUser().getProfileImageUrl()).into(holder.ivProfile);
    if (!TextUtils.isEmpty(t.getPhotoUrl())) {
      holder.ivImage.setImageResource(0);
      Glide.with(holder.itemView.getContext()).load(t.getPhotoUrl()).into(holder.ivImage);
      holder.ivImage.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public int getItemCount() {
    return mTweets.size();
  }

  public void clear() {
    mTweets.clear();
    notifyDataSetChanged();
  }

  // Add a list of items
  public void addAll(List<Tweet> list) {
    int curSize = mTweets.size();
    mTweets.addAll(list);
    notifyItemRangeInserted(curSize, list.size());
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.tvTime) TextView tvTime;
    @Bind(R.id.tvUser) TextView tvUser;
    @Bind(R.id.tvText) TextView tvText;
    @Bind(R.id.ivImage) ImageView ivImage;
    @Bind(R.id.ivProfile) ImageView ivProfile;

    // We also create a constructor that accepts the entire item row
    // and does the view lookups to find each subview
    public ViewHolder(View itemView) {
      // Stores the itemView in a public final member variable that can be used
      // to access the context from any ViewHolder instance.
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
