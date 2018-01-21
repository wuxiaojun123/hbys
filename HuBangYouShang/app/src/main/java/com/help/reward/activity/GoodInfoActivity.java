package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.help.reward.bean.Response.AddSellerGroupResponse;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.chat.Constant;
import com.help.reward.chat.ui.ChatActivity;
import com.help.reward.fragment.GoodFragment;
import com.help.reward.fragment.GoodImgInfoFragment;
import com.help.reward.fragment.GoodRetedFragment;
import com.help.reward.network.ShopcartNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.GoodInfoRxbusType;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.view.AlertDialog;
import com.help.reward.view.MyProcessDialog;
import com.help.reward.view.StoreInfoMenuPop;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
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
 * 商品详情
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

    private String member_id; // 加商家群的时候需要用到
    private boolean is_in_group; // 标记是否已经加入群组
    private Subscription subscribeRxBus;

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
                //addToShopcart();
                if (goodFragment != null) {
                    goodFragment.startPropertyActivity();
                }
                break;
            case R.id.iv_goodinfo_back:
                this.finish();
                ActivitySlideAnim.slideOutAnim(this);
                break;
            case R.id.iv_goodinfo_more:
                StoreInfoMenuPop.showPopupWindow(this, ivGoodinfoMore);
                break;
            case R.id.tv_goodinfo_buy:
                // 获取到goodsFragment
                if (goodFragment != null) {
                    goodFragment.startPropertyActivity();
                }
                /*Intent intent = new Intent(GoodInfoActivity.this, ConfirmOrderActivity.class);
                intent.putExtra("cart_id", goodFragment.propertyBean.getGoods_id() + "|" + goodFragment.propertyBean.getSelectNum());
                intent.putExtra("if_cart", "0");
                startActivity(intent);
                ActivitySlideAnim.slideInAnim(GoodInfoActivity.this);*/

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
            case R.id.ll_seller_group: // 加入商家群
                addSellerGroup();

                break;
            case R.id.ll_collection: // 收藏
                collectionShop();

                break;
        }
    }

    /**
     * 加入商家群
     */
    private void addSellerGroup() {
        if (App.APP_CLIENT_KEY == null) {
            ToastUtils.show(mContext, R.string.string_please_login);
            return;
        }
        if (is_in_group && !TextUtils.isEmpty(member_id)) { // 已经加入到商家群了，直接跳转到群页面
            /*Intent intent = new Intent(GoodInfoActivity.this, ChatActivity.class);
            intent.putExtra(Constant.EXTRA_USER_ID, member_id);
            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
            startActivity(intent);
            ActivitySlideAnim.slideInAnim(GoodInfoActivity.this);*/
            showDialogWhetherEnterGroup(R.string.string_whether_enter_group);

        } else {
            if (TextUtils.isEmpty(member_id)) {
                return;
            }
            ShopcartNetwork
                    .getShopcartCookieApi()
                    .getAddSellerGroup(App.APP_CLIENT_KEY, member_id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<AddSellerGroupResponse>() {

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
                        public void onNext(final AddSellerGroupResponse res) {
                            MyProcessDialog.closeDialog();
                            if (res.code == 200) { // 收藏成功
                                LogUtils.e("结果是：" + res.data.groupid);
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                                        } catch (HyphenateException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.start();
                                is_in_group = true;
                                member_id = res.data.groupid;
                                showDialogWhetherEnterGroup(R.string.string_whether_success_join_group);
                            } else {
                                ToastUtils.show(mContext, res.msg);
                            }
                        }
                    });
        }
    }

    private void showDialogWhetherEnterGroup(int textResId) {
        new AlertDialog(mContext)
                .builder()
                .setTitle(R.string.string_system_prompt)
                .setMsg(textResId)
                .setPositiveButton("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GoodInfoActivity.this, ChatActivity.class);
                        intent.putExtra(Constant.EXTRA_USER_ID, member_id);
                        intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                        startActivity(intent);
                        ActivitySlideAnim.slideInAnim(GoodInfoActivity.this);
                    }
                })
                .setNegativeButton("否", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .show();
    }

    /***
     * 收藏商品
     */
    private void collectionShop() {
        if (App.APP_CLIENT_KEY == null) {
            ToastUtils.show(mContext, R.string.string_please_login);
            return;
        }
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
        goodsId = goodFragment.propertyBean.getGoods_id();
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

    private void initEvent() {
        subscribeRxBus = RxBus.getDefault().toObservable(GoodInfoRxbusType.class).subscribe(new Action1<GoodInfoRxbusType>() {
            @Override
            public void call(GoodInfoRxbusType type) {
                if (type.collection) {
                    iv_collection.setImageResource(R.mipmap.nav_favorites_b);
                } else {
                    iv_collection.setImageResource(R.mipmap.nav_favorites_a);
                }
                member_id = type.member_id;
                is_in_group = type.is_in_group;
                if (!subscribeRxBus.isUnsubscribed()) {
                    subscribeRxBus.unsubscribe();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribeRxBus != null && !subscribeRxBus.isUnsubscribed()) {
            subscribeRxBus.unsubscribe();
        }
    }

}
