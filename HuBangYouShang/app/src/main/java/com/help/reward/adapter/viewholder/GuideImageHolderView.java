package com.help.reward.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.help.reward.utils.GlideUtils;

/**
 * Created by wuxiaojun on 17-8-4.
 */

public class GuideImageHolderView implements Holder<Integer> {

    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, Integer data) {
        GlideUtils.loadLocalImage(data,imageView);
    }
}
