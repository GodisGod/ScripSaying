package com.feiyu.scripsaying.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.feiyu.scripsaying.R;
import com.feiyu.scripsaying.bean.UserInfo;
import com.feiyu.scripsaying.constant.GlobalConstant;
import com.feiyu.scripsaying.util.HD;
import com.feiyu.scripsaying.util.ScripContext;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class AutoLoginActivity extends AppCompatActivity {

    private String userAccount;
    private String userPassword;
    private String userToken;
    private String userName;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor edit;
    private Context ctx;

    @BindView(R.id.auto_login_img)
    ImageView autoLoginImg;
    private boolean isLoginOut = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_login);
        ButterKnife.bind(this);
        ctx = this;
        Glide.with(ctx).load(R.mipmap.autologin_jieyin)
                .asGif()//这个必须在下面缓存设置的前面
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()//放大图片铺满ImageView控件
                .into(autoLoginImg);

        if (ScripContext.getInstance() != null) {
            sharedPreferences = ScripContext.getInstance().getSharedPreferences();
            edit = ScripContext.getInstance().getSharedPreferences().edit();
        }
        isLoginOut = sharedPreferences.getBoolean(GlobalConstant.LOGIN_OUT, false);
        HD.LOG("自动登录： isLoginOut: "+isLoginOut);
        if (isLoginOut){//如果退出账号就直接进入登录界面而不是自动登录
            startActivity(new Intent(ctx, LoginActivity.class));
            finish();
        }

        userAccount = sharedPreferences.getString(GlobalConstant.CURRENT_ID, "default");
        userPassword = sharedPreferences.getString(userAccount + GlobalConstant.USER_PASSWORD, "default");
        userToken = sharedPreferences.getString(userAccount + GlobalConstant.USER_TOKEN, "default");

        autoLogin();

    }

    //自动登录逻辑
    private void autoLogin() {
        //查看本地文件，获取ID PASSWORD TOKEN
        HD.LOG("userAccount: " + userAccount + "   userPassword : " + userPassword);
        if (userAccount.equals("default") || userPassword.equals("default") || userToken.equals("default")) {
            //不存在直接退出方法
            startActivity(new Intent(ctx, LoginActivity.class));
            finish();
        } else {
            //存在就根据ID到Bmob查询密码和Token
            BmobQuery<UserInfo> query = new BmobQuery<UserInfo>("UserInfo");
            query.addWhereEqualTo("userId", userAccount);
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
                                    startActivity(new Intent(ctx, LoginActivity.class));
                                    finish();
                                    return;
                                }

                                @Override
                                public void onSuccess(String userid) {
                                    HD.TLOG("--onSuccess" + userid);
                                    edit.putString(GlobalConstant.CURRENT_ID, userid);
                                    edit.putString(userid + GlobalConstant.USER_ID, userid);//不需要
                                    edit.putString(userid + GlobalConstant.USER_GENDER, userInfo.getUserGender());
                                    edit.putString(userid + GlobalConstant.USER_ICON, userInfo.getUserIcon());
                                    edit.putString(userid + GlobalConstant.USER_TYPE, userInfo.getUserType());
                                    edit.putString(userid + GlobalConstant.USER_NAME, userInfo.getUserName());
                                    edit.putString(userid + GlobalConstant.USER_SIGN, userInfo.getSign());
                                    edit.putString(userid + GlobalConstant.USER_SIGN, userInfo.getSign());
                                    edit.putString(userid + GlobalConstant.USER_PASSWORD, userPassword);
                                    edit.putString(userid + GlobalConstant.USER_TOKEN, userToken);
                                    edit.putString(userid + GlobalConstant.DEFAULT_TOKEN, userToken);
                                    edit.putBoolean(GlobalConstant.LOGIN_OUT, false);
                                    edit.apply();
                                    startActivity(new Intent(ctx, ScripSayingActivity.class));
                                    finish();
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                    HD.TLOG("onError: " + errorCode);
                                }
                            });
                        } else {
                            //不相等就跳转到登录页面,可能是用户改变了密码，所以要帮用户填写账号和旧密码
                            startActivity(new Intent(ctx, LoginActivity.class));
                            finish();
                        }
                    } else {
                        //服务器不存在这个ID
                        HD.TLOG("账号不存在，请先注册。");
                        startActivity(new Intent(ctx, LoginActivity.class));
                        finish();
                        return;
                    }
                }
            });
        }
    }
}
