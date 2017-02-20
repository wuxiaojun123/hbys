package com.wxj.hbys.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxj.hbys.R;
import com.wxj.hbys.adapter.HelpFilterCityAdapter;
import com.wxj.hbys.adapter.HelpFilterTypeAdapter;
import com.wxj.hbys.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * 互帮-筛选
 * Created by MXY on 2017/2/19.
 */

public class HelpFilterActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_filter_type1)
    TextView tvFilterType1;
    @BindView(R.id.tv_filter_type2)
    TextView tvFilterType2;
    @BindView(R.id.gv_filter_city)
    MyGridView gvFilterCity;
    @BindView(R.id.gv_filter_type)
    MyGridView gvFilterType;

    private HelpFilterCityAdapter cityAdapter;
    private HelpFilterTypeAdapter typeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        initData();
    }

    private List<String> list;
    private List<String> listtype;

    private void initData() {
        list = new ArrayList<String>();
        for (int i = 0; i < 8; i++) {
            list.add("北京" + i);
        }
        cityAdapter = new HelpFilterCityAdapter(mContext, list, R.layout.item_filter);
        gvFilterCity.setAdapter(cityAdapter);

        listtype = new ArrayList<String>();
        for (int i = 0; i < 8; i++) {
            listtype.add("类别" + i);
        }
        typeAdapter = new HelpFilterTypeAdapter(mContext, listtype, R.layout.item_filter);
        gvFilterType.setAdapter(typeAdapter);
    }

    @OnClick({R.id.iv_title_back})
    void click(View v) {
        switch (v.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }
    }

    @OnItemClick(R.id.gv_filter_city)
    void itemClick(int position) {
        tvFilterType1.setText(list.get(position));
    }

    @OnItemClick(R.id.gv_filter_type)
    void itemClick_type(int position){
        tvFilterType2.setText(listtype.get(position));
    }
}
