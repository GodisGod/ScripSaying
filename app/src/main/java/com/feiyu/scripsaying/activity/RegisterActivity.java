package com.feiyu.scripsaying.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.feiyu.scripsaying.R;
import com.feiyu.scripsaying.bean.UserInfo;
import com.feiyu.scripsaying.callback.getTokenCallback;
import com.feiyu.scripsaying.constant.GlobalConstant;
import com.feiyu.scripsaying.util.CloudUtil;
import com.feiyu.scripsaying.util.HD;
import com.feiyu.scripsaying.util.ScripContext;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class RegisterActivity extends BaseActivity {

    //控件初始化
    private EditText etUserName;
    private EditText etUserPassword;
    private CheckBox chShowPassword;
    private TextView tvWrongTip;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button btnRegister;

    //变量声明
    private String regName;
    private String regPassword;
    private CloudUtil cloudUtil;
    private Context ctx;
    private String gender = "m";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ctx = this;
        initView();
    }

    private void initView() {

        etUserName = (EditText) findViewById(R.id.reg_et_name);
        etUserPassword = (EditText) findViewById(R.id.reg_et_password);
        chShowPassword = (CheckBox) findViewById(R.id.reg_check_show_password);
        tvWrongTip = (TextView) findViewById(R.id.reg_wrong_tip);
        btnRegister = (Button) findViewById(R.id.reg_btn_register);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                if (radioButton.getId() == R.id.rb_male) {
                    gender = "m";
                } else if (radioButton.getId() == R.id.rb_female) {
                    gender = "f";
                } else {
                    gender = "no";
                }
            }
        });
        chShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    //显示密码
                    etUserPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    //第二种方法
//                    etUserPassword.setTransformationMethod(HideReturnsTransformationMethod
//                            .getInstance());
                    //切换成眼睛图标
                    chShowPassword.setBackgroundResource(R.mipmap.show_password);
                } else {
                    //隐藏密码
                    //第一种
                    etUserPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    //第二种
//                    etUserPassword.setTransformationMethod(PasswordTransformationMethod
//                            .getInstance());
                    //切换成闭眼图标
                    chShowPassword.setBackgroundResource(R.mipmap.hide_password);
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regName = etUserName.getText().toString();
                regPassword = etUserPassword.getText().toString();
                if (regName.isEmpty() || regPassword.isEmpty()) {
                    tvWrongTip.setText("用户名或密码不能为空");
                    return;
                }
                UserInfo userInfo = new UserInfo();
                userInfo.setUserName(regName);
                userInfo.setUserPassword(regPassword);
                userInfo.setUserGender(gender);
                userInfo.setUserIcon(GlobalConstant.DEFAULT_USER_ICON_URL);
                userInfo.setUserType("no");
                userInfo.save(new SaveListener<String>() {
                    @Override
                    public void done(final String objectId, BmobException e) {
                        if (e == null) {
                            HD.TLOG("用户名密码上传成功");
                            //1、获取Token
                            //注册回调
                            cloudUtil = new CloudUtil(new getTokenCallback() {
                                @Override
                                public void finishToken(final String token) {
                                    HD.TLOG("获取Token成功");
                                    //2、上传用户ID
                                    final UserInfo u2 = new UserInfo();
                                    u2.setUserId(objectId);
                                    u2.setToken(token);
                                    u2.update(objectId, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                HD.TLOG("用户ID上传成功");
                                                HD.TLOG("用户信息更新成功" + u2.getUpdatedAt());
                                                //3、连接融云
                                                RongIM.connect(token, new RongIMClient.ConnectCallback() {

                                                    /**
                                                     * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                                                     *                  2.  token 对应的融云的 appKey 和工程里设置的 appKey 是否一致
                                                     */
                                                    @Override
                                                    public void onTokenIncorrect() {
                                                        HD.TLOG("--onTokenIncorrect");
                                                    }

                                                    /**
                                                     * 连接融云成功
                                                     * @param userid 当前 token 对应的用户 id
                                                     */
                                                    @Override
                                                    public void onSuccess(String userid) {
                                                        HD.TLOG("--onSuccess" + userid);
                                                        //4、保存用户信息到本地
                                                        SharedPreferences.Editor edit = ScripContext.getInstance().getSharedPreferences().edit();
                                                        edit.putString(GlobalConstant.CURRENT_ID, userid);
                                                        edit.putString(objectId + GlobalConstant.USER_ID, objectId);
                                                        edit.putString(objectId + GlobalConstant.USER_NAME, regName);
                                                        edit.putString(objectId + GlobalConstant.USER_GENDER, gender);
                                                        edit.putString(objectId + GlobalConstant.USER_ICON,"1");
                                                        edit.putString(objectId + GlobalConstant.USER_PASSWORD, regPassword);
                                                        edit.putString(objectId + GlobalConstant.USER_TOKEN, token);
                                                        edit.putString(objectId + GlobalConstant.DEFAULT_TOKEN, token);
                                                        edit.apply();
                                                        startActivity(new Intent(ctx, ScripSayingActivity.class));
                                                        finish();
                                                    }

                                                    /**
                                                     * 连接融云失败
                                                     * @param errorCode 错误码，可到官网 查看错误码对应的注释
                                                     */
                                                    @Override
                                                    public void onError(RongIMClient.ErrorCode errorCode) {
                                                        Log.i("RegisterActivity", "onError: " + errorCode);
                                                    }
                                                });
                                            } else {
                                                Log.i("LHD", "更新失败" + e.getMessage());
                                            }
                                        }
                                    });

                                }

                                @Override
                                public void error(Exception e) {
                                    HD.LOG("RegisterActivity error : " + e.getMessage());
                                }
                            });
                            //发起云端逻辑请求
                            cloudUtil.getTokenFromCloud(objectId, regName);

                        } else {
                            HD.TOS("网络异常,注册失败，请稍后重试");
                        }
                    }
                });
            }
        });
    }


}
