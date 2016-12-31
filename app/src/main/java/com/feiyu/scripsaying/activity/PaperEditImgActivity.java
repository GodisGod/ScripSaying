package com.feiyu.scripsaying.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.feiyu.scripsaying.R;
import com.feiyu.scripsaying.constant.GlobalConstant;
import com.feiyu.scripsaying.view.GlideCircleTransform;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by YueDong on 2016/12/27.
 */
public class PaperEditImgActivity extends BaseActivity {

    @BindView(R.id.edit_img_usericon)
    ImageView userIcon;
    @BindView(R.id.edit_img_scrip_tag)
    ImageView scripTag;
    @BindView(R.id.edit_img_content)
    ImageView scripImgContent;
    @BindView(R.id.edit_img_scrip_audio)
    ImageView scripAudio;
    @BindView(R.id.edit_text_content)
    EditText scripTextContent;
    @BindView(R.id.edit_send)
    ImageView scripSend;

    private Context ctx;
    private String chooseTag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_edit);
        ButterKnife.bind(this);
        ctx = this;
        initView();

    }
    @OnClick({R.id.edit_img_scrip_tag, R.id.edit_img_scrip_audio,R.id.edit_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_img_scrip_tag:
                Intent intent = new Intent(ctx,ChooseTagActivity.class);
                startActivityForResult(intent, GlobalConstant.CHOOSE_TAG);
                break;
            case R.id.edit_img_scrip_audio:
                //todo 录音

                break;
            case R.id.edit_send:
                //todo 发布

                break;
        }
    }


    public void initView() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GlobalConstant.CHOOSE_TAG){
            if (resultCode==RESULT_OK){
                chooseTag = data.getStringExtra(GlobalConstant.CHOOSE_TAG_KEY);
                Glide.with(ctx).load(chooseTag)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.mipmap.ic_launcher)
                        .transform(new GlideCircleTransform(ctx))
                        .into(scripTag);
            }
        }
    }
}
