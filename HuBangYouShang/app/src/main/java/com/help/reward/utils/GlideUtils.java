package com.help.reward.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.help.reward.R;

/**
 * Created by wuxiaojun on 17-3-2.
 */

public class GlideUtils {


    public static void loadImage(String imgUrl, ImageView imageView) {
        if (!TextUtils.isEmpty(imgUrl)) {
            Glide.with(imageView.getContext())
                    .load(imgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .error(R.mipmap.img_default)
                    .placeholder(R.mipmap.img_default)
                    .centerCrop()
                    .crossFade()
                    .into(imageView);
        }
    }

    public static void loadGIFImage(Context context, String imgUrl, ImageView imageView) {
        if (!TextUtils.isEmpty(imgUrl)) {
            Glide.clear(imageView);
            imageView.setImageDrawable(null);
            Glide.with(context)
                    .load(imgUrl)
                    .asBitmap()
                    .centerCrop()
//                    .placeholder(R.mipmap.img_default)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        }
    }

    public static void loadCircleImage(String imgUrl, ImageView imageView) {
        if (!TextUtils.isEmpty(imgUrl)) {
            Glide.with(imageView.getContext())
                    .load(imgUrl)
                    .transform(new GlideCircleTransform(imageView.getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .error(R.mipmap.img_default)
                    .placeholder(R.mipmap.img_default)
                    .into(imageView);
        }
    }


}
