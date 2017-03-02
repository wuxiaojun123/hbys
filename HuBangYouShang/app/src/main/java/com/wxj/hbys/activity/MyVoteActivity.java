package com.wxj.hbys.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxj.hbys.R;
import com.wxj.hbys.utils.ActivitySlideAnim;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的投票
 * item_vote
 * Created by wuxiaojun on 2017/2/11.
 */
public class MyVoteActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vote);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_title.setText(R.string.string_my_vote_title);
        tv_title_right.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_title_back})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(MyVoteActivity.this);

                break;
        }
    }


}
