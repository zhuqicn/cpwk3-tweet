package com.codepath.apps.restclienttemplate.models;

import android.text.Html;
import android.text.Spanned;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by qi_zhu on 2/15/16.
 */
public class User {
  public String getName() {
    return name;
  }

  public long getUid() {
    return uid;
  }

  public String getScreenName() {
    return screenName;
  }

  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  private String name;
  private long uid;
  private String screenName;
  private String profileImageUrl;

  public void setName(String name) {
    this.name = name;
  }

  public void setUid(long uid) {
    this.uid = uid;
  }

  public void setScreenName(String screenName) {
    this.screenName = screenName;
  }

  public void setProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }

  public static User fromJSON(JSONObject jsonObject) {
    User user = new User();
    try {
      user.name = jsonObject.getString("name");
      user.uid = jsonObject.getLong("id");
      user.screenName = jsonObject.getString("screen_name");
      user.profileImageUrl = jsonObject.getString("profile_image_url");
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return user;
  }

  public Spanned getDisplay() {
    return Html.fromHtml("<font color=black><b>" + this.name + "</b></font> <font color=grey>@" + this.screenName + "</font>");
  }
}
