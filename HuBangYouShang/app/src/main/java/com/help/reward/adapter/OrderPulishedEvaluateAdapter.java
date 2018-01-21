package com.help.reward.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.OrderPulishedEvaluateBean;
import com.help.reward.utils.GlideUtils;
import com.idotools.utils.LogUtils;

import butterknife.BindView;

/**
 * Created by wuxiaojun on 2017/4/7.
 */

public class OrderPulishedEvaluateAdapter extends BaseRecyclerAdapter {


    public OrderPulishedEvaluateAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_order_pulished_evaluate;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        ImageView iv_goods_img = holder.getView(R.id.iv_goods_img);
        TextView tv_goods_name = holder.getView(R.id.tv_goods_name);
        EditText et_content = holder.getView(R.id.et_content);
        RatingBar id_rating_bar = holder.getView(R.id.id_rating_bar);

        ImageView ivReleaseAddphoto = holder.getView(R.id.iv_release_addphoto); // 点击添加图片
        TextView tv_photonum = holder.getView(R.id.tv_photonum);
        ImageView iv_photo1 = holder.getView(R.id.iv_photo1);
        ImageView iv_photo2 = holder.getView(R.id.iv_photo2);
        ImageView iv_photo3 = holder.getView(R.id.iv_photo3);
        ImageView iv_photo4 = holder.getView(R.id.iv_photo4);
        ImageView iv_delete1 = holder.getView(R.id.iv_delete1);
        ImageView iv_delete2 = holder.getView(R.id.iv_delete2);
        ImageView iv_delete3 = holder.getView(R.id.iv_delete3);
        ImageView iv_delete4 = holder.getView(R.id.iv_delete4);

        final OrderPulishedEvaluateBean bean = (OrderPulishedEvaluateBean) mDataList.get(position);
        GlideUtils.loadImage(bean.goods_img, iv_goods_img);
        tv_goods_name.setText(bean.goods_name);
        et_content.addTextChangedListener(new MyTextWatch(position, bean));
        et_content.setText(bean.comment);
        if (bean.score != null) {
            id_rating_bar.setRating(Float.parseFloat(bean.score));
        }
        setOnRatingBarChangeListener(position, id_rating_bar, bean);
        ivReleaseAddphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAddPhotoListener != null) {
                    mOnAddPhotoListener.onItemAddPhotoListener(position);
                }
            }
        });
        iv_delete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        showPhoto(ivReleaseAddphoto, tv_photonum, iv_photo1, iv_photo2, iv_photo3,
                iv_photo4, iv_delete1, iv_delete2, iv_delete3, iv_delete4,bean);
    }

    private void showPhoto(ImageView ivReleaseAddphoto, TextView tv_photonum, ImageView iv_photo1, ImageView iv_photo2,
                           ImageView iv_photo3, ImageView iv_photo4, ImageView iv_delete1, ImageView iv_delete2,
                           ImageView iv_delete3, ImageView iv_delete4,OrderPulishedEvaluateBean bean) {
        iv_photo1.setVisibility(View.GONE);
        iv_delete1.setVisibility(View.GONE);
        iv_photo2.setVisibility(View.GONE);
        iv_delete2.setVisibility(View.GONE);
        iv_photo3.setVisibility(View.GONE);
        iv_delete3.setVisibility(View.GONE);
        iv_photo4.setVisibility(View.GONE);
        iv_delete4.setVisibility(View.GONE);
        ivReleaseAddphoto.setVisibility(View.VISIBLE);


        int length = bean.evaluate_image.size();
        switch (length) {
            case 4:
                ivReleaseAddphoto.setVisibility(View.GONE);
                iv_photo4.setVisibility(View.VISIBLE);
                iv_delete4.setVisibility(View.VISIBLE);
                GlideUtils.loadImage(bean.evaluate_image.get(3), iv_photo4);
            case 3:
                iv_photo3.setVisibility(View.VISIBLE);
                iv_delete3.setVisibility(View.VISIBLE);
                GlideUtils.loadImage(bean.evaluate_image.get(2), iv_photo3);
            case 2:
                iv_photo2.setVisibility(View.VISIBLE);
                iv_delete2.setVisibility(View.VISIBLE);
                GlideUtils.loadImage(bean.evaluate_image.get(1), iv_photo2);
            case 1:
                iv_photo1.setVisibility(View.VISIBLE);
                iv_delete1.setVisibility(View.VISIBLE);
                GlideUtils.loadImage(bean.evaluate_image.get(0), iv_photo1);
                break;
        }
        tv_photonum.setText("还可上传（" + (4 - length) + "）张");
    }

    private void setOnRatingBarChangeListener(final int position, RatingBar id_rating_bar, final OrderPulishedEvaluateBean bean) {
        id_rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                bean.score = rating + "";
                mDataList.set(position, bean);
            }
        });
    }

    public OnAddPhotoListener mOnAddPhotoListener;

    public void setOnAddPhotoListener(OnAddPhotoListener mOnAddPhotoListener) {
        this.mOnAddPhotoListener = mOnAddPhotoListener;
    }

    public interface OnAddPhotoListener {
        void onItemAddPhotoListener(int position);
    }

    public void setPhotoImageView(int position,String file_name,String url){
        OrderPulishedEvaluateBean bean = (OrderPulishedEvaluateBean) mDataList.get(position);
        bean.evaluate_image.add(file_name);
//        bean.file_names.add(file_name);
        mDataList.set(position,bean);
        notifyDataSetChanged();
    }

    private class MyTextWatch implements TextWatcher {
        public int position;
        public OrderPulishedEvaluateBean bean;

        public MyTextWatch(int position, OrderPulishedEvaluateBean bean) {
            this.position = position;
            this.bean = bean;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null) {
                bean.comment = s.toString();
                mDataList.set(position, bean);
            }
        }
    }

}
