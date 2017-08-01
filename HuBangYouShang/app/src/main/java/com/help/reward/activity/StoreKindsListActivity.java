package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnItemClickListener;
import com.help.reward.R;
import com.help.reward.adapter.StoreKindsListAdapter;
import com.help.reward.bean.Response.StoreKindsResponse;
import com.help.reward.bean.StoreKindsBean;
import com.help.reward.network.StoreDetailNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.StringUtils;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 店铺分类列表
 */

public class StoreKindsListActivity extends BaseActivity {
    protected Subscription subscribe;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;

    @BindView(R.id.lv_srorekinds)
    LRecyclerView lRecyclerview;
    private StoreKindsListAdapter adapter;
    public List<StoreKindsBean> mDatas = new ArrayList<>();
    String store_id;
    LRecyclerViewAdapter mLRecyclerViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storekindslist);
        ButterKnife.bind(this);
        store_id = getIntent().getExtras().getString("store_id");
        if (!StringUtils.checkStr(store_id)) {
            finish();
        }
        initView();
        initRecycler();
        MyProcessDialog.showDialog(mContext);
        requestData();
    }

    private void initView() {
        tvTitle.setText("商品分类");
        tvTitleRight.setVisibility(View.GONE);
    }

    private void initRecycler() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StoreKindsListAdapter(this);
        adapter.setDataList(mDatas);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        //禁用下拉刷新功能
        lRecyclerview.setPullRefreshEnabled(false);
        //禁用自动加载更多功能
        lRecyclerview.setLoadMoreEnabled(false);
        lRecyclerview.setItemAnimator(new DefaultItemAnimator());
        lRecyclerview.setVisibility(View.GONE);
        initItemClickListener();
    }

    private void initItemClickListener() {
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String gc_id = adapter.getDataList().get(position).id;
                goToSearchShopResultActivity(gc_id);
            }
        });
    }

    @OnClick({R.id.iv_title_back, R.id.layout_all})
    void click(View v) {
        switch (v.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.layout_all:
                goToSearchShopResultActivity(null);

                break;
        }
    }

    private void requestData() {
        subscribe = StoreDetailNetwork
                .getStroeApi()
                .getStoreGoodsClassResponse("store_goods_class", store_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<StoreKindsResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                    }

                    @Override
                    public void onNext(StoreKindsResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if (response.data != null) {
                                lRecyclerview.setVisibility(View.VISIBLE);
                                adapter.setDataList(response.data.store_goods_class);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    /***
     * 跳转到商品搜索页面
     *
     * @param gc_id
     */
    private void goToSearchShopResultActivity(String gc_id) {
        Intent mIntent = new Intent(mContext, SearchShopResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("gc_id", gc_id); // 商品分类id
        bundle.putString("store_id", store_id);
        bundle.putString("searchType", "goods");
        bundle.putBoolean("showShop", true);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        ActivitySlideAnim.slideInAnim(StoreKindsListActivity.this);
    }

    @Override
    protected void onDestroy() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        super.onDestroy();
        ActivitySlideAnim.slideOutAnim(this);
    }

}
