package com.feiyu.scripsaying.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.feiyu.scripsaying.R;
import com.feiyu.scripsaying.constant.GlobalConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseTagActivity extends BaseActivity {

    @BindView(R.id.tag1)
    ImageView tag1;
    @BindView(R.id.tag2)
    ImageView tag2;
    @BindView(R.id.tag3)
    ImageView tag3;
    @BindView(R.id.tag4)
    ImageView tag4;

    private Context ctx;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_tag);
        ButterKnife.bind(this);
        ctx = this;
        intent = new Intent(ctx, PaperEditImgActivity.class);
    }

    @OnClick({R.id.tag1, R.id.tag2, R.id.tag3, R.id.tag4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tag1:
                intent.putExtra(GlobalConstant.CHOOSE_TAG_KEY,1);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.tag2:
                intent.putExtra(GlobalConstant.CHOOSE_TAG_KEY,2);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.tag3:
                intent.putExtra(GlobalConstant.CHOOSE_TAG_KEY,3);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.tag4:
                intent.putExtra(GlobalConstant.CHOOSE_TAG_KEY,4);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
