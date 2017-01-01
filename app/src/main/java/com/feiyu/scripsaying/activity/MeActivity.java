package com.feiyu.scripsaying.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.feiyu.scripsaying.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeActivity extends AppCompatActivity {

    @BindView(R.id.img_my_head_Me)
    ImageView btnUserHead;
    @BindView(R.id.tv_my_name_Me)
    TextView tvUserName;
    @BindView(R.id.tv_my_sign_Me)
    TextView tvUserSign;
    @BindView(R.id.tv_my_id_Me)
    TextView tvUserID;
    @BindView(R.id.tv_my_gender)
    TextView tvUserGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        ButterKnife.bind(this);
    }
}
