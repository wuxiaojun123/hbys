package com.help.reward.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.help.reward.activity.GoodInfoActivity;
import com.help.reward.adapter.ShopHotAdapter;
import com.help.reward.bean.GoodsInfoBean;
import com.help.reward.bean.ShopInfoRecommendBean;
import com.help.reward.bean.ShopMallHotBean;
import com.help.reward.bean.StoreInfoBean;
import com.help.reward.utils.ActivitySlideAnim;
import com.idotools.utils.ToastUtils;
import com.help.reward.R;
import com.help.reward.adapter.viewholder.BannerImageGoodHolderView;
import com.help.reward.bean.Response.GoodResponse;
import com.help.reward.network.ShopMallNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MXY on 2017/2/26.
 */

public class GoodFragment extends BaseFragment {

    @BindView(R.id.cb_goods_img)
    ConvenientBanner cb_goods_img; // 商品图片
    @BindView(R.id.tv_goodinfo_goodname)
    TextView tv_goodinfo_goodname; // 商品名称
    @BindView(R.id.tv_goodinfo_goodprice)
    TextView tv_goodinfo_goodprice; // 售价
    @BindView(R.id.tv_goodinfo_goodppost)
    TextView tv_goodinfo_goodppost; // 是否包邮
    @BindView(R.id.tv_goodinfo_oldprice)
    TextView tv_goodinfo_oldprice; // 原价
    @BindView(R.id.tv_goodinfo_salecount)
    TextView tv_goodinfo_salecount;// 多少人已付款
    @BindView(R.id.tv_goodinfo_address)
    TextView tv_goodinfo_address; // 地址
    @BindView(R.id.layout_goodinfo_youhuiquan)
    RelativeLayout layout_goodinfo_youhuiquan; //优惠劵交易
    @BindView(R.id.layout_goodinfo_xuanze)
    RelativeLayout layout_goodinfo_xuanze;// 颜色分类尺码
    @BindView(R.id.tv_goods_store_name)
    TextView tv_goods_store_name; // 店铺名称
    @BindView(R.id.tv_goodinfo_desccredit)
    TextView tv_goodinfo_desccredit; // 描述相符
    @BindView(R.id.tv_goodinfo_servicecredit)
    TextView tv_goodinfo_servicecredit; // 服务相符
    @BindView(R.id.tv_goodinfo_deliverycredit)
    TextView tv_goodinfo_deliverycredit; // 发货速度
    @BindView(R.id.gv_shopinfo_recommand)
    MyGridView gv_shopinfo_recommand; // 商品推荐

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_good;
    }

    @Override
    protected void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String goodsId = bundle.getString("goods_id");
            if (!TextUtils.isEmpty(goodsId)) {
                initNetwork(goodsId);
            }
        }
    }

    private void initNetwork(String goodsId) {
        ShopMallNetwork
                .getShopMallMainApi()
                .getGoodResponse("goods", "goods_detail", goodsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<GoodResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(GoodResponse response) {
                        if (response.code == 200) {
                            if (response.data != null) { // 获取数据
                                initGoodsImage(response.data.goods_image);
                                initGoodsInfo(response.data.goods_info);
                                initStoreInfo(response.data.store_info);
                                initHotShop(response.data.goods_commend_list);

                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private void initHotShop(final List<ShopMallHotBean> hot_goods_list) {
        ShopHotAdapter mShopHotAdapter = new ShopHotAdapter(mContext, hot_goods_list, R.layout.item_hot_shop);
        gv_shopinfo_recommand.setAdapter(mShopHotAdapter);
        gv_shopinfo_recommand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(mContext, GoodInfoActivity.class);
                mIntent.putExtra("goods_id", hot_goods_list.get(position).goods_id);
                startActivity(mIntent);
                ActivitySlideAnim.slideInAnim(getActivity());
            }
        });
    }

    private void initStoreInfo(StoreInfoBean store_info) {
        if (store_info != null) {
            tv_goods_store_name.setText(store_info.store_name);
            if (store_info.store_desccredit != null) {
                tv_goodinfo_desccredit.setText(store_info.store_desccredit.text);
            }
            if (store_info.store_deliverycredit != null) {
                tv_goodinfo_deliverycredit.setText(store_info.store_deliverycredit.text);
            }
            if (store_info.store_servicecredit != null) {
                tv_goodinfo_servicecredit.setText(store_info.store_servicecredit.text);
            }
        }
    }

    private void initGoodsInfo(GoodsInfoBean goods_info) {
        if (goods_info != null) {
            tv_goodinfo_goodname.setText(goods_info.goods_name);
            tv_goodinfo_goodprice.setText("¥ " + goods_info.goods_price);
            tv_goodinfo_oldprice.setText("¥ " + goods_info.goods_marketprice);
            tv_goodinfo_salecount.setText(goods_info.goods_salenum + "人已付款");
        }
    }

    private void initGoodsImage(String goods_image) {
        if (!TextUtils.isEmpty(goods_image)) {
            String[] imgArgs = goods_image.split(",");
            List<String> goodsImageList = new ArrayList<>();
            for (int i = 0; i < imgArgs.length; i++) {
                goodsImageList.add(imgArgs[i]);
            }
            cb_goods_img.setPages(new CBViewHolderCreator() {
                @Override
                public Object createHolder() {
                    return new BannerImageGoodHolderView();
                }
            }, goodsImageList)
                    .setPageIndicator(new int[]{R.mipmap.img_ic_page_indicator, R.mipmap.img_ic_page_indicator_focus})
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
            .startTurning(10000);

        }

        cb_goods_img.setFocusable(true);
        cb_goods_img.setFocusableInTouchMode(true);
        cb_goods_img.requestFocus();
    }


}
