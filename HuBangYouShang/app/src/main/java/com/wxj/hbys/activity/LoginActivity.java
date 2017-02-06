package com.wxj.hbys.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wxj.hbys.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登陆
 * Created by wuxiaojun on 2017/1/6.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.tv_register)
    TextView tv_register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.tv_register})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.tv_register:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

                break;

        }
    }


}
