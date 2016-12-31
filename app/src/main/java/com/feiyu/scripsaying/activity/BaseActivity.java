package com.feiyu.scripsaying.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by YueDong on 2016/12/27.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    protected abstract void initView();
    protected abstract void initData();
}
