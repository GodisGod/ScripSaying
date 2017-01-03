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

public class EditNameActivity extends AppCompatActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.btn_confirm_name)
    Button btnConfirm;

    private String userId;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);
        ButterKnife.bind(this);
        if (ScripContext.getInstance() != null) {
            userId = ScripContext.getInstance().getSharedPreferences().getString(GlobalConstant.CURRENT_ID, "default");
            userName = ScripContext.getInstance().getSharedPreferences().getString(userId + GlobalConstant.USER_NAME, "");
            etName.setText(userName);
        }
    }

    @OnClick({R.id.btn_confirm_name})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm_name:
                if (etName.getText().toString().isEmpty()) {
                    HD.TLOG("昵称不能为空");
                    return;
                }
                //修改昵称
                updateName();
                break;

        }
    }

    private void updateName() {
        final UserInfo u2 = new UserInfo();
        u2.setUserName(etName.getText().toString());
        u2.update(userId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                HD.TLOG(userId+"用户名更新成功"+etName.getText().toString());
                SharedPreferences.Editor edit = ScripContext.getInstance().getSharedPreferences().edit();
                edit.putString(userId + GlobalConstant.USER_NAME, etName.getText().toString());
                edit.apply();
                finish();
            }
        });
    }
}
