package com.help.reward.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.HelpSeekBean;
import com.idotools.utils.DateUtil;

/**
 * Created by MXY on 2017/2/19.
 */

public class HelpSeekAdapter extends BaseRecyclerAdapter<HelpSeekBean> {

    public HelpSeekAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_help1;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        HelpSeekBean item = mDataList.get(position);

        TextView item_help1_title = holder.getView(R.id.item_help1_title);
        item_help1_title.setText(item.title);
        TextView item_help1_type = holder.getView(R.id.item_help1_type);
        item_help1_type.setText(item.board_name);
        TextView item_help1_date = holder.getView(R.id.item_help1_date);
        item_help1_date.setText(DateUtil.getDateToString(item.create_time*1000));
        TextView item_help1_score = holder.getView(R.id.item_help1_score);
        item_help1_score.setText("悬赏 " + item.offer);
        TextView item_help1_count = holder.getView(R.id.item_help1_count);
        item_help1_count.setVisibility(View.GONE);
        TextView item_help1_des = holder.getView(R.id.item_help1_des);
        item_help1_des.setText(item.u_name);

    }
}