package com.help.reward.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.base.recyclerview.OnItemClickListener;
import com.idotools.utils.DateUtil;
import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.Response.MyHelpPostResponse;

/**
 * Created by wuxiaojun on 2017/2/26.
 */

public class MyHelpPostAdapter extends BaseRecyclerAdapter<MyHelpPostResponse.MyHelpPostBean> {

    public MyHelpPostAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_my_help_post;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TextView tv_content = holder.getView(R.id.tv_content);
        TextView tv_title = holder.getView(R.id.tv_title);
        TextView tv_time = holder.getView(R.id.tv_time);
        TextView tv_state = holder.getView(R.id.tv_state);
        TextView tv_post = holder.getView(R.id.tv_post);
        TextView tv_reward = holder.getView(R.id.tv_reward);

        MyHelpPostResponse.MyHelpPostBean bean = mDataList.get(position);

        tv_content.setText(bean.title);
        tv_title.setText(bean.board_name);
        tv_time.setText(DateUtil.getDateToString(bean.create_time));
        tv_state.setText(bean.status);
        tv_post.setText(bean.comment);
        tv_reward.setText(bean.offer);

    }

}
