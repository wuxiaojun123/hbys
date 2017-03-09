package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.Adapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MXY on 2017/2/18.
 */

public class PostActivity extends BaseActivity implements Adapter.IonSlidingViewClickListener{

    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.rv_post)
    RecyclerView rvPost;

    private Adapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        tvTitle.setText("帖子动态");
        tvTitleRight.setText("清空");
        setAdapter();
    }

    private void setAdapter(){
        rvPost.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this);
        rvPost.setAdapter(adapter);
        rvPost.setItemAnimator(new DefaultItemAnimator());
    }
    @OnClick({R.id.iv_title_back,R.id.tv_title_right})
    public void onCLick(View view){

        switch (view.getId()){
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_right:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onDeleteBtnCilck(View view, int position) {
        adapter.removeData(position);
    }
}
