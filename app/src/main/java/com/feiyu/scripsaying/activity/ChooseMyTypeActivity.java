package com.feiyu.scripsaying.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feiyu.scripsaying.R;
import com.feiyu.scripsaying.bean.UserInfo;
import com.feiyu.scripsaying.constant.GlobalConstant;
import com.feiyu.scripsaying.util.HD;
import com.feiyu.scripsaying.util.ScripContext;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ChooseMyTypeActivity extends AppCompatActivity {

    @BindView(R.id.line_female)
    LinearLayout lineFemail;
    @BindView(R.id.line_male)
    LinearLayout lineMale;

    @BindView(R.id.girl_luoli)
    ImageView girlLuoli;
    @BindView(R.id.girl_yujie)
    ImageView girlYujie;
    @BindView(R.id.girl_shunv)
    ImageView girlShunv;

    @BindView(R.id.tv_girl_luoli)
    TextView tvGirlLuoli;
    @BindView(R.id.tv_girl_yujie)
    TextView tvGirlYujie;
    @BindView(R.id.tv_girl_shunv)
    TextView tvGirlShunv;

    @BindView(R.id.boy_zhengtai)
    ImageView boyZhengTai;
    @BindView(R.id.boy_shaonian)
    ImageView boyShaonian;
    @BindView(R.id.boy_chengshu)
    ImageView boyChengshu;
    @BindView(R.id.tv_boy_zhengtai)

    TextView tvBoyZhengTai;
    @BindView(R.id.tv_boy_shaonian)
    TextView tvBoyShaonian;
    @BindView(R.id.tv_boy_chengshu)
    TextView tvBoyChengShu;
    private String userId;
    private String userType;
    private String userGender;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_my_type);
        ButterKnife.bind(this);
        if (ScripContext.getInstance() != null) {
            mPreferences = ScripContext.getInstance().getSharedPreferences();
            edit = mPreferences.edit();
            userId = mPreferences.getString(GlobalConstant.CURRENT_ID, "default");
            userType = mPreferences.getString(userId + GlobalConstant.USER_TYPE, "default");
            userGender = mPreferences.getString(userId + GlobalConstant.USER_GENDER, "性别未知");
            //游戏一样的角色选择器？
            if (userGender.equals("f")) {
                boyZhengTai.setVisibility(View.GONE);
                boyShaonian.setVisibility(View.GONE);
                boyChengshu.setVisibility(View.GONE);
                tvBoyZhengTai.setVisibility(View.GONE);
                tvBoyShaonian.setVisibility(View.GONE);
                tvBoyChengShu.setVisibility(View.GONE);
                girlLuoli.setVisibility(View.VISIBLE);
                girlYujie.setVisibility(View.VISIBLE);
                girlShunv.setVisibility(View.VISIBLE);
            } else if (userGender.equals("m")) {
                boyZhengTai.setVisibility(View.VISIBLE);
                boyShaonian.setVisibility(View.VISIBLE);
                boyChengshu.setVisibility(View.VISIBLE);
                tvBoyZhengTai.setVisibility(View.VISIBLE);
                tvBoyShaonian.setVisibility(View.VISIBLE);
                tvBoyChengShu.setVisibility(View.VISIBLE);
                girlLuoli.setVisibility(View.GONE);
                girlYujie.setVisibility(View.GONE);
                girlShunv.setVisibility(View.GONE);
            }
        }
    }


    @OnClick({R.id.girl_luoli, R.id.girl_yujie, R.id.girl_shunv, R.id.boy_zhengtai, R.id.boy_shaonian, R.id.boy_chengshu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.girl_luoli:
                edit.putString(userId + GlobalConstant.USER_TYPE, GlobalConstant.GIRL_LUOLI);
                edit.apply();
                UserInfo userInfo = new UserInfo();
                userInfo.setUserType(GlobalConstant.GIRL_LUOLI);
                userInfo.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        HD.TLOG("修改成功");
                        finish();
                    }
                });
                break;
            case R.id.girl_yujie:
                edit.putString(userId + GlobalConstant.USER_TYPE, GlobalConstant.GIRL_YUJIE);
                edit.apply();
                UserInfo userInfo2 = new UserInfo();
                userInfo2.setUserType(GlobalConstant.GIRL_YUJIE);
                userInfo2.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        HD.TLOG("修改成功");
                        finish();
                    }
                });
                break;
            case R.id.girl_shunv:
                edit.putString(userId + GlobalConstant.USER_TYPE, GlobalConstant.GIRL_SHUNV);
                edit.apply();
                UserInfo userInfo3 = new UserInfo();
                userInfo3.setUserType(GlobalConstant.GIRL_SHUNV);
                userInfo3.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        HD.TLOG("修改成功");
                        finish();
                    }
                });
                break;
            case R.id.boy_zhengtai:
                edit.putString(userId + GlobalConstant.USER_TYPE, GlobalConstant.BOY_ZHENGTAI);
                edit.apply();
                UserInfo userInfo4 = new UserInfo();
                userInfo4.setUserType(GlobalConstant.BOY_ZHENGTAI);
                userInfo4.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        HD.TLOG("修改成功");
                        finish();
                    }
                });
                break;
            case R.id.boy_shaonian:
                edit.putString(userId + GlobalConstant.USER_TYPE, GlobalConstant.BOY_SHAONIAN);
                edit.apply();
                UserInfo userInfo5 = new UserInfo();
                userInfo5.setUserType(GlobalConstant.BOY_SHAONIAN);
                userInfo5.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        HD.TLOG("修改成功");
                        finish();
                    }
                });
                break;
            case R.id.boy_chengshu:
                edit.putString(userId + GlobalConstant.USER_TYPE, GlobalConstant.BOY_CHENGSHU);
                edit.apply();
                UserInfo userInfo6 = new UserInfo();
                userInfo6.setUserType(GlobalConstant.BOY_CHENGSHU);
                userInfo6.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        HD.TLOG("修改成功");
                        finish();
                    }
                });
                break;
        }
    }
}
