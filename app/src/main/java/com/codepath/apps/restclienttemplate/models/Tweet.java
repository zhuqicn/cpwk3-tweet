package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by qi_zhu on 2/15/16.
 */
public class Tweet {
  private String body;
  private long uid;
  private User user;
  private String createAt;
  private String photoUrl;

  public String getBody() {
    return body;
  }

  public long getUid() {
    return uid;
  }

  public User getUser() {
    return user;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public String getCreateAt() {
    return createAt;
  }

  public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
    ArrayList<Tweet> tweets = new ArrayList<>();
    for (int i = 0; i < jsonArray.length(); i++) {
      try {
        Tweet tweet = Tweet.fromJSON(jsonArray.getJSONObject(i));
        if (tweet != null) {
          tweets.add(tweet);
        }
      } catch (JSONException e) {
        e.printStackTrace();
        continue;
      }
    }
    return tweets;
  }

  private static String getPhotoUrl(JSONObject jsonTweet) {
    try {
      if (!jsonTweet.has("entities") || !jsonTweet.getJSONObject("entities").has("media")) {
        return null;
      }
      JSONArray mediaArray = jsonTweet.getJSONObject("entities").getJSONArray("media");
      for (int i = 0; i < mediaArray.length(); i++) {
        JSONObject media = mediaArray.getJSONObject(i);
        if (media.getString("type").equals("photo")) {
          return media.getString("media_url");
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
    return null;
  }

  public static Tweet fromJSON(JSONObject jsonObject) {
    Tweet tweet = new Tweet();
    try {
      tweet.body = jsonObject.getString("text");
      tweet.uid = jsonObject.getLong("id");
      tweet.createAt = jsonObject.getString("created_at");
      tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
      tweet.photoUrl = getPhotoUrl(jsonObject);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return tweet;
  }

  // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
  public String getRelativeTimeAgo() {
    String rawJsonDate = getCreateAt();
    String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
    SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
    sf.setLenient(true);

    String relativeDate = "";
    try {
      long dateMillis = sf.parse(rawJsonDate).getTime();
      relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return relativeDate.
      replace(" minutes", "m").
      replace(" minute", "m").
      replace(" hours", "h").
      replace(" hour", "h").
      replace(" days", "d").
      replace(" day", "d").
      replace(" weeks", "w").
      replace(" week", "w").
      replace(" ago", "");
  }
}
