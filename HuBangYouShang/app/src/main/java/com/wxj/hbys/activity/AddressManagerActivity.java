package com.wxj.hbys.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wxj.hbys.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wuxiaojun on 2017/1/8.
 * 地址管理
 */

public class AddressManagerActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.btn_add_address)
    Button btn_add_address;
    @BindView(R.id.tv_empty_view)
    TextView tv_empty_view;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;// item_address_manager


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manager);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_add_address})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_add_address:
                startActivity(new Intent(AddressManagerActivity.this,AddAddressActivity.class));

                break;
        }
    }


}
