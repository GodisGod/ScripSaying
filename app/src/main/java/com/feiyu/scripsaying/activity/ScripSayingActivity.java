package com.feiyu.scripsaying.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.feiyu.scripsaying.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScripSayingActivity extends BaseActivity {

    @BindView(R.id.btn_release)
    Button btnRelease;
    @BindView(R.id.btn_discover)
    Button btnDiscover;
    @BindView(R.id.btn_my_page)
    Button btnMyPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrip_saying);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_release, R.id.btn_discover, R.id.btn_my_page})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_release:
                startActivity(new Intent(this, ReleaseActivity.class));
                break;
            case R.id.btn_discover:
                startActivity(new Intent(this, DiscoverActivity.class));
                break;
            case R.id.btn_my_page:
                startActivity(new Intent(this, MeActivity.class));
                break;
        }
    }
}
