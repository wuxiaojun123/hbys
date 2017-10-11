package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.HelpFilterCityAdapter;
import com.help.reward.adapter.HelpFilterTypeAdapter;
import com.help.reward.bean.AreaBean;
import com.help.reward.bean.HelpBoardBean;
import com.help.reward.bean.Response.AreaResponse;
import com.help.reward.bean.Response.HelpBoardResponse;
import com.help.reward.network.HelpNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.view.MyGridView;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 互帮-筛选
 * Created by MXY on 2017/2/19.
 */

public class HelpFilterActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;
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
    ArrayList<AreaBean> cityList = new ArrayList<>();
    ArrayList<HelpBoardBean> boardList = new ArrayList<>();
    String area_id, area_name;
    String board_id, board_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        AreaBean a = new AreaBean();
        a.area_name = "全国";
        a.area_id = "";
        cityList.add(a);
        HelpBoardBean h = new HelpBoardBean();
        h.board_name = "全部";
        h.id = "";
        boardList.add(h);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            area_id = bundle.getString("area_id", "");
            area_name = bundle.getString("area_name", "");
            board_id = bundle.getString("board_id", "");
            board_name = bundle.getString("board_name", "");
            tvFilterType1.setText(area_name);
            tvFilterType2.setText(board_name);
        }
        getAreaData();
        getBoardData();
        initData();
    }


    private void initData() {
        cityAdapter = new HelpFilterCityAdapter(mContext, cityList, R.layout.item_filter);
        gvFilterCity.setAdapter(cityAdapter);
        typeAdapter = new HelpFilterTypeAdapter(mContext, boardList, R.layout.item_filter);
        gvFilterType.setAdapter(typeAdapter);
    }

    @OnClick({R.id.iv_title_back, R.id.tv_title_right})
    void click(View v) {
        switch (v.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_right:
                Intent intent = new Intent();
                intent.putExtra("area_id", area_id);
                intent.putExtra("area_name", area_name);
                intent.putExtra("board_id", board_id);
                intent.putExtra("board_name", board_name);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @OnItemClick(R.id.gv_filter_city)
    void itemClick(int position) {
        tvFilterType1.setText(cityList.get(position).area_name);
        area_id = cityList.get(position).area_id;
        area_name=cityList.get(position).area_name;
    }

    @OnItemClick(R.id.gv_filter_type)
    void itemClick_type(int position) {
        tvFilterType2.setText(boardList.get(position).board_name);
        board_id = boardList.get(position).id;
        board_name = boardList.get(position).board_name;
    }


    /**
     * 获取地址
     */
    private void getAreaData() {

        MyProcessDialog.showDialog(mContext);
        HelpNetwork
                .getHelpApi()
                .getAreaBean(App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<AreaResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(AreaResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            cityList.addAll(response.data.area_list);
                            cityAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    /**
     * 获取分类
     */
    private void getBoardData() {

        MyProcessDialog.showDialog(mContext);
        HelpNetwork
                .getHelpApi()
                .getBoardBean(App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<HelpBoardResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(HelpBoardResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            boardList.addAll(response.data.board_list);
                            typeAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }
}
