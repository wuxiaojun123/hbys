package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.fragment.GoodFragment;
import com.help.reward.fragment.GoodImgInfoFragment;
import com.help.reward.fragment.GoodRetedFragment;
import com.help.reward.network.ShopcartNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ToastUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MXY on 2017/3/3.
 */

public class GoodInfoActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.iv_goodinfo_back)
    ImageView ivGoodinfoBack;
    @BindView(R.id.iv_goodinfo_more)
    ImageView ivGoodinfoMore;
    @BindView(R.id.pstb_goodinfo)
    PagerSlidingTabStrip pstbGoodinfo;
    @BindView(R.id.layout_goodinfo_title)
    RelativeLayout layoutGoodinfoTitle;
    @BindView(R.id.layout_goodinfo_bottom)
    LinearLayout layoutGoodinfoBottom;
    @BindView(R.id.layout_spill)
    LinearLayout layoutSpill;
    @BindView(R.id.vp_goodinfo)
    ViewPager vpGoodinfo;

    private String goodsId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodinfo);
        ButterKnife.bind(this);
        goodsId = getIntent().getStringExtra("goods_id");

        vpGoodinfo.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pstbGoodinfo.setViewPager(vpGoodinfo);
        vpGoodinfo.setOffscreenPageLimit(3);
    }

    @OnClick({R.id.iv_goodinfo_back,R.id.tv_goodinfo_shopcart_add})
    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.tv_goodinfo_shopcart_add:
               addToShopcart();
               break;
           case R.id.iv_goodinfo_back:
               this.finish();
               break;
       }
    }

    private void addToShopcart() {
        if (!TextUtils.isEmpty(goodsId)) {
            MyProcessDialog.showDialog(mContext);
            ShopcartNetwork.getShopcartCookieApi().getShopcartAdd(App.APP_CLIENT_KEY,goodsId,"1")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<BaseResponse>() {

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
                        public void onNext(BaseResponse baseResponse) {
                            if (baseResponse.code == 200) {
                                ToastUtils.show(mContext, "加入购物车成功");
                            } else {
                                MyProcessDialog.closeDialog();
                                ToastUtils.show(mContext, baseResponse.msg);
                            }
                        }
                    });
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private String[] TITLES = new String[3];

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            TITLES[0] = getResources().getString(R.string.good_lable14);
            TITLES[1] = getResources().getString(R.string.good_lable15);
            TITLES[2] = getResources().getString(R.string.good_lable16);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {
            // 下面两个fragment是个人中心里的
            if (position == 0) {
                GoodFragment goodFragment = new GoodFragment();
                Bundle bundle = new Bundle();
                bundle.putString("goods_id", goodsId);
                goodFragment.setArguments(bundle);
                return goodFragment;
            } else if (position == 1) {
                GoodImgInfoFragment goodImgInfoFragment = new GoodImgInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putString("goods_id", goodsId);
                goodImgInfoFragment.setArguments(bundle);
                return goodImgInfoFragment;
            } else {
                GoodRetedFragment goodRetedFragment = new GoodRetedFragment();
                Bundle bundle = new Bundle();
                bundle.putString("goods_id", goodsId);
                goodRetedFragment.setArguments(bundle);
                return goodRetedFragment;
            }
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }
}
