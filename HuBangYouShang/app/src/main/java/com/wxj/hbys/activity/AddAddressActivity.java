package com.wxj.hbys.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.wxj.hbys.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuxiaojun on 2017/1/8.
 */

public class AddAddressActivity extends BaseActivity {

    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_phone_number)
    EditText et_phone_number;
    @BindView(R.id.et_area)
    EditText et_area;
    @BindView(R.id.et_address)
    EditText et_address;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);


    }

}
