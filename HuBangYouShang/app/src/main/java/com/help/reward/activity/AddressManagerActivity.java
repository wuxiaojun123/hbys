package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.utils.ActivitySlideAnim;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wuxiaojun on 2017/1/8.
 * 地址管理
 */

public class AddressManagerActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

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
        initView();
    }

    private void initView() {
        tv_title.setText(R.string.string_address_manager_title);
        tv_title_right.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_title_back,R.id.btn_add_address})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(AddressManagerActivity.this);

                break;
            case R.id.btn_add_address:
                startActivity(new Intent(AddressManagerActivity.this,AddAddressActivity.class));
                ActivitySlideAnim.slideInAnim(AddressManagerActivity.this);

                break;
        }
    }


}
