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
import com.help.reward.bean.AddressBean;
import com.help.reward.bean.Response.CommitOrderResponse;
import com.help.reward.bean.Response.ConfirmOrderResponse;
import com.help.reward.bean.Response.ShopCartResponse;
import com.help.reward.network.ShopcartNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.view.MyProcessDialog;
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
 * Created by ADBrian on 04/04/2017.
 */

public class ConfirmOrderActivity extends BaseActivity implements View.OnClickListener {

    protected final static int REQ_CODE = 1;
    protected final static int RES_CODE = 2;

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
    private ConfirmOrderResponse.ConfirmOrderBean confirmOrderBean;


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
                    intent.putExtra("from", "confirm_order");
                    startActivityForResult(intent, REQ_CODE);
                    ActivitySlideAnim.slideInAnim(ConfirmOrderActivity.this);
                }
            }
        });
    }

    @OnClick({R.id.iv_title_back, R.id.tv_commit_order})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(ConfirmOrderActivity.this);

                break;
            case R.id.tv_commit_order:

                commitOrderRequest();
                break;
        }
    }

    /***
     * 提交确认订单
     */
    private void commitOrderRequest() {
        if (App.APP_CLIENT_KEY == null) {
            ToastUtils.show(mContext, R.string.string_please_login);
            return;
        }
        String voucher = adapter.getVoucher();
        String pay_message = adapter.getPay_message();
        String general_voucher = null;
        if (adapter.getGeneral_voucher()) { // 用户使用通用卷
            general_voucher = confirmOrderBean.general_voucher_allocation;
        }
        LogUtils.e("优惠卷的值是:" + voucher);
        LogUtils.e("备注留言的值是:" + pay_message);
        LogUtils.e("通用卷是:" + general_voucher);
        if (confirmOrderBean == null) {
            return;
        }
        if (confirmOrderBean.address_info == null || confirmOrderBean.address_info.address_id == null) {
            ToastUtils.show(mContext, "请选择收获地址");
            return;
        }
        MyProcessDialog.showDialog(mContext);
        ShopcartNetwork.getShopcartCookieApi().commitComfirmOrderList(App.APP_CLIENT_KEY, cart_id, if_cart, confirmOrderBean.address_info.address_id
                , confirmOrderBean.vat_hash, confirmOrderBean.address_api.offpay_hash, confirmOrderBean.address_api.offpay_hash_batch, "online",
                voucher, general_voucher, pay_message)
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
//                                ToastUtils.show(mContext, response.data.pay_sn);
                                String pay_sn = response.data.pay_sn;
                                if (!TextUtils.isEmpty(pay_sn)) {
                                    //TODO 支付
                                    Intent mIntent = new Intent(ConfirmOrderActivity.this, PayTypeActivity.class);
                                    mIntent.putExtra("pay_sn", pay_sn);
                                    mIntent.putExtra("removeShopcatAndConfirmOrderActivity", true);
                                    startActivity(mIntent);
                                    ActivitySlideAnim.slideInAnim(ConfirmOrderActivity.this);
                                }
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private void initData() {
        // cart_id=66|1,67|1,68|1,69|1---if_cart=1
        LogUtils.e("确认订单页面 cart_id=" + cart_id + "---if_cart=" + if_cart);
        MyProcessDialog.showDialog(mContext);
        ShopcartNetwork.getShopcartCookieApi().getComfirmOrderList(App.APP_CLIENT_KEY, cart_id, if_cart, null)
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
                        LogUtils.e("请求回来的数据是:" + response.code + "--" + response.msg);
                        if (response.code == 200) {
                            if (response.data != null) {
                                if (response.data.store_cart_list != null) {
                                    //expandChild();
                                    confirmOrderBean = response.data;

                                    adapter.setAddressInfo(response.data.address_info);
                                    adapter.setDiscount_level(response.data.discount_level);
                                    adapter.setAvailable_general_voucher(response.data.available_general_voucher);
                                    adapter.setGeneral_voucher_total_cheap(response.data.general_voucher_total_cheap);
                                    adapter.setAddressApi(response.data.address_api);
                                    store_cart_list.clear();
                                    store_cart_list.addAll(response.data.store_cart_list);
                                    adapter.setDataList(store_cart_list);
                                    lRecyclerview.setVisibility(View.VISIBLE);
                                }
                                if (!TextUtils.isEmpty(response.data.order_amount)) {
                                    mTvTotal.setText(response.data.order_amount);
                                    adapter.setTvTotal(mTvTotal, response.data.order_amount);
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RES_CODE) {
            // 表示从地址页面过来 设置地址view
            AddressBean bean = data.getParcelableExtra("confirmAddress");
            if (adapter != null && bean != null) {
                confirmOrderBean.address_info.address_id = bean.address_id;
                adapter.setAddressInfo(bean);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        initData(); 为啥要放在这里，而且在onCreate方法里面已经调用过
    }

}
