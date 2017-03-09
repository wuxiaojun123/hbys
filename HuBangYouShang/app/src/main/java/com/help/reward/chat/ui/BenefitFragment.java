package com.help.reward.chat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.help.reward.R;
import com.help.reward.chat.adapter.GroupTypesAdapter;
import com.help.reward.fragment.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页-获益
 * Created by wuxiaojun on 2017/1/8.
 */

public class BenefitFragment extends BaseFragment {

    protected List<EMGroup> grouplist;
    private View contentView;

    @BindView(R.id.list)
    LRecyclerView groupListView;

    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;

    private GroupTypesAdapter groupAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(contentView == null){
            contentView = inflater.inflate(R.layout.fragment_benefit,null);
        }
        ButterKnife.bind(this, contentView);

        tvTitle.setText("聊天");
        ivTitleBack.setVisibility(View.GONE);
        tvTitleRight.setVisibility(View.GONE);



        grouplist = EMClient.getInstance().groupManager().getAllGroups();

        groupListView.setLayoutManager(new LinearLayoutManager(mContext));
        groupAdapter = new GroupTypesAdapter(this.getActivity(), 1, 1);
        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(groupAdapter);
        groupListView.setAdapter(mLRecyclerViewAdapter);
        groupAdapter.setDataList(grouplist);
        return contentView;
    }

}
