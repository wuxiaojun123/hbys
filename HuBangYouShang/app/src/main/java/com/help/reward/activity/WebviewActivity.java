package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wuxiaojun on 17-8-17.
 */

public class WebviewActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.id_webview)
    WebView id_webview;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        id_webview.getSettings().setJavaScriptEnabled(true);
        id_webview.setWebChromeClient(new WebChromeClient());
        id_webview.setWebViewClient(new WebViewClient());

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        tv_title.setText(title);
        tv_title_right.setVisibility(View.GONE);
        String url = intent.getStringExtra("url");
        id_webview.loadUrl(Constant.BASE_URL + url);
    }

    @OnClick({R.id.iv_title_back})
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(WebviewActivity.this);

                break;
        }
    }


}
