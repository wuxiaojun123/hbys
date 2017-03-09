package com.help.reward.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
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


                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
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
    }


}
