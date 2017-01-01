package com.feiyu.scripsaying.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.feiyu.scripsaying.R;


public class ConversationActivity extends FragmentActivity {
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        mTextView = (TextView) findViewById(R.id.title);
        mTextView.setText(getIntent().getData().getQueryParameter("title"));
        Log.e("type", "type is:" + getIntent().getData().getPath());
    }
}
