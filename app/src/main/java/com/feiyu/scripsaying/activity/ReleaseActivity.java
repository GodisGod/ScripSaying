package com.feiyu.scripsaying.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.feiyu.scripsaying.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by YueDong on 2016/12/27.
 */
public class ReleaseActivity extends BaseActivity {
    @BindView(R.id.btn_album)
    Button btnAlbum;
    @BindView(R.id.btn_camera)
    Button btnCamera;
    @BindView(R.id.btn_edit_paper)
    Button btnEditPaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        ButterKnife.bind(this);
    }

    public void initView() {

    }

    public void initData() {

    }

    @OnClick({R.id.btn_album, R.id.btn_camera, R.id.btn_edit_paper})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_album:
                break;
            case R.id.btn_camera:
                break;
            case R.id.btn_edit_paper:
                startActivity(new Intent(this,PaperEditImgActivity.class));
                break;
        }
    }
}
