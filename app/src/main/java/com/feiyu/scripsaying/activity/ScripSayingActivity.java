package com.feiyu.scripsaying.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.feiyu.scripsaying.R;
import com.feiyu.scripsaying.util.HD;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;

public class ScripSayingActivity extends BaseActivity {

    @BindView(R.id.btn_release)
    Button btnRelease;
    @BindView(R.id.btn_discover)
    Button btnDiscover;
    @BindView(R.id.btn_my_page)
    Button btnMyPage;
    @BindView(R.id.btn_test)
    Button btnTest;
    @BindView(R.id.btn_conversation_list)
    Button btnConversationList;

    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrip_saying);
        ButterKnife.bind(this);
        ctx = this;
    }

    @OnClick({R.id.btn_release, R.id.btn_discover, R.id.btn_my_page, R.id.btn_test, R.id.btn_conversation_list})
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
                finish();
                break;
            case R.id.btn_test:
                startActivity(new Intent(this, CustomMarkerActivity.class));
                break;
            case R.id.btn_conversation_list:
                //我的消息列表
                if (RongIM.getInstance() != null) {
                    HD.LOG("jump_conversation_list");
                    RongIM.getInstance().startConversationList(ctx, null);
                }
                break;
        }
    }
}
