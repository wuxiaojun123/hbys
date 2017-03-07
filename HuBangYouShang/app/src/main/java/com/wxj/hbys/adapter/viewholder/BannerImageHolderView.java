package com.wxj.hbys.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.wxj.hbys.bean.ShopBannerBean;
import com.wxj.hbys.utils.GlideUtils;

/**
 * Created by wuxiaojun on 2017/3/7.
 */

public class BannerImageHolderView implements Holder<ShopBannerBean> {

    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, final int position, ShopBannerBean data) {
        GlideUtils.loadImage(data.adv_pic, imageView);
    }

}
