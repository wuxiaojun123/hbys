package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnLoadMoreListener;
import com.base.recyclerview.OnRefreshListener;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.CouponTradingAdapter;
import com.help.reward.bean.Response.CouponTradingResponse;
import com.help.reward.fragment.BaseFragment;
import com.help.reward.fragment.CouponTradingFragment;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.StringUtils;
import com.help.reward.view.GoodsTypeSearchPop;
import com.help.reward.view.SearchEditTextView;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 优惠劵交易大厅
 * item_coupon_trading.xml
 * <p>
 * Created by wuxiaojun on 2017/1/10.
 */

public class CouponTradingActivity extends BaseActivity implements View.OnClickListener {

    private int numSize = 15; // 一页展示数量
    private int currentPage = 1; // 当前页
    private String order = "asc"; // 升序
    private String storeName = ""; // 店铺名称
    private String goodsname = ""; // 商品名称

    @BindView(R.id.iv_search_type)
    TextView iv_search_type;
    @BindView(R.id.et_search)
    SearchEditTextView et_search;
    @BindView(R.id.tv_text)
    TextView tv_text;
    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;
    private CouponTradingAdapter mCollectionGoodsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_trading);
        ButterKnife.bind(this);
        et_search.setHint(R.string.string_search_relative_goods);

        initRecycler();
        initNetwork();
        initEvent();
    }

    private void initEvent() {
        et_search.setOnKeyListener(new SearchEditTextView.onKeyListener() {
            @Override
            public void onKey() {
                String keyword = et_search.getText().toString().trim();
                if (!StringUtils.checkStr(keyword)) {
                    return;
                }
                LogUtils.e("输入的搜索内容是：" + keyword);
                if ("商品".equals(iv_search_type.getText().toString())) {
                    goodsname = keyword;
                    storeName = "";
                } else { //搜索店铺
                    goodsname = "";
                    storeName = keyword;
                }
                // 跳转到搜索结果的页面
                Intent mIntent = new Intent(CouponTradingActivity.this, CouponTradingSearchResultActivity.class);
                mIntent.putExtra("goodsname", goodsname);
                mIntent.putExtra("storeName", storeName);
                startActivity(mIntent);
                ActivitySlideAnim.slideInAnim(CouponTradingActivity.this);
            }
        });
    }

    private void initRecycler() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mCollectionGoodsAdapter = new CouponTradingAdapter(mContext);
        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(mCollectionGoodsAdapter);
        lRecyclerview.setAdapter(adapter);
        initRefreshListener();
        initLoadMoreListener();
    }

    private void initRefreshListener() {
        lRecyclerview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() { // 如果集合中没有数据，则进行刷新，否则不刷新
                currentPage = 1;
                initNetwork();
            }
        });
    }

    private void initLoadMoreListener() {
        lRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                initNetwork();
            }
        });
    }

    private void initNetwork() {
        PersonalNetwork
                .getResponseApi()
                .getCouponTradingResponse("voucher", "voucher_list", "" + currentPage, App.APP_CLIENT_KEY, storeName, goodsname, order)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<CouponTradingResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        lRecyclerview.refreshComplete(numSize);
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(CouponTradingResponse response) {
                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            if (response.data != null) {
                                if (currentPage == 1) {
                                    mCollectionGoodsAdapter.clear();
                                }
                                mCollectionGoodsAdapter.addAll(response.data.voucher_list);
                            }
                            if (!response.hasmore) { // 是否有更多数据
                                lRecyclerview.setLoadMoreEnabled(false);
                            } else {
                                currentPage += 1;
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    @OnClick({R.id.iv_search_type, R.id.tv_text})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_text: // 取消
                finish();
                ActivitySlideAnim.slideOutAnim(CouponTradingActivity.this);

                break;
            case R.id.iv_search_type: // 弹窗popupwindow
                new GoodsTypeSearchPop().showPopupWindow(this, iv_search_type).setOnTypeChooseListener(new GoodsTypeSearchPop.OnTypeChooseListener() {
                    @Override
                    public void onType(String type) {
                        if ("goods".equals(type)) {
                            et_search.setHint(R.string.string_search_relative_goods);
                            iv_search_type.setText(R.string.string_goods);
                            storeName = "";
                        } else {
                            et_search.setHint(R.string.string_search_relative_store);
                            iv_search_type.setText(R.string.string_store);
                            goodsname = "";
                        }
                    }
                });
                break;
        }

    }

}
