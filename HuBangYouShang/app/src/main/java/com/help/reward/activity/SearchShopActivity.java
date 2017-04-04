package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.bean.Response.ShopSearchResponse;
import com.help.reward.network.ShopMallNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.view.FluidLayout;
import com.help.reward.view.SearchEditTextView;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 搜索商品页面
 * Created by wuxiaojun on 17-3-31.
 */

public class SearchShopActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.et_search)
    SearchEditTextView et_search; // 搜索
    @BindView(R.id.tv_search_history)
    TextView tv_search_history;
    @BindView(R.id.fl_history)
    FluidLayout fl_history; // 历史记录的viewgroup
    @BindView(R.id.tv_search_hot)
    TextView tv_search_hot;
    @BindView(R.id.fl_hot)
    FluidLayout fl_hot; // 热门搜索的viewgroup

    private int colorTextBg;
    private String[] hotList;
    private String[] historyList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shop);
        ButterKnife.bind(this);
        initView();
        initNetwork();
    }

    private void initNetwork() {
        ShopMallNetwork
                .getShopOtherApi()
                .getShopSearchResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ShopSearchResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(ShopSearchResponse response) {
                        if (response.code == 200) {
                            if (response.data != null) { // 显示数据
                                hotList = response.data.list;
                                historyList = response.data.his_list;
                                setHistory();
                                setHot();
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private void initView() {
        colorTextBg = getResources().getColor(R.color.color_3a);
        et_search.setOnKeyListener(new SearchEditTextView.onKeyListener() {
            @Override
            public void onKey() {
                search();
            }
        });
    }

    /***
     * 点击搜索跳往搜索结果页面
     */
    private void search() {
        String searchStr = et_search.getText().toString().trim();
        if (TextUtils.isEmpty(searchStr)) {
            ToastUtils.show(mContext, "请输入搜索内容");
            return;
        }
        Intent mIntent = new Intent(this, SearchShopResultActivity.class);
        mIntent.putExtra("keyword", searchStr);
        startActivity(mIntent);
        ActivitySlideAnim.slideInAnim(SearchShopActivity.this);
    }


    private void setHistory() {
        if (historyList == null) {
            return;
        }
        fl_history.removeAllViews();
        fl_history.setGravity(Gravity.CENTER);
        int size = historyList.length;
        for (int i = 0; i < size; i++) {
            TextView tv = new TextView(this);
            tv.setText(historyList[i]);
            tv.setTextSize(13);
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundResource(R.drawable.bg_search_shop_fluid_text);
            tv.setTextColor(colorTextBg); //
            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(12, 12, 12, 12);
            fl_history.addView(tv, params);
        }
    }

    private void setHot() {
        if (hotList == null) {
            return;
        }
        fl_hot.removeAllViews();
        fl_hot.setGravity(Gravity.CENTER);
        int size = hotList.length;
        for (int i = 0; i < size; i++) {
            TextView tv = new TextView(this);
            tv.setText(hotList[i]);
            tv.setTextSize(13);
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundResource(R.drawable.bg_search_shop_fluid_text);
            tv.setTextColor(colorTextBg); //
            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(12, 12, 12, 12);
            fl_hot.addView(tv, params);
        }
    }

    @OnClick({R.id.tv_text, R.id.iv_search_type, R.id.iv_search})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_text:
                finish();
                ActivitySlideAnim.slideOutAnim(SearchShopActivity.this);

                break;
            case R.id.iv_search_type:
                // 点击搜索出现下拉框，商品或者店铺

                break;
            case R.id.iv_search:
                // 点击搜索


                break;


        }

    }


}
