package com.help.reward.activity;

import android.content.Intent;
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
import com.google.android.gms.games.multiplayer.InvitationEntity;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.fragment.GoodFragment;
import com.help.reward.fragment.GoodImgInfoFragment;
import com.help.reward.fragment.GoodRetedFragment;
import com.help.reward.network.ShopcartNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.CollectionRxbusType;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.view.MyProcessDialog;
import com.help.reward.view.StoreInfoMenuPop;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by MXY on 2017/3/3.
 */

public class GoodInfoActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.iv_collection)
    ImageView iv_collection; // 收藏

    private String goodsId;
    private String storeId;
    GoodFragment goodFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodinfo);
        ButterKnife.bind(this);
        goodsId = getIntent().getStringExtra("goods_id");
        storeId = getIntent().getStringExtra("store_id");

        vpGoodinfo.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pstbGoodinfo.setViewPager(vpGoodinfo);
        vpGoodinfo.setOffscreenPageLimit(3);
        initEvent();
    }

    @OnClick({R.id.iv_goodinfo_back, R.id.tv_goodinfo_shopcart_add, R.id.iv_goodinfo_more,
            R.id.tv_goodinfo_buy, R.id.ll_store, R.id.ll_seller_group, R.id.ll_collection})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_goodinfo_shopcart_add:
                addToShopcart();
                break;
            case R.id.iv_goodinfo_back:
                this.finish();
                ActivitySlideAnim.slideOutAnim(this);
                break;
            case R.id.iv_goodinfo_more:
                StoreInfoMenuPop.showPopupWindow(this, ivGoodinfoMore);
                break;
            case R.id.tv_goodinfo_buy:
                Intent intent = new Intent(GoodInfoActivity.this, ConfirmOrderActivity.class);
                intent.putExtra("cart_id", goodsId + "|" + goodFragment.propertyBean.getSelectNum());
                intent.putExtra("if_cart", "0");
                startActivity(intent);
                ActivitySlideAnim.slideInAnim(GoodInfoActivity.this);

                break;
            case R.id.ll_store: // 店铺
                if (App.APP_CLIENT_KEY == null) {
                    ToastUtils.show(mContext, R.string.string_please_login);
                    return;
                }
                if (!TextUtils.isEmpty(storeId)) {
                    Intent mStoreIntent = new Intent(GoodInfoActivity.this, StoreInfoActivity.class);
                    mStoreIntent.putExtra("store_id", storeId);
                    startActivity(mStoreIntent);
                    ActivitySlideAnim.slideInAnim(GoodInfoActivity.this);
                }

                break;
            case R.id.ll_seller_group: // 商家群


                break;
            case R.id.ll_collection: // 收藏
                collectionShop();

                break;
        }
    }

    /***
     * 收藏商品
     */
    private void collectionShop() {
        if (!TextUtils.isEmpty(goodsId)) {
            MyProcessDialog.showDialog(mContext);
            ShopcartNetwork
                    .getShopcartCookieApi()
                    .getCollectionShopss(App.APP_CLIENT_KEY, goodsId)
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
                            MyProcessDialog.closeDialog();
                            if (baseResponse.code == 200) { // 收藏成功
                                iv_collection.setImageResource(R.mipmap.nav_favorites_b);
                            }
                            ToastUtils.show(mContext, baseResponse.msg);
                        }
                    });
        }
    }

    private void addToShopcart() {
        if (App.APP_CLIENT_KEY == null) {
            ToastUtils.show(mContext, R.string.string_please_login);
            return;
        }
        if (!TextUtils.isEmpty(goodsId)) {
            MyProcessDialog.showDialog(mContext);
            ShopcartNetwork.getShopcartCookieApi().getShopcartAdd(App.APP_CLIENT_KEY, goodsId, goodFragment.propertyBean.getSelectNum())
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
                            MyProcessDialog.closeDialog();
                            if (baseResponse.code == 200) {
                                ToastUtils.show(mContext, "加入购物车成功");
                            } else {
                                ToastUtils.show(mContext, baseResponse.msg);
                            }
                        }
                    });
        }
    }

    private Subscription subscribe;

    private void initEvent() {
        subscribe = RxBus.getDefault().toObservable(CollectionRxbusType.class).subscribe(new Action1<CollectionRxbusType>() {
            @Override
            public void call(CollectionRxbusType type) {
                LogUtils.e("收到值" + type.collection);
                if (type.collection) {
                    iv_collection.setImageResource(R.mipmap.nav_favorites_b);
                } else {
                    iv_collection.setImageResource(R.mipmap.nav_favorites_a);
                }
                if (!subscribe.isUnsubscribed()) {
                    subscribe.unsubscribe();
                }
            }
        });
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
                goodFragment = new GoodFragment();
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
