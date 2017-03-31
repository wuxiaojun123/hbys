package com.help.reward.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.view.FluidLayout;
import com.help.reward.view.SearchEditTextView;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 搜索商品页面
 * Created by wuxiaojun on 17-3-31.
 */

public class SearchShopActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.et_search)
    SearchEditTextView et_search; // 搜索
    @BindView(R.id.tv_search_history)
    TextView tv_search_history;
    @BindView(R.id.fl_history)
    FluidLayout fl_history; // 历史记录的viewgroup
    @BindView(R.id.tv_search_hot)
    TextView tv_search_hot;
    @BindView(R.id.fl_hot)
    FluidLayout fl_hot; // 热门搜索的viewgroup

    private int colorTextBg;
    private String[] tags = new String[]{
            "倩女幽魂", "单机斗地主", "天堂战记", "妖精的尾巴", "极限挑战", "我们相爱吧", "倚天屠龙记"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shop);
        ButterKnife.bind(this);
        colorTextBg = getResources().getColor(R.color.color_3a);
        setHistory();
        et_search.setOnKeyListener(new SearchEditTextView.onKeyListener() {
            @Override
            public void onKey() {
                search();
            }
        });
    }

    /***
     * 点击搜索跳往搜索结果页面
     */
    private void search() {
        String searchStr = et_search.getText().toString().trim();
        if(TextUtils.isEmpty(searchStr)){
            ToastUtils.show(mContext,"请输入搜索内容");
            return;
        }
        Intent mIntent = new Intent(this,SearchShopResultActivity.class);
        mIntent.putExtra("search_test",searchStr);
        startActivity(mIntent);
        ActivitySlideAnim.slideOutAnim(SearchShopActivity.this);
    }

    private void setHistory() {
        fl_history.removeAllViews();
        fl_history.setGravity(Gravity.CENTER);
        for (int i = 0; i < tags.length; i++) {
            TextView tv = new TextView(this);
            tv.setText(tags[i]);
            tv.setTextSize(13);
//            tv.setHeight(100);
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundResource(R.drawable.bg_search_shop_fluid_text);

            tv.setTextColor(colorTextBg); //

            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(12, 12, 12, 12);
            fl_history.addView(tv, params);
        }
    }

    @OnClick({R.id.tv_text, R.id.iv_search_type, R.id.iv_search})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_text:
                finish();
                ActivitySlideAnim.slideOutAnim(SearchShopActivity.this);

                break;
            case R.id.iv_search_type:
                // 点击搜索出现下拉框，商品或者店铺

                break;
            case R.id.iv_search:
                // 点击搜索


                break;


        }

    }


}
