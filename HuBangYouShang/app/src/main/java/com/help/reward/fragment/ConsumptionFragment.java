package com.help.reward.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.help.reward.App;
import com.help.reward.activity.MyAccountActivity;
import com.help.reward.activity.MyCouponActivity;
import com.help.reward.activity.MyOrderActivity;
import com.help.reward.activity.SearchShopActivity;
import com.help.reward.activity.ShopcartActivity;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.UpdateMessageDataRxbusType;
import com.help.reward.view.SearchEditTextView;
import com.idotools.utils.InputWindowUtils;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;
import com.help.reward.R;
import com.help.reward.activity.GoodInfoActivity;
import com.help.reward.activity.GoodsTypeActivity;
import com.help.reward.activity.MsgCenterActivity;
import com.help.reward.activity.StoreInfoActivity;
import com.help.reward.adapter.ShopHotAdapter;
import com.help.reward.adapter.StoreRecommandAdapter;
import com.help.reward.adapter.viewholder.BannerImageHolderView;
import com.help.reward.bean.Response.ShopMallMainResponse;
import com.help.reward.bean.ShopBannerBean;
import com.help.reward.bean.ShopMallHotBean;
import com.help.reward.bean.ShopMallStoreBean;
import com.help.reward.network.ShopMallNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.GlideUtils;
import com.help.reward.view.MyGridView;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.help.reward.R.id.layout_help_title_sms;

/**
 * 首页-消费
 * Created by wuxiaojun on 2017/1/8.
 */

public class ConsumptionFragment extends BaseFragment {

    @BindView(R.id.tv_search)
    TextView tv_search; // 搜索框
    @BindView(R.id.banner_shop)
    ConvenientBanner bannerShop;
    //    @BindView(R.id.layout_shop_myaccount)
//    LinearLayout layoutShopMyaccount;
    @BindView(R.id.layout_shop_myorder)
    LinearLayout layoutShopMyorder;
    @BindView(R.id.layout_shop_coupon)
    LinearLayout layoutShopCoupon;
    @BindView(R.id.layout_shop_type)
    LinearLayout layoutShopType;
    @BindView(R.id.gv_shop_recommand)
    MyGridView gvShopRecommand;
    @BindView(R.id.banner_shop2)
    ConvenientBanner bannerShop2;
    @BindView(R.id.gv_shop_hot_more)
    MyGridView gvShopHotMore;
    // 推荐的5个商品
    @BindView(R.id.iv_hot_img)
    ImageView iv_hot_img;
    @BindView(R.id.tv_hot_title)
    TextView tv_hot_title;
    @BindView(R.id.tv_hot_info)
    TextView tv_hot_info;

    @BindView(R.id.iv_hot_img2)
    ImageView iv_hot_img2;
    @BindView(R.id.tv_hot_title2)
    TextView tv_hot_title2;
    @BindView(R.id.tv_hot_info2)
    TextView tv_hot_info2;

    @BindView(R.id.iv_hot_img3)
    ImageView iv_hot_img3;
    @BindView(R.id.tv_hot_title3)
    TextView tv_hot_title3;
    @BindView(R.id.tv_hot_info3)
    TextView tv_hot_info3;

    @BindView(R.id.iv_hot_img4)
    ImageView iv_hot_img4;
    @BindView(R.id.tv_hot_title4)
    TextView tv_hot_title4;
    @BindView(R.id.tv_hot_info4)
    TextView tv_hot_info4;

    @BindView(R.id.iv_hot_img5)
    ImageView iv_hot_img5;
    @BindView(R.id.tv_hot_title5)
    TextView tv_hot_title5;
    @BindView(R.id.tv_hot_info5)
    TextView tv_hot_info5;

    //    @BindView(R.id.et_search)
//    SearchEditTextView et_search;
    @BindView(R.id.iv_email)
    ImageView iv_email; // 消息中心
    @BindView(R.id.tv_title_help_msgcount)
    TextView tvTitleHelpMsgcount; // 消息红点

    StoreRecommandAdapter mStoreRecommandAdapter;
    ShopHotAdapter mShopHotAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_consumption;
    }

    @Override
    protected void init() {
        initView();
        initNetwork();
        updateData();
        if (App.mLoginReponse != null && App.mLoginReponse.new_message) {
            tvTitleHelpMsgcount.setVisibility(View.VISIBLE);
        } else {
            tvTitleHelpMsgcount.setVisibility(View.INVISIBLE);
        }
    }

    private void initView() {
        tv_search.setText(R.string.string_search_like_ad);
    }

    private void initNetwork() {
        ShopMallNetwork
                .getShopMallMainApi()
                .getShopMallMainResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ShopMallMainResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(ShopMallMainResponse response) {
                        if (response.code == 200) {
                            if (response.data != null) {
                                initHeadBannerShop(response.data.up_banner);
                                initHot5Shop(response.data.adv_list);
                                initStore(response.data.rec_store_list);
                                initMiddleBannerShop(response.data.mid_banner);
                                initHotShop(response.data.hot_goods_list);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    /***
     * 热门商品
     *
     * @param hot_goods_list
     */
    private void initHotShop(final List<ShopMallHotBean> hot_goods_list) {
        mShopHotAdapter = new ShopHotAdapter(mContext, hot_goods_list, R.layout.item_hot_shop);
        gvShopHotMore.setAdapter(mShopHotAdapter);
        gvShopHotMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.e("点击的position=" + position + "  good_id=" + hot_goods_list.get(position).goods_id);
                Intent mIntent = new Intent(mContext, GoodInfoActivity.class);
                mIntent.putExtra("goods_id", hot_goods_list.get(position).goods_id);
                mIntent.putExtra("store_id", hot_goods_list.get(position).store_id);
                startActivity(mIntent);
                ActivitySlideAnim.slideInAnim(getActivity());
            }
        });
    }

    /***
     * 推荐店铺
     *
     * @param rec_store_list
     */
    private void initStore(final List<ShopMallStoreBean> rec_store_list) {
        mStoreRecommandAdapter = new StoreRecommandAdapter(mContext, rec_store_list, R.layout.item_store_recommand);
        gvShopRecommand.setAdapter(mStoreRecommandAdapter);
        gvShopRecommand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToStoreInfoActivity(rec_store_list.get(position).store_id);
            }
        });
    }

    /***
     * 填充5个热门商品
     */
    private void initHot5Shop(List<ShopBannerBean> adv_list) {
        ShopBannerBean bean = adv_list.get(0);
        GlideUtils.loadImage(bean.adv_pic, iv_hot_img);
        tv_hot_title.setText(bean.adv_title);
        tv_hot_info.setText(bean.adv_sub_title);
        iv_hot_img.setOnClickListener(new HotImgClickListener(bean.store_id));

        ShopBannerBean bean2 = adv_list.get(1);
        GlideUtils.loadImage(bean2.adv_pic, iv_hot_img2);
        tv_hot_title2.setText(bean2.adv_title);
        tv_hot_info2.setText(bean2.adv_sub_title);
        iv_hot_img2.setOnClickListener(new HotImgClickListener(bean2.store_id));

        ShopBannerBean bean3 = adv_list.get(2);
        GlideUtils.loadImage(bean3.adv_pic, iv_hot_img3);
        tv_hot_title3.setText(bean3.adv_title);
        tv_hot_info3.setText(bean3.adv_sub_title);
        iv_hot_img3.setOnClickListener(new HotImgClickListener(bean3.store_id));

        ShopBannerBean bean4 = adv_list.get(3);
        GlideUtils.loadImage(bean4.adv_pic, iv_hot_img4);
        tv_hot_title4.setText(bean4.adv_title);
        tv_hot_info4.setText(bean4.adv_sub_title);
        iv_hot_img4.setOnClickListener(new HotImgClickListener(bean4.store_id));

        ShopBannerBean bean5 = adv_list.get(4);
        GlideUtils.loadImage(bean5.adv_pic, iv_hot_img5);
        tv_hot_title5.setText(bean5.adv_title);
        tv_hot_info5.setText(bean5.adv_sub_title);
        iv_hot_img5.setOnClickListener(new HotImgClickListener(bean5.store_id));
    }

    /***
     * 初始化顶部banner
     */
    private void initHeadBannerShop(List<ShopBannerBean> up_banner) {
        bannerShop.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new BannerImageHolderView();
            }
        }, up_banner)
                .setPageIndicator(new int[]{R.mipmap.img_ic_page_indicator, R.mipmap.img_ic_page_indicator_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .startTurning(3000);
        bannerShop.setFocusable(true);
        bannerShop.setFocusableInTouchMode(true);
        bannerShop.requestFocus();
    }

    /***
     * 初始化中部banner
     */
    private void initMiddleBannerShop(List<ShopBannerBean> mid_banner) {
        bannerShop2.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new BannerImageHolderView();
            }
        }, mid_banner).setPageIndicator(new int[]{R.mipmap.img_ic_page_indicator, R.mipmap.img_ic_page_indicator_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .startTurning(3000);
    }

    @OnClick({R.id.layout_shop_cart, R.id.layout_shop_myorder, R.id.layout_shop_coupon,
            R.id.layout_shop_type, R.id.rl_hot_shop, R.id.rl_hot_shop2, R.id.ll_hot_shop3,
            R.id.ll_hot_shop4, R.id.ll_hot_shop5, R.id.tv_search, R.id.iv_email})
    void click(View v) {
        switch (v.getId()) {
            case R.id.layout_shop_cart: // 购物车
                startActivity(new Intent(mContext, ShopcartActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;
            case R.id.layout_shop_myorder: // 我的订单
                startActivity(new Intent(mContext, MyOrderActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;
            case R.id.layout_shop_coupon: // 优惠劵
                startActivity(new Intent(mContext, MyCouponActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;
            case R.id.layout_shop_type: // 分类
                startActivity(new Intent(mContext, GoodsTypeActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());
                break;
            case R.id.iv_email: // 消息中心
                startActivity(new Intent(mContext, MsgCenterActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());
                break;
            case R.id.tv_search:
                goToSearchActivity();

                break;
        }
    }

    private void goToSearchActivity() {
        Intent mIntent = new Intent(mContext, SearchShopActivity.class);
        mContext.startActivity(mIntent);
        ActivitySlideAnim.slideInAnim(getActivity());
    }

    /***
     * 更新消息红点
     */
    private void updateData() {
        RxBus.getDefault().toObservable(UpdateMessageDataRxbusType.class).subscribe(new Action1<UpdateMessageDataRxbusType>() {
            @Override
            public void call(UpdateMessageDataRxbusType type) {
                updateNewMsg(type.hasNew);
            }
        });
    }

    private void updateNewMsg(boolean hasNew) {
        if (hasNew) { // 更新数据
            tvTitleHelpMsgcount.setVisibility(View.VISIBLE);
        } else {
            tvTitleHelpMsgcount.setVisibility(View.INVISIBLE);
        }
    }

    /***
     * 跳转到店铺 页面
     *
     * @param store_id
     */
    private void goToStoreInfoActivity(String store_id) {
        Intent intent = new Intent(mContext, StoreInfoActivity.class);
        intent.putExtra("store_id", store_id);
        startActivity(intent);
        ActivitySlideAnim.slideInAnim(getActivity());
    }

    private class HotImgClickListener implements View.OnClickListener {

        private String store_id;

        public HotImgClickListener(String store_id) {
            this.store_id = store_id;
        }

        @Override
        public void onClick(View v) {
            goToStoreInfoActivity(store_id);
        }

    }

}
