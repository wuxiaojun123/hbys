package com.help.reward.activity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.gxz.library.StickyNavLayout;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.Response.StringResponse;
import com.help.reward.bean.StotrDetailBean;
import com.help.reward.fragment.ShopInfoAllFragment;
import com.help.reward.fragment.ShopInfoHomeFragment;
import com.help.reward.fragment.ShopInfoNewFragment;
import com.help.reward.network.StoreDetailNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.GlideUtils;
import com.help.reward.view.MyProcessDialog;
import com.help.reward.view.SearchEditTextView;
import com.help.reward.view.StoreInfoMenuPop;
import com.idotools.utils.InputWindowUtils;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 店铺信息
 * Created by MXY on 2017/2/26.
 */

public class StoreInfoActivity extends BaseActivity implements ShopInfoHomeFragment.OnShopInfoLoadSucced {


    @BindView(R.id.iv_shopinfo_title_back)
    ImageView ivShopinfoTitleBack;
    @BindView(R.id.iv_shopinfo_title_type)
    ImageView ivShopinfoTitleType;
    @BindView(R.id.et_shop_search)
    SearchEditTextView et_shop_search;
    @BindView(R.id.iv_shopinfo_title_more)
    ImageView iv_shopinfo_title_more;
    @BindView(R.id.iv_shopinfo_bg)
    ImageView iv_shopinfo_bg;
    @BindView(R.id.pstb_shopinfo)
    PagerSlidingTabStrip pstbShopinfo;
    @BindView(R.id.iv_shopinfo_userimg)
    ImageView ivShopinfoUserimg;
    @BindView(R.id.tv_shop_funs)
    TextView tv_shop_funs;
    @BindView(R.id.tv_shop_name)
    TextView tv_shop_name;
    @BindView(R.id.tv_collect)
    TextView tv_collect;
    @BindView(R.id.layout_alltitle)
    LinearLayout layout_alltitle;

    @BindView(R.id.tv_zonghe)
    TextView tv_zonghe;
    @BindView(R.id.tv_salenum)
    TextView tv_salenum;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.iv_style)
    ImageView iv_style;
    @BindView(R.id.id_stick)
    StickyNavLayout id_stick;

    @BindView(R.id.id_stickynavlayout_viewpager)
    ViewPager vpShopinfo;
    String store_id;
    ShopInfoHomeFragment shopInfoHomeFragment;
    ShopInfoAllFragment shopInfoAllFragment;
    ShopInfoNewFragment shopInfoNewFragment;
    String saleOrder="asc";
    String priceOrder="asc";
    String type="gird";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopinfo);
        ButterKnife.bind(this);
        initView();
        store_id = getIntent().getExtras().getString("store_id");
        Bundle bundle = new Bundle();
        bundle.putString("store_id", store_id);
        shopInfoHomeFragment = new ShopInfoHomeFragment();
        shopInfoHomeFragment.setArguments(bundle);
        shopInfoAllFragment = new ShopInfoAllFragment();
        shopInfoAllFragment.setArguments(bundle);
        shopInfoHomeFragment.setOnShopInfoLoadSucced(this);
        shopInfoNewFragment = new ShopInfoNewFragment();
        shopInfoNewFragment.setArguments(bundle);
        vpShopinfo.setOffscreenPageLimit(3);
        vpShopinfo.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        vpShopinfo.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    layout_alltitle.setVisibility(View.VISIBLE);
                } else if (position == 0){
                    layout_alltitle.setVisibility(View.GONE);
                }else{
                    layout_alltitle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pstbShopinfo.setViewPager(vpShopinfo);
    }
    void initView(){
        et_shop_search.setOnKeyListener(new SearchEditTextView.onKeyListener() {
            @Override
            public void onKey() {
                String searchStr = et_shop_search.getText().toString().trim();
                if (!TextUtils.isEmpty(searchStr)) {
                    InputWindowUtils.closeInputWindow(et_shop_search, mContext);
                    Intent intent = new Intent(mContext, SearchHelpActivity.class);
                    intent.putExtra("keyword", searchStr);
                    mContext.startActivity(intent);
                    ActivitySlideAnim.slideInAnim(StoreInfoActivity.this);
                } else {
                    ToastUtils.show(mContext, "请输入搜索内容");
                }

            }
        });
    }

    @OnClick({R.id.iv_shopinfo_title_back, R.id.iv_shopinfo_title_type, R.id.iv_shopinfo_title_more,
            R.id.tv_collect, R.id.tv_zonghe, R.id.tv_salenum, R.id.tv_price, R.id.iv_style})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_shopinfo_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(this);
                break;
            case R.id.iv_shopinfo_title_type:
                Intent intent = new Intent(this,StoreKindsListActivity.class);
                intent.putExtra("store_id",store_id);
                startActivity(intent);
                ActivitySlideAnim.slideInAnim(this);
                break;
            case R.id.iv_shopinfo_title_more:
                StoreInfoMenuPop.showPopupWindow(this,iv_shopinfo_title_more);
                break;
            case R.id.tv_collect:
                tv_collect.setEnabled(false);
                collectStore();
                break;
            case R.id.tv_zonghe:
                shopInfoAllFragment.setKey("","");
                break;
            case R.id.tv_salenum:
                if("asc".equals(saleOrder)){
                    saleOrder="desc";
                }else{
                    saleOrder="asc";
                }
                shopInfoAllFragment.setKey("salenum",saleOrder);
                break;
            case R.id.tv_price:
                if("asc".equals(priceOrder)){
                    priceOrder="desc";
                    Drawable drawable = mContext.getResources().getDrawable(R.mipmap.up_to_down);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
                    tv_price.setCompoundDrawables(null, null, drawable, null);
                }else{
                    priceOrder="asc";
                    Drawable drawable = mContext.getResources().getDrawable(R.mipmap.down_to_up);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
                    tv_price.setCompoundDrawables(null, null, drawable, null);
                }
                shopInfoAllFragment.setKey("price",priceOrder);
                break;
            case R.id.iv_style:
                if("list".equals(type)){
                    type ="gird";
                    iv_style.setImageResource(R.mipmap.list_piece);
                    shopInfoAllFragment.setGridAdapter();
                }else{
                    type ="list";
                    iv_style.setImageResource(R.mipmap.list_grid);
                    shopInfoAllFragment.setListAdapter();
                }

                break;
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private String[] TITLES = new String[3];

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            TITLES[0] = getResources().getString(R.string.shop_lable18);
            TITLES[1] = getResources().getString(R.string.shop_lable19);
            TITLES[2] = getResources().getString(R.string.shop_lable20);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {
            // 下面两个fragment是个人中心里的
            if (position == 0) {
                return shopInfoHomeFragment;
            } else if (position == 1) {
                return shopInfoAllFragment;
            } else {
                return shopInfoNewFragment;
            }
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            if (observer != null)
                super.unregisterDataSetObserver(observer);
        }
    }

    @Override
    public void onLoad(StotrDetailBean store_info) {
        GlideUtils.loadImage(store_info.store_banner, iv_shopinfo_bg);
        GlideUtils.loadImage(store_info.store_avatar, ivShopinfoUserimg);
        tv_shop_funs.setText(store_info.store_collect + "粉丝");
        tv_shop_name.setText(store_info.store_name);
        tv_collect.setText(store_info.is_favorate ? "已收藏" : "收藏");
    }


    private void collectStore() {
        MyProcessDialog.showDialog(this);
        StoreDetailNetwork
                .getStroeApi()
                .getAddFavoritesStoreResponse(App.APP_CLIENT_KEY, store_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<StringResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        tv_collect.setEnabled(true);
                        MyProcessDialog.closeDialog();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(StringResponse response) {
                        MyProcessDialog.closeDialog();
                        tv_collect.setEnabled(true);
                        if (response.code == 200) {
                            ToastUtils.show(mContext, "收藏成功");
                            tv_collect.setText("已收藏");
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });

    }


}
