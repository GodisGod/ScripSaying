package com.feiyu.scripsaying.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.feiyu.scripsaying.R;
import com.feiyu.scripsaying.bean.UserInfo;
import com.feiyu.scripsaying.callback.getTokenCallback;
import com.feiyu.scripsaying.constant.GlobalConstant;
import com.feiyu.scripsaying.util.CloudUtil;
import com.feiyu.scripsaying.util.HD;
import com.feiyu.scripsaying.util.ScripContext;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUserAccount;
    private EditText etUserPassword;
    private CheckBox chShowPassword;
    private TextView tvWrongTip;
    private Button btnLogin;
    private Button btnRegister;


    private Context ctx;
    private String userAccount;
    private String userPassword;
    private String userToken;
    private String userName;
    private CloudUtil cloudUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        autoLogin();
        initView();

    }

    //自动登录逻辑
    private void autoLogin() {
        if (ScripContext.getInstance() != null) {
            userAccount = ScripContext.getInstance().getSharedPreferences().getString(GlobalConstant.CURRENT_ID, "default");
            userPassword = ScripContext.getInstance().getSharedPreferences().getString(userAccount+GlobalConstant.USER_PASSWORD, "default");
            userToken = ScripContext.getInstance().getSharedPreferences().getString(userAccount+GlobalConstant.USER_TOKEN, "default");
            //查看本地文件，获取ID PASSWORD TOKEN
            if (userAccount.equals("default") || userPassword.equals("default") || userToken.equals("default")) {
                //不存在直接退出方法
                return;
            } else {
                //存在就根据ID到Bmob查询密码和Token
                BmobQuery<UserInfo> query = new BmobQuery<UserInfo>("UserInfo");
                query.addWhereEqualTo("userID", userAccount);
                query.findObjects(new FindListener<UserInfo>() {
                    @Override
                    public void done(List<UserInfo> list, BmobException e) {
                        if (e == null) {
                            final UserInfo userInfo = list.get(0);
                            userName = userInfo.getUserName();
                            HD.TOS("存在用户： " + userInfo.getUserName() + "  " + userInfo.getUserPassword());
                            //如果和服务器保存的Token一致就使用本地Token连接融云
                            if (userPassword.equals(userInfo.getUserPassword()) && userToken.equals(userInfo.getToken())) {
                                //用这个Token连接融云，如果连接失败就重新申请Token再登录
                                //4、连接融云
                                RongIM.connect(userToken, new RongIMClient.ConnectCallback() {
                                    @Override
                                    public void onTokenIncorrect() {
                                        HD.TLOG("--onTokenIncorrect");
                                        //连接失败说明token过期，需要用户再次登录，所以这里就直接退出方法
                                        //需要帮用户填写账号密码
                                        etUserAccount.setText(userAccount);
                                        etUserPassword.setText(userPassword);
                                        return;
                                    }

                                    @Override
                                    public void onSuccess(String userid) {
                                        HD.TLOG("--onSuccess" + userid);
                                        SharedPreferences.Editor edit = ScripContext.getInstance().getSharedPreferences().edit();
                                        edit.putString(GlobalConstant.CURRENT_ID, userid);
                                        edit.putString(userid+GlobalConstant.USER_GENDER, userInfo.getUserGender());
                                        edit.putString(userid+GlobalConstant.USER_ICON, userInfo.getUserIcon());
                                        edit.putString(userid+GlobalConstant.USER_TYPE, userInfo.getUserType());
                                        edit.putString(userid+GlobalConstant.USER_ID, userid);
                                        edit.putString(userid+GlobalConstant.USER_NAME, userInfo.getUserName());
                                        edit.putString(userid+GlobalConstant.USER_SIGN, userInfo.getSign());
                                        edit.putString(userid+GlobalConstant.USER_SIGN, userInfo.getSign());
                                        edit.putString(userid+GlobalConstant.USER_PASSWORD, userPassword);
                                        edit.putString(userid+GlobalConstant.USER_TOKEN, userToken);
                                        edit.putString(userid+GlobalConstant.DEFAULT_TOKEN, userToken);
                                        edit.apply();
                                        startActivity(new Intent(ctx, ScripSayingActivity.class));
                                        finish();
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {
                                        HD.TLOG("onError: " + errorCode);
                                    }
                                });
                            }
                            //不相等就直接退出方法,可能是用户改变了密码，所以要帮用户填写账号和旧密码
                            etUserAccount.setText(userAccount);
                            etUserPassword.setText(userPassword);
                            //退出方法
                            return;
                        } else {
                            //服务器不存在这个ID
                            HD.TLOG("账号不存在，请先注册。");
                            return;
                        }
                    }
                });
            }
        }
    }

    public void initView() {
        setContentView(R.layout.activity_login);
        ctx = this;
        etUserAccount = (EditText) findViewById(R.id.login_et_account);
        etUserPassword = (EditText) findViewById(R.id.login_et_password);
        chShowPassword = (CheckBox) findViewById(R.id.login_check_show_password);
        tvWrongTip = (TextView) findViewById(R.id.login_wrong_tip);
        btnLogin = (Button) findViewById(R.id.login_btn_login);
        btnRegister = (Button) findViewById(R.id.login_btn_register);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        chShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //显示密码
                    etUserPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    //切换成眼睛图标
                    chShowPassword.setBackgroundResource(R.mipmap.show_password);
                } else {
                    //隐藏密码
                    //第一种
                    etUserPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    //切换成闭眼图标
                    chShowPassword.setBackgroundResource(R.mipmap.hide_password);
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn_login:
                //1、如果本地有ID PASSWORD TOKEN就尝试和服务器的校准
                //1.1校准成功就直接登录
                //1.2校准失败就让用户重新登录,用户ID要自定填写到页面上
                //2、如果本地没有用户信息就进入登录界面
                userAccount = etUserAccount.getText().toString();
                userPassword = etUserPassword.getText().toString();
                if (userAccount.isEmpty() || userPassword.isEmpty()) {
                    tvWrongTip.setText("用户名或密码不能为空");
                    return;
                }
                //验证
                BmobQuery<UserInfo> query = new BmobQuery<UserInfo>("UserInfo");
                query.addWhereEqualTo("userId", userAccount);
                query.findObjects(new FindListener<UserInfo>() {
                    @Override
                    public void done(List<UserInfo> list, BmobException e) {
                        if (e == null) {
                            final UserInfo userInfo = list.get(0);
                            HD.TOS("登录存在用户： " + userInfo.getUserName() + "  " + userInfo.getUserPassword());
                            if (userPassword.equals(userInfo.getUserPassword())) {
                                //获取用户信息并保存
                                userToken = userInfo.getToken();
                                //连接融云
                                //4、连接融云
                                RongIM.connect(userToken, new RongIMClient.ConnectCallback() {
                                    @Override
                                    public void onTokenIncorrect() {
                                        HD.TLOG("--onTokenIncorrect");
                                        //连接失败说明token过期，需要申请新的token
                                        cloudUtil = new CloudUtil(new getTokenCallback() {
                                            @Override
                                            public void finishToken(final String token) {
                                                //获取新的Token,更新Bmob用户表，更新完再次连接融云
                                                HD.TLOG("获取Token成功");
                                                //2、上传用户新Token
                                                final UserInfo u2 = new UserInfo();
                                                u2.setToken(token);
                                                //userAccount=objectId 所以直接用userAccount就可以
                                                u2.update(userAccount, new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        if (e == null) {
                                                            HD.TLOG("用户新Token上传成功");
                                                            HD.TLOG("用户信息更新成功" + u2.getUpdatedAt());

                                                            //4、再次连接融云
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
                                                                    //3、保存用户信息到本地
                                                                    SharedPreferences.Editor edit = ScripContext.getInstance().getSharedPreferences().edit();
                                                                    edit.putString(userid+GlobalConstant.USER_TOKEN, token);
                                                                    edit.putString(userid+GlobalConstant.DEFAULT_TOKEN, token);
                                                                    edit.putString(GlobalConstant.CURRENT_ID, userid);
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
                                                                    HD.LOG("onError: " + errorCode);
                                                                    HD.TLOG("当前用户过多，请耐心等待(*^__^*)");
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
                                                HD.TLOG("当前用户过多，请耐心等待(*^__^*)");
                                            }
                                        });
                                        cloudUtil.getTokenFromCloud(userAccount, userName);

                                        return;
                                    }

                                    @Override
                                    public void onSuccess(String userid) {
                                        HD.TLOG("--onSuccess" + userid);
                                        SharedPreferences.Editor edit = ScripContext.getInstance().getSharedPreferences().edit();
                                        edit.putString(GlobalConstant.CURRENT_ID, userid);
                                        edit.putString(userid+GlobalConstant.USER_GENDER, userInfo.getUserGender());
                                        edit.putString(userid+GlobalConstant.USER_ICON, userInfo.getUserIcon());
                                        edit.putString(userid+GlobalConstant.USER_TYPE, userInfo.getUserType());
                                        edit.putString(userid+GlobalConstant.USER_ID, userid);
                                        edit.putString(userid+GlobalConstant.USER_NAME, userInfo.getUserName());
                                        edit.putString(userid+GlobalConstant.USER_SIGN, userInfo.getSign());
                                        edit.putString(userid+GlobalConstant.USER_PASSWORD, userPassword);
                                        edit.putString(userid+GlobalConstant.USER_TOKEN, userToken);
                                        edit.putString(userid+GlobalConstant.DEFAULT_TOKEN, userToken);
                                        edit.apply();
                                        startActivity(new Intent(ctx, ScripSayingActivity.class));
                                        finish();
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {
                                        HD.TLOG("onError: " + errorCode);
                                    }
                                });
                            }
                            return;
                        } else {
                            HD.TLOG("账号密码不存在，请先注册");
                            return;
                        }
                    }
                });

                break;
            case R.id.login_btn_register:
                startActivity(new Intent(ctx, RegisterActivity.class));
                finish();
                break;
        }
    }
}


