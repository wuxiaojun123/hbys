package com.wxj.hbys.adapter;

import android.content.Context;
import android.widget.TextView;

import com.idotools.utils.DateUtil;
import com.wxj.hbys.R;
import com.wxj.hbys.adapter.viewholder.SuperViewHolder;
import com.wxj.hbys.bean.Response.MyRewardPostResponse;
import com.wxj.hbys.bean.Response.MyVoteResponse;

/**
 * 我的获赏--发帖
 * Created by wuxiaojun on 2017/2/26.
 */

public class MyVoteAdapter extends BaseRecyclerAdapter {

    public MyVoteAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_my_vote;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TextView tv_content = holder.getView(R.id.tv_content);
        TextView tv_title = holder.getView(R.id.tv_title);
        TextView tv_time = holder.getView(R.id.tv_time);
//        TextView tv_state = holder.getView(R.id.tv_state);
        TextView tv_help = holder.getView(R.id.tv_help);
        TextView tv_state = holder.getView(R.id.tv_state);

        MyVoteResponse bean = (MyVoteResponse) mDataList.get(position);

        tv_content.setText(bean.content);
        tv_title.setText(bean.post_board);
        tv_time.setText(DateUtil.getDateToString(Long.parseLong(bean.create_time)));
//        tv_state.setText(bean.status);
        tv_help.setText(bean.post_title);
        tv_state.setText(bean.status);

    }

}
