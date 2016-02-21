package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ComposeActivity extends AppCompatActivity {
  @Bind(R.id.btnCancel) ImageButton btnCancel;
  @Bind(R.id.btnTweet) Button btnTweet;
  @Bind(R.id.ivProfile) ImageView ivProfile;
  @Bind(R.id.etTweet) EditText etTweet;
  @Bind(R.id.tvCount) TextView tvCount;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_compose);
    ButterKnife.bind(this);

    Glide.with(this).load(getIntent().getStringExtra("prof_url")).into(ivProfile);
  }

  @OnClick(R.id.btnCancel)
  public void onCancel(View view) {
    finish();
  }

  @OnClick(R.id.btnTweet)
  public void onTweet(View view) {
    String text = etTweet.getText().toString();
    if (text.length() > 140) {
      Toast.makeText(this, "Tweet can not exceed 140 characters", Toast.LENGTH_LONG).show();
      return;
    }
    if (text.length() == 0) {
      Toast.makeText(this, "Tweet can not be empty", Toast.LENGTH_LONG).show();
      return;
    }
    Intent i = getIntent();
    i.putExtra("tweet", text);
    setResult(RESULT_OK, i);
    finish();
  }

  @OnTextChanged(R.id.etTweet)
  public void onChange() {
    tvCount.setText("" + (140 - etTweet.getText().length()));
  }

}
