package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnItemClickListener;
import com.base.recyclerview.OnLoadMoreListener;
import com.help.reward.R;
import com.help.reward.adapter.PayEndGoodsAdapter;
import com.help.reward.bean.Response.StoreDetailAllResponse;
import com.help.reward.bean.ShopMallHotBean;
import com.help.reward.network.ShopMallNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MXY on 2017/2/25.
 */

public class PayendActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;

    TextView tv_payend_type;
    TextView tv_payend_money;
    TextView tv_payend_seeorder;
    TextView tv_payend_gohome;

    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;

    private PayEndGoodsAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    List<ShopMallHotBean> mDatas = new ArrayList<>();
    int curpage = 1;
    String money, type,order_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payend);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            money = bundle.getString("money");
            type = bundle.getString("type");
            order_id = bundle.getString("order_id");
        }
        initView();
        initData();
    }

    private void initView() {
        ivTitleBack.setVisibility(View.GONE);
        tvTitleRight.setVisibility(View.GONE);
        tvTitle.setText("订单支付成功");
    }

    private void initData() {


        lRecyclerview.setLayoutManager(new GridLayoutManager(mContext, 2));
        adapter = new PayEndGoodsAdapter(mContext);
        adapter.setDataList(mDatas);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        //禁用下拉刷新功能
        lRecyclerview.setPullRefreshEnabled(false);
        //禁用自动加载更多功能
        lRecyclerview.setLoadMoreEnabled(false);
        lRecyclerview.setItemAnimator(new DefaultItemAnimator());
        initHeader();
        initLoadMoreListener();
        initItemClickListener();
//        MyProcessDialog.showDialog(this);
//        requestData();
    }

    private void initHeader() {
        View view = getLayoutInflater().inflate(
                R.layout.header_payend, null);
        tv_payend_type = (TextView) view.findViewById(R.id.tv_payend_type);
        tv_payend_money = (TextView) view.findViewById(R.id.tv_payend_money);
        tv_payend_seeorder = (TextView) view.findViewById(R.id.tv_payend_seeorder);
        tv_payend_gohome = (TextView) view.findViewById(R.id.tv_payend_gohome);

        tv_payend_type.setText(type);
        tv_payend_money.setText(money);
        tv_payend_seeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PayendActivity.this,OrderDetailsActivity.class);
                intent.putExtra("order_id",order_id);
                startActivity(intent);
                finish();
            }
        });
        tv_payend_gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PayendActivity.this, MainActivity.class));
                finish();
            }
        });
        mLRecyclerViewAdapter.addHeaderView(view);

    }

    private void initLoadMoreListener() {
        lRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                curpage++;
                requestData();
            }
        });
    }

    private void initItemClickListener() {
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, GoodInfoActivity.class);
                intent.putExtra("goods_id", adapter.getDataList().get(position).goods_id);
                startActivity(intent);
                ActivitySlideAnim.slideInAnim(PayendActivity.this);
            }

        });
    }

    Subscription subscribe;

    private void requestData() {

        subscribe = ShopMallNetwork
                .getShopMallMainApi()
                .getPayEndGoodsResponse(curpage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<StoreDetailAllResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        lRecyclerview.refreshComplete(15);
                        MyProcessDialog.closeDialog();
                        ToastUtils.show(mContext, R.string.string_error);
                        if (curpage != 1)
                            curpage--;
                    }

                    @Override
                    public void onNext(StoreDetailAllResponse response) {
                        lRecyclerview.refreshComplete(15);
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if (response.data != null) {
                                if (curpage == 1) {
                                    mDatas = response.data.goods_list;
                                    adapter.setDataList(response.data.goods_list);
                                    if (adapter.getDataList().size() == 0) {
                                    }
                                } else {
                                    mDatas.addAll(response.data.goods_list);
                                    adapter.addAll(response.data.goods_list);
                                }
                            }
                            if (!response.hasmore) {
                                lRecyclerview.setLoadMoreEnabled(false);
                            } else {
                                lRecyclerview.setLoadMoreEnabled(true);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });

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
