package com.help.reward.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.activity.HelpSeekInfoActivity;
import com.help.reward.activity.ShowBigImageActivity;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.GoodEvaluateBean;
import com.help.reward.utils.DisplayUtil;
import com.help.reward.utils.GlideUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ADBrian on 06/04/2017.
 */

public class GoodEvaluateAdapter extends BaseRecyclerAdapter<GoodEvaluateBean> {

    private static int LINE_NUMBERS = 3;
//    private List<ImageView> lsImageView = new ArrayList<>();
    private SparseArray<String[]> viewSparseArray = new SparseArray<>();

    public GoodEvaluateAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_good_evaluate;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ImageView mIvPhoto = (ImageView) holder.itemView.findViewById(R.id.iv_good_evaluate_photo);
        TextView mTvName = (TextView) holder.itemView.findViewById(R.id.tv_good_evaluate_name);
        TextView mTvDate = (TextView) holder.itemView.findViewById(R.id.tv_good_evaluate_date);
        TextView mTvProp = (TextView) holder.itemView.findViewById(R.id.tv_good_evaluate_prop); // 尺码：S 颜色：黑色
        TextView mTvContent = (TextView) holder.itemView.findViewById(R.id.tv_evaluate_content);

        LinearLayout mLayout = (LinearLayout) holder.itemView.findViewById(R.id.ll_img_content);
        ImageView iv_image1 = holder.getView(R.id.iv_image1);
        ImageView iv_image2 = holder.getView(R.id.iv_image2);
        ImageView iv_image3 = holder.getView(R.id.iv_image3);
        ImageView iv_image4 = holder.getView(R.id.iv_image4);

        GoodEvaluateBean goodEvaluate = mDataList.get(position);
        GlideUtils.loadImage(goodEvaluate.member_avatar, mIvPhoto);
        mTvName.setText(goodEvaluate.geval_frommembername);
        mTvDate.setText(goodEvaluate.geval_addtime_date);
        mTvContent.setText(goodEvaluate.geval_content);


        String[] geval_image_240Array = goodEvaluate.geval_image_240;
        if (geval_image_240Array != null && geval_image_240Array.length > 0) {
            switch (geval_image_240Array.length) {
                case 4:
                    iv_image4.setVisibility(View.VISIBLE);
                    iv_image4.setOnClickListener(new onShowBigeImageCick(geval_image_240Array[3]));
                    GlideUtils.loadImage(geval_image_240Array[3], iv_image4);
                case 3:
                    iv_image3.setVisibility(View.VISIBLE);
                    iv_image3.setOnClickListener(new onShowBigeImageCick(geval_image_240Array[2]));
                    GlideUtils.loadImage(geval_image_240Array[2], iv_image3);
                case 2:
                    iv_image2.setVisibility(View.VISIBLE);
                    iv_image2.setOnClickListener(new onShowBigeImageCick(geval_image_240Array[1]));
                    GlideUtils.loadImage(geval_image_240Array[1], iv_image2);
                case 1:
                    iv_image1.setVisibility(View.VISIBLE);
                    iv_image1.setOnClickListener(new onShowBigeImageCick(geval_image_240Array[0]));
                    GlideUtils.loadImage(geval_image_240Array[0], iv_image1);
                    break;
            }
            viewSparseArray.put(position, geval_image_240Array);
        }

        if (viewSparseArray.get(position) != null && geval_image_240Array != null && geval_image_240Array.length > 0) {
            String[] geval_image_240 = viewSparseArray.get(position);
            switch (geval_image_240.length) {
                case 4:
                    iv_image4.setVisibility(View.VISIBLE);
                    iv_image4.setOnClickListener(new onShowBigeImageCick(geval_image_240[3]));
                    GlideUtils.loadImage(geval_image_240[3], iv_image4);
                case 3:
                    iv_image3.setVisibility(View.VISIBLE);
                    iv_image3.setOnClickListener(new onShowBigeImageCick(geval_image_240[2]));
                    GlideUtils.loadImage(geval_image_240[2], iv_image3);
                case 2:
                    iv_image2.setVisibility(View.VISIBLE);
                    iv_image2.setOnClickListener(new onShowBigeImageCick(geval_image_240[1]));
                    GlideUtils.loadImage(geval_image_240[1], iv_image2);
                case 1:
                    iv_image1.setVisibility(View.VISIBLE);
                    iv_image1.setOnClickListener(new onShowBigeImageCick(geval_image_240[0]));
                    GlideUtils.loadImage(geval_image_240[0], iv_image1);
                    break;
            }
        } else {
            switch (4) {
                case 4:
                    iv_image4.setVisibility(View.GONE);
                case 3:
                    iv_image3.setVisibility(View.GONE);
                case 2:
                    iv_image2.setVisibility(View.GONE);
                case 1:
                    iv_image1.setVisibility(View.GONE);
                    break;
            }
        }

        /*String[] geval_image_240 = goodEvaluate.geval_image_240;
        if (geval_image_240 != null && geval_image_240.length > 0) {
            int total = geval_image_240.length / LINE_NUMBERS;
            int MaxLength = geval_image_240.length > 6 ? 6 : geval_image_240.length;
            if (mLayout.getTag() == null && viewSparseArray.get(position) == null) {
                addImageview(position, mLayout, goodEvaluate, geval_image_240, total, MaxLength);
            }
        }*/

    }

    private class onShowBigeImageCick implements View.OnClickListener {
        String url;

        private onShowBigeImageCick(String url) {
            this.url = url;
        }

        @Override
        public void onClick(View view) {
            showBigImage(url);
        }
    }

    private void showBigImage(String url) {
        Intent intent = new Intent(mContext, ShowBigImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("imageUrl", url);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }


    private void addImageview(int position, LinearLayout mLayout, GoodEvaluateBean goodEvaluate,
                              String[] geval_image_240, int total, int maxLength) {
        int i = 0;
        if (total > 1) {
            LinearLayout ll = new LinearLayout(mContext);
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.setLayoutParams(lp1);

            for (; i < LINE_NUMBERS; i++) {
                ImageView iv = new ImageView(mContext);
                //iv.setImageResource(R.mipmap.img_default);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DisplayUtil.dip2px(mContext, 90),
                        DisplayUtil.dip2px(mContext, 90));
                lp.leftMargin = 10;
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                //iv.setLayoutParams(lp);
                ll.addView(iv);
                GlideUtils.loadImage(geval_image_240[i], iv);
            }
            mLayout.addView(ll);
        }

        LinearLayout ll2 = new LinearLayout(mContext);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        ll2.setLayoutParams(lp2);

        for (; i < maxLength; i++) {
            ImageView iv = new ImageView(mContext);
            //iv.setImageResource(R.mipmap.img_default);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DisplayUtil.dip2px(mContext, 90),
                    DisplayUtil.dip2px(mContext, 90));
            lp.leftMargin = 10;
            iv.setLayoutParams(lp);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            ll2.addView(iv);
            if (mLayout.getChildCount() > 0) {
                mLayout.removeAllViews();
            }
            mLayout.addView(ll2);
            GlideUtils.loadImage(geval_image_240[i], iv);
        }
        mLayout.setTag(goodEvaluate.geval_frommemberid);
    }

}
