package com.help.reward.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.help.reward.R;
import com.help.reward.activity.HelpInfoActivity;
import com.help.reward.adapter.HelpVoteAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * 互帮-获赏
 * Created by MXY on 2017/2/19.
 */

public class HelpVoteFragment extends BaseFragment {

    @BindView(R.id.lv_fragment_help3)
    ListView lvFragmentHelp3;
    private View contentView;
    private HelpVoteAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_help3, null);
        }
        ButterKnife.bind(this, contentView);
        initData();
        return contentView;
    }

    private void initData() {
        List<String> list = new ArrayList<String>();
        for(int i = 0;i<10;i++){
            list.add(i+"");
        }
        adapter = new HelpVoteAdapter(mContext,list,R.layout.item_help1);
        lvFragmentHelp3.setAdapter(adapter);
    }

    @OnItemClick(R.id.lv_fragment_help3)
    void itemCick(int position){
        startActivity(new Intent(mContext, HelpInfoActivity.class));
//        startActivity(new Intent(mContext, PostActivity.class));

    }
}
