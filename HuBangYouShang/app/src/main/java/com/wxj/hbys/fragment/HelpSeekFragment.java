package com.wxj.hbys.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.wxj.hbys.R;
import com.wxj.hbys.activity.HelpInfoActivity;
import com.wxj.hbys.adapter.HelpSeekAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * 互帮-求助
 * Created by MXY on 2017/2/19.
 */

public class HelpSeekFragment extends BaseFragment {

    @BindView(R.id.lv_fragment_help1)
    ListView lvFragmentHelp1;
    private View contentView;
    private HelpSeekAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_help1, null);
        }
        ButterKnife.bind(this, contentView);
        initData();
        return contentView;
    }
    private void initData(){
        List<String> list = new ArrayList<String>();
        for(int i = 0;i<10;i++){
            list.add(i+"");
        }
        adapter = new HelpSeekAdapter(mContext,list,R.layout.item_help1);
        lvFragmentHelp1.setAdapter(adapter);
    }

    @OnItemClick(R.id.lv_fragment_help1)
    void itemCick(int position){
        startActivity(new Intent(mContext, HelpInfoActivity.class));
//        startActivity(new Intent(mContext, PostActivity.class));

    }
}
