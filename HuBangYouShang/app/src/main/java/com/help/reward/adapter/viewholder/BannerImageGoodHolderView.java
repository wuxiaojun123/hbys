package com.help.reward.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.help.reward.utils.GlideUtils;

/**
 * 商品详情页banner
 * Created by wuxiaojun on 17-3-9.
 */

public class BannerImageGoodHolderView implements Holder<String> {

    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, final int position, String data) {
        GlideUtils.loadImage(data, imageView);
    }

}

