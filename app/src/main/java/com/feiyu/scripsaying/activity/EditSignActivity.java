package com.feiyu.scripsaying.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class EditSignActivity extends AppCompatActivity {

    @BindView(R.id.et_sign)
    EditText etSign;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    private String userId;
    private String userSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sign);
        ButterKnife.bind(this);
        if (ScripContext.getInstance() != null) {
            userId = ScripContext.getInstance().getSharedPreferences().getString(GlobalConstant.CURRENT_ID, "default");
            userSign = ScripContext.getInstance().getSharedPreferences().getString(userId + GlobalConstant.USER_SIGN, "暂无签名");
            etSign.setText(userSign);
        }
    }

    @OnClick({R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                //修改签名
                updateSign();
                break;

        }
    }

    private void updateSign() {
        final UserInfo u2 = new UserInfo();
        u2.setSign(etSign.getText().toString());//不用判断是不是为空，为空就是没有签名或清除了签名
        u2.update(userId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                HD.TLOG("用户头像更新成功");
                SharedPreferences.Editor edit = ScripContext.getInstance().getSharedPreferences().edit();
                edit.putString(userId + GlobalConstant.USER_SIGN, etSign.getText().toString());
                edit.apply();
                finish();
            }
        });
    }

}
