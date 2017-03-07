package com.wxj.hbys.chat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.wxj.hbys.R;
import com.wxj.hbys.adapter.MyHelpCommentAdapter;
import com.wxj.hbys.chat.adapter.GroupAdapter;
import com.wxj.hbys.fragment.BaseFragment;

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
    private GroupAdapter groupAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(contentView == null){
            contentView = inflater.inflate(R.layout.fragment_benefit,null);
        }
        ButterKnife.bind(this, contentView);
        grouplist = EMClient.getInstance().groupManager().getAllGroups();



        groupListView.setLayoutManager(new LinearLayoutManager(mContext));
        groupAdapter = new GroupAdapter(this.getActivity(), 1, 1);
        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(groupAdapter);
        groupListView.setAdapter(mLRecyclerViewAdapter);
        groupAdapter.setDataList(grouplist);
        return contentView;
    }

}
