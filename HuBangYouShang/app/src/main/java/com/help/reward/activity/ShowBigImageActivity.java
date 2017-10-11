package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.help.reward.R;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.GlideUtils;
import com.hyphenate.easeui.widget.photoview.EasePhotoView;
import com.hyphenate.easeui.widget.photoview.PhotoViewAttacher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 查看大图
 * <p>
 * Created by wuxiaojun on 2017/1/8.
 */

public class ShowBigImageActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.xiangce_bg_img)
    RelativeLayout xiangce_bg_img;
    @BindView(R.id.xiangce_img)
    EasePhotoView xiangce_img;
    String imageUrl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showbigimage);
        ButterKnife.bind(this);
        imageUrl = getIntent().getExtras().getString("imageUrl");
        GlideUtils.loadImage(imageUrl, xiangce_img);
        xiangce_img.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                onBackPressed();
            }
        });
    }



    @OnClick({R.id.xiangce_bg_img})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.xiangce_bg_img:
                onBackPressed();
                break;
        }
    }

}
