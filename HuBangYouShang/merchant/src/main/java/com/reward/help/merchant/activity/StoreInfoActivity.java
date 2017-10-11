package com.reward.help.merchant.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.idotools.utils.ToastUtils;
import com.reward.help.merchant.App;
import com.reward.help.merchant.R;
import com.reward.help.merchant.bean.Response.StoreInfoResponse;
import com.reward.help.merchant.chat.ui.BaseActivity;
import com.reward.help.merchant.network.PersonalNetwork;
import com.reward.help.merchant.network.base.BaseSubscriber;
import com.reward.help.merchant.view.MyProcessDialog;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class StoreInfoActivity extends BaseActivity {



    @BindView(R.id.iv_title_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_store_check_progress)
    ImageView mIvStoreCheckProgress;
    @BindView(R.id.tv_store_check)
    TextView mTvCheck;
    @BindView(R.id.tv_store_check_des)
    TextView mTvCheckDes;

    @BindView(R.id.tv_store_check_name)
    TextView mTvStoreName;
    @BindView(R.id.tv_store_check_type)
    TextView mTvStoreType;
    @BindView(R.id.tv_store_check_type2)
    TextView mTvStoreType2;
    @BindView(R.id.tv_store_check_main)
    TextView mTvStoreMain;
    @BindView(R.id.tv_store_check_owner)
    TextView mTvStoreOwner;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_store_info);
        ButterKnife.bind(this);

        mTvTitle.setText("店铺信息");
        mIvStoreCheckProgress.setVisibility(View.INVISIBLE);
        mTvCheck.setVisibility(View.INVISIBLE);
        mTvCheckDes.setVisibility(View.GONE);

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initData();
    }

    private void initData() {
        MyProcessDialog.showDialog(StoreInfoActivity.this);
        subscribe = PersonalNetwork.getStoreApi().getStoreInfo(App.getAppClientKey())
                .subscribeOn(Schedulers.io()) // 请求放在io线程中
                .observeOn(AndroidSchedulers.mainThread()) // 请求结果放在主线程中
                .subscribe(new BaseSubscriber<StoreInfoResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        if (e instanceof UnknownHostException) {
                            ToastUtils.show(mContext, "请求到错误服务器");
                        } else if (e instanceof SocketTimeoutException) {
                            ToastUtils.show(mContext, "请求超时");
                        }
                    }

                    @Override
                    public void onNext(StoreInfoResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                           showInfo(response.data);
                            //finish();
                            //ActivitySlideAnim.slideOutAnim(LoginActivity.this);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private void showInfo(StoreInfoResponse.StoreInfoBean data) {
        if(data != null) {
            mTvStoreName.setText(data.company_name);
            mTvStoreType.setText(data.sc_name);
            String[] types = data.store_class_names;
            if (types != null && types.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (String info: types) {
                    sb.append(info).append(",");
                }
                mTvStoreType2.setText(sb.toString().substring(0,sb.length() -2));
            }
            mTvStoreMain.setText(data.company_name);
            mTvStoreOwner.setText(data.contacts_name);

            if ("1".equals(data.joinin_type)) {//失败
                mIvStoreCheckProgress.setImageResource(R.mipmap.store_unpass);
            } else if ("2".equals(data.joinin_type)) {//成功
                mIvStoreCheckProgress.setImageResource(R.mipmap.store_pass);
            } else {
                mIvStoreCheckProgress.setImageResource(R.mipmap.store_wait);

            }
            mIvStoreCheckProgress.setVisibility(View.VISIBLE);
        }
    }
}
