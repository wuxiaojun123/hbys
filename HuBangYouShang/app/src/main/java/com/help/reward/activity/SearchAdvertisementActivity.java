package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnLoadMoreListener;
import com.base.recyclerview.OnRefreshListener;
import com.help.reward.R;
import com.help.reward.adapter.IntegrationWatchPraiseAdapter;
import com.help.reward.bean.Response.AdvertisementResponse;
import com.help.reward.network.IntegrationNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.view.SearchEditTextView;
import com.idotools.utils.InputWindowUtils;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 搜索广告页面
 * Created by wuxiaojun on 2017/3/22.
 */

public class SearchAdvertisementActivity extends BaseActivity implements View.OnClickListener {
    protected Subscription subscribe;

    @BindView(R.id.iv_email)
    ImageView iv_email; // 隐藏
    @BindView(R.id.tv_text)
    TextView tv_text; // 取消
    @BindView(R.id.et_search)
    SearchEditTextView et_search; // 搜索内容
    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;
    @BindView(R.id.ll_empty)
    View ll_empty;

    private int numSize = 15;
    private int currentPage = 1;
    private String searchStr;
    private IntegrationWatchPraiseAdapter mIntegrationWatchPraiseAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_advertisement);
        ButterKnife.bind(this);
        initView();
        initRecycler();
        searchStr = getIntent().getStringExtra("keyword");
        if (!TextUtils.isEmpty(searchStr)) {
            initNetwor();
        }
    }

    private void initView() {
        iv_email.setVisibility(View.GONE);
        tv_text.setVisibility(View.VISIBLE);
        tv_text.setText("取消");
        et_search.setOnKeyListener(new SearchEditTextView.onKeyListener() {
            @Override
            public void onKey() {
                search();
            }
        });
    }

    private void initRecycler() {
        lRecyclerview.setEmptyView(ll_empty);
        lRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mIntegrationWatchPraiseAdapter = new IntegrationWatchPraiseAdapter(mContext);
        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(mIntegrationWatchPraiseAdapter);
        lRecyclerview.setAdapter(adapter);
        lRecyclerview.setPullRefreshEnabled(false);
        initLoadMoreListener();
    }

    @OnClick({R.id.iv_search, R.id.tv_text})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_search:
                search();

                break;
            case R.id.tv_text:
                finish();
                ActivitySlideAnim.slideOutAnim(SearchAdvertisementActivity.this);

                break;

        }
    }

    private void search() {
        searchStr = et_search.getText().toString().trim();
        if (!TextUtils.isEmpty(searchStr)) {
            initNetwor();
            InputWindowUtils.closeInputWindow(et_search, mContext);
        } else {
            ToastUtils.show(mContext, "请输入搜索内容");
        }
    }

    private void initNetwor() {
        // mobile/index.php?act=advertisement&op=search
        subscribe = IntegrationNetwork
                .getIntegrationApi()
                .getSearchAdvertisementResponse("advertisement", "search", currentPage + "", searchStr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<AdvertisementResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        lRecyclerview.refreshComplete(numSize);
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(AdvertisementResponse response) {
                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            if (response.data != null) {
                                if (response.data.adv_list != null) {
                                    mIntegrationWatchPraiseAdapter.setDataList(response.data.adv_list);
                                } else {
                                    mIntegrationWatchPraiseAdapter.clear();
                                    ToastUtils.show(mContext,"没有找到符合条件的广告");
                                }
                            } else { // 显示无搜索结果
                                mIntegrationWatchPraiseAdapter.clear();
                                ToastUtils.show(mContext,"没有找到符合条件的广告");
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

    private void initLoadMoreListener() {
        lRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                initNetwor();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }

}
