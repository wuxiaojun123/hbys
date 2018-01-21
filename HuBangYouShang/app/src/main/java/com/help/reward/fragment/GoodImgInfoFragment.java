package com.help.reward.fragment;

import android.content.Context;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.GoodResponse;
import com.help.reward.network.ShopMallNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MXY on 2017/2/26.
 */

public class GoodImgInfoFragment extends BaseFragment {

    private View contentView;

    @BindView(R.id.wb_load)
    WebView webView;

    @BindView(R.id.tv_empty)
    TextView mTvEmpty;

    private WebChromeClient chromeClient;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goodinfo_img;
    }


    @Override
    protected void init() {
        initView();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String goodsId = bundle.getString("goods_id");
            if (!TextUtils.isEmpty(goodsId)) {
                initNetwork(goodsId);
            }
        }
    }

    private void initView() {

        WebSettings setting = webView.getSettings();
        setting.setSupportZoom(true);
        setting.setDisplayZoomControls(false);
        setting.setBuiltInZoomControls(false);
//        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setting.setUseWideViewPort(true);
        setting.setDatabaseEnabled(true);
        setting.setDomStorageEnabled(true);
        setting.setAppCacheMaxSize(1024 * 1024 * 8);// 设置缓冲大小10M
        //String dir = this.getDir("database", Context.MODE_PRIVATE).getPath();
        //setting.setDatabasePath(dir);
        setting.setLoadsImagesAutomatically(true);
        setting.setLoadWithOverviewMode(true);
        setting.setGeolocationEnabled(true);
        setting.setRenderPriority(WebSettings.RenderPriority.LOW);
        setting.setEnableSmoothTransition(true);
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        setting.setAppCacheEnabled(true);
        setting.setPluginState(WebSettings.PluginState.ON);
        // setting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        setting.setJavaScriptEnabled(true);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);

        chromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                // TODO Auto-generated method stub
                super.onReceivedTitle(view, title);
                //titleTV.setText(title);
            }
        };
        webView.setWebChromeClient(chromeClient);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // 错误处理
                try {
                    webView.stopLoading();
                } catch (Exception e) {
                }
                try {
                    webView.clearView();
                } catch (Exception e) {
                }
                if (webView.canGoBack()) {
                    webView.goBack();
                }
                // super.onReceivedError(view, errorCode, description,
                // failingUrl);
                webView.setVisibility(View.GONE);
                mTvEmpty.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                // TODO Auto-generated method stub
                handler.proceed();
                // super.onReceivedSslError(view, handler, error);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view,
                                                    final String murl) {
                // TODO Auto-generated method stub
                webView.loadUrl(murl);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String murl) {
                super.onPageFinished(view, murl);
            }
        });
        //webView.loadUrl("http://www.baidu.com");

    }


    private void initNetwork(String goodsId) {
        ShopMallNetwork
                .getShopMallMainApi()
                .getGoodDetailsImgResponse("goods", "goods_body_info", goodsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.code == 200) {
                            if (response.data != null) { // 获取数据
                                String url = (String) response.data;
                                if (!TextUtils.isEmpty(url)) {
                                    if (!url.contains("http")) {
                                        url = "http://" + url;
                                    }
                                    LogUtils.e("请求的url是:"+url);
                                    webView.loadUrl(url);
                                    webView.setVisibility(View.VISIBLE);
                                    mTvEmpty.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        if(webView != null){
            ViewGroup viewGroup = (ViewGroup) webView.getParent();
            if(viewGroup != null){
                viewGroup.removeView(webView);
            }
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
