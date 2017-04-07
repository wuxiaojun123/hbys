package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
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
import com.help.reward.bean.Response.CommitOrderResponse;
import com.help.reward.bean.Response.ConfirmOrderResponse;
import com.help.reward.bean.Response.ShopCartResponse;
import com.help.reward.network.ShopcartNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

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

    @BindView(R.id.tv_total)
    TextView mTvTotal;

    ConfirmOrderAdapter adapter;

    private String cart_id;
    private String if_cart;
    private List<ConfirmOrderResponse.ConfirmCartList> store_cart_list = new ArrayList<>();
    ConfirmOrderResponse.ConfirmOrderBean confirmOrderBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        Intent intent = getIntent();
        cart_id = intent.getStringExtra("cart_id");
        if_cart = intent.getStringExtra("if_cart");
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
        lRecyclerview.setVisibility(View.GONE);

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
                commitOrderRequest();
                break;
        }
    }

    private void commitOrderRequest() {

        MyProcessDialog.showDialog(mContext);
        ShopcartNetwork.getShopcartCookieApi().commitComfirmOrderList(App.APP_CLIENT_KEY,cart_id,if_cart,confirmOrderBean.address_info.address_id
                ,confirmOrderBean.vat_hash,confirmOrderBean.address_api.offpay_hash,confirmOrderBean.address_api.offpay_hash_batch,"online")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<CommitOrderResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(CommitOrderResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if (response.data != null) {
                                ToastUtils.show(mContext,response.data.pay_sn);
                                //TODO 支付
                            }

                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private void initData(){
        MyProcessDialog.showDialog(mContext);
        ShopcartNetwork.getShopcartCookieApi().getComfirmOrderList(App.APP_CLIENT_KEY,cart_id,if_cart,null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ConfirmOrderResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(ConfirmOrderResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if (response.data != null) {
                                if (response.data.store_cart_list!= null) {
                                    //expandChild();
                                    confirmOrderBean = response.data;
                                    adapter.setAddressInfo(response.data.address_info);
                                    adapter.setDiscount_level(response.data.discount_level);
                                    adapter.setAvailable_general_voucher(response.data.available_general_voucher);
                                    store_cart_list.clear();
                                    store_cart_list.addAll(response.data.store_cart_list);
                                    adapter.setDataList(store_cart_list);
                                    lRecyclerview.setVisibility(View.VISIBLE);


                                }

                                if (!TextUtils.isEmpty(response.data.order_amount)) {
                                    mTvTotal.setText("¥" + response.data.order_amount);
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

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
