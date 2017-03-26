package com.help.reward.adapter;

import android.content.Context;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;

/**
 * Created by MXY on 2017/2/19.
 */

public class SearchHistoryAdapter extends BaseRecyclerAdapter<String> {

    public SearchHistoryAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_history;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        String title = mDataList.get(position);
        TextView tv_title = holder.getView(R.id.tv_title);
        tv_title.setText(title);

    }
}
