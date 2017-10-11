package com.help.reward.adapter;

import android.content.Context;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.Response.HelpPeopleNumberResponse;
import com.idotools.utils.DateUtil;

import java.util.List;

/**
 * Created by MJJ on 2015/7/25.
 */
public class HelpPeopleNumberAdapter extends BaseRecyclerAdapter<HelpPeopleNumberResponse.HelpPeopleNumberBean> {

    public String number;
//    private List<HelpPeopleNumberResponse.HelpPeopleNumberBean> mDatas = null;

    public HelpPeopleNumberAdapter(Context context) {
        super(context);
    }

//    @Override
//    public int getItemCount() {
//        return mDatas.size();
//    }


    @Override
    public int getLayoutId() {
        return R.layout.item_help_people_number;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TextView tv_title = (TextView) holder.itemView.findViewById(R.id.tv_title);
        TextView tv_body = (TextView) holder.itemView.findViewById(R.id.tv_body);
        TextView tv_time = (TextView) holder.itemView.findViewById(R.id.tv_time);


        HelpPeopleNumberResponse.HelpPeopleNumberBean bean = mDataList.get(position);

        double points = Double.parseDouble(bean.points);
        if (points > 0) {
            tv_title.setText(bean.desc + number + "增加" + points);
        } else {
            tv_title.setText(bean.desc + number + "减少" + points);
        }
        tv_body.setText(bean.posttitle);
        tv_time.setText(DateUtil.getDateToString(bean.addtime + ""));

    }

}

