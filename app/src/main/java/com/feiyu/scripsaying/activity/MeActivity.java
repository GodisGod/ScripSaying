package com.feiyu.scripsaying.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.feiyu.scripsaying.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeActivity extends AppCompatActivity {

    @BindView(R.id.line_my_head_me)
    LinearLayout imgMyHead;
    @BindView(R.id.line_my_name_me)
    LinearLayout tvMyName;
    @BindView(R.id.line_my_sign_me)
    LinearLayout tvMySign;
    @BindView(R.id.line_my_id_me)
    LinearLayout tvMyId;
    @BindView(R.id.line_my_gender)
    LinearLayout tvMyGender;
    @BindView(R.id.line_my_send_scrip_me)
    LinearLayout lineMySendScrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.line_my_head_me, R.id.line_my_name_me, R.id.line_my_sign_me, R.id.line_my_id_me, R.id.line_my_gender, R.id.line_my_send_scrip_me})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.line_my_head_me:
                //打开相册
                break;
            case R.id.line_my_name_me:
                //开启相机

                break;
            case R.id.line_my_sign_me:

                break;
            case R.id.line_my_id_me:

                break;
            case R.id.line_my_gender:

                break;
            case R.id.line_my_send_scrip_me:

                break;


        }
    }

}
