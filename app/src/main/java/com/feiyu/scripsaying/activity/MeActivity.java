package com.feiyu.scripsaying.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feiyu.scripsaying.R;
import com.feiyu.scripsaying.constant.GlobalConstant;
import com.feiyu.scripsaying.util.ScripContext;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeActivity extends AppCompatActivity {

    //子布局控件初始化
    @BindView(R.id.line_my_head_me)
    LinearLayout lineMyHead;
    @BindView(R.id.line_my_name_me)
    LinearLayout lineMyName;
    @BindView(R.id.line_my_sign_me)
    LinearLayout lineMySign;
    @BindView(R.id.line_my_id_me)
    LinearLayout lineMyId;
    @BindView(R.id.line_my_gender)
    LinearLayout lineMyGender;
    @BindView(R.id.line_my_send_scrip_me)
    LinearLayout lineMySendScrip;
    //具体控件初始化
    @BindView(R.id.img_my_head_me)
    ImageView imgMyHead;
    @BindView(R.id.tv_my_name_me)
    TextView tvMyName;
    @BindView(R.id.tv_my_sign_me)
    TextView tvMySign;
    @BindView(R.id.tv_my_id_me)
    TextView tvMyId;
    @BindView(R.id.tv_my_gender_me)
    TextView tvMyGender;

    private String userId;
    private String userName;
    private String userSign;
    private String userGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        ButterKnife.bind(this);

        if (ScripContext.getInstance() != null) {
            userId = ScripContext.getInstance().getSharedPreferences().getString(GlobalConstant.CURRENT_ID, "default");
            userName = ScripContext.getInstance().getSharedPreferences().getString(userId + GlobalConstant.USER_NAME, "default");
            userSign = ScripContext.getInstance().getSharedPreferences().getString(userId + GlobalConstant.USER_SIGN, "暂无签名");
            userGender = ScripContext.getInstance().getSharedPreferences().getString(userId + GlobalConstant.USER_GENDER, "性别未知");

            tvMyId.setText(userId);
            tvMyName.setText(userName);
            tvMySign.setText(userSign);
            tvMyGender.setText(userGender);
        }

    }

    @OnClick({R.id.line_my_head_me, R.id.line_my_name_me, R.id.line_my_sign_me, R.id.line_my_gender, R.id.line_my_send_scrip_me})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.line_my_head_me:
                //修改头像

                break;
            case R.id.line_my_name_me:
                //修改昵称

                break;
            case R.id.line_my_sign_me:
                //修改签名

                break;
            case R.id.line_my_gender:
                //修改性别

                break;
            case R.id.line_my_send_scrip_me:
                //地图展示我发送的纸片

                break;


        }
    }

}
