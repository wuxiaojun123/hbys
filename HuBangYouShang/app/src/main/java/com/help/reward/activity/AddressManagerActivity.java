package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.AddressManagerAdapter;
import com.help.reward.bean.AddressBean;
import com.help.reward.bean.Response.AddressResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wuxiaojun on 2017/1/8.
 * 地址管理
 */

public class AddressManagerActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_CODE1 = 1;

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.btn_add_address)
    Button btn_add_address;
    //    @BindView(R.id.tv_empty_view)
//    TextView tv_empty_view;
    @BindView(R.id.recycler_view)
    LRecyclerView lRecyclerview;// item_address_manager

    private int numSize = 15;
    private AddressManagerAdapter mHelpPostAdapter;

    private String fromFlag; // confirm_order 表示从确认订单页面过来的

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manager);
        ButterKnife.bind(this);
        fromFlag = getIntent().getStringExtra("from");
        initView();
        initNetwork();
    }

    private void initView() {
        tv_title.setText(R.string.string_address_manager_title);
        tv_title_right.setVisibility(View.GONE);
        initRecyclerView();
    }

    private void initRecyclerView() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mHelpPostAdapter = new AddressManagerAdapter(mContext);
        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(mHelpPostAdapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
//        lRecyclerview.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.layout_no_address_manager,null));
        lRecyclerview.setPullRefreshEnabled(false);
        lRecyclerview.setLoadMoreEnabled(false);
    }

    private void initNetwork() {
        PersonalNetwork
                .getResponseApi()
                .getAddressResponse(App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<AddressResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        lRecyclerview.refreshComplete(numSize);
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(AddressResponse response) {
                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            if (response.data != null) {
                                mHelpPostAdapter.setDataList(response.data.address_list);
                            }

                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    @OnClick({R.id.iv_title_back, R.id.btn_add_address})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                setAddress();
                finish();
                ActivitySlideAnim.slideOutAnim(AddressManagerActivity.this);

                break;
            case R.id.btn_add_address:
                Intent mIntent = new Intent(AddressManagerActivity.this, AddAddressActivity.class);
                startActivityForResult(mIntent, REQUEST_CODE1);
                ActivitySlideAnim.slideInAnim(AddressManagerActivity.this);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUEST_CODE1) {
            initNetwork();
        }
    }

    private void setAddress() {
        if (!TextUtils.isEmpty(fromFlag)) {
            if (mHelpPostAdapter != null) {
                AddressBean checkBean = null;
                List<AddressBean> addressBeanList = mHelpPostAdapter.getDataList();
                if (addressBeanList != null) {
                    for (AddressBean bean : addressBeanList) {
                        if (!bean.is_default.equals("0")) {
                            checkBean = bean;
                            break;
                        }
                    }
                }
                Intent mIntent = new Intent();
                mIntent.putExtra("confirmAddress", checkBean);
                setResult(ConfirmOrderActivity.RES_CODE, mIntent);
            }
        }
    }

    @Override
    public void onBackPressed() {
        setAddress();
        super.onBackPressed();
    }

}
