package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnItemClickListener;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.ConfirmOrderAdapter;
import com.help.reward.adapter.MyOrderAdapter;
import com.help.reward.bean.Response.ShopCartResponse;
import com.help.reward.network.ShopcartNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ADBrian on 04/04/2017.
 */

public class ConfirmOrderActivity extends BaseActivity implements View.OnClickListener{

    protected final static  int REQ_CODE = 1;

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;

    ConfirmOrderAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        tv_title.setText("确认订单");
        tv_title_right.setVisibility(View.GONE);
        lRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new ConfirmOrderAdapter(mContext);
        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        lRecyclerview.setLoadMoreEnabled(false);
        lRecyclerview.setPullRefreshEnabled(false);


        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0) {
                    Intent intent = new Intent(ConfirmOrderActivity.this, AddressManagerActivity.class);
                    intent.putExtra("from","confirm_order");
                    startActivityForResult(intent,REQ_CODE);
                }
            }
        });
    }

    @OnClick({R.id.iv_title_back,R.id.tv_commit_order})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_commit_order:
                break;
        }
    }

    private void initData(){
        ShopcartNetwork.getShopcartCookieApi().getShopcartList(App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ShopCartResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(ShopCartResponse response) {
                        if (response.code == 200) {
                            if (response.data != null) {
                                if (response.data.cart_list != null) {
                                    //expandChild();
                                    adapter.setDataList(response.data.cart_list);
                                }
                            }

                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
    }
}
