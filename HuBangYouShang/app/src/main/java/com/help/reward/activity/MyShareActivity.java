package com.help.reward.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.EncodingUtils;
import com.idotools.utils.MetricsUtils;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的分享
 * Created by wuxiaojun on 2017/2/11.
 */
public class MyShareActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.iv_zxing_code)
    ImageView iv_zxing_code;

    private Bitmap mBitmap;
    private int imageWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_share);
        ButterKnife.bind(this);
        initView();

        imageWidth = MetricsUtils.dipToPx(192.0f);
        mBitmap = EncodingUtils.createQRCode("我的二维码:测试使用",
                imageWidth, imageWidth, null);

        iv_zxing_code.setImageBitmap(mBitmap);
    }

    private void initView() {
        tv_title.setText(R.string.string_my_share_title);
        tv_title_right.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_title_back, R.id.btn_share})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(MyShareActivity.this);

                break;
            case R.id.btn_share:
                // 点击分享
                shareText(mContext, "发现一个好应用，你也来下载吧！");

                break;
        }
    }

    public static void shareText(Context context, String content) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    @Override
    protected void onDestroy() {
        if(mBitmap != null && !mBitmap.isRecycled()){
            mBitmap.recycle();
            mBitmap = null;
        }
        super.onDestroy();
    }
}
