package com.help.reward.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.activity.StoreInfoActivity;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.SearchStoreInfoBean;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.GlideUtils;

/**
 * 搜索的店铺
 * Created by wuxiaojun on 2017/2/26.
 */

public class SearchStoreAdapter extends BaseRecyclerAdapter<SearchStoreInfoBean> {
    private Activity activity;

    public SearchStoreAdapter(Activity context) {
        super(context);
        this.activity = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_my_collection_store;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        ImageView iv_store = holder.getView(R.id.iv_store);
        TextView tv_title = holder.getView(R.id.tv_title);
        TextView tv_describe = holder.getView(R.id.tv_describe);
        TextView tv_server = holder.getView(R.id.tv_server);
        TextView tv_logistics = holder.getView(R.id.tv_logistics);

        TextView tv_describe_title = holder.getView(R.id.tv_describe_title);
        TextView tv_server_title = holder.getView(R.id.tv_server_title);
        TextView tv_logistics_title = holder.getView(R.id.tv_logistics_title);
        TextView tv_collection = holder.getView(R.id.tv_collection);

        TextView tv_delete = holder.getView(R.id.tv_delete);
        tv_delete.setVisibility(View.GONE);
        View left_layout = holder.getView(R.id.id_ll_content);
        final SearchStoreInfoBean bean = mDataList.get(position);

        GlideUtils.loadImage(bean.store_avatar_url, iv_store);
        tv_title.setText(bean.store_name);
        tv_collection.setText(bean.store_collect + "人关注");
        tv_describe_title.setText(bean.store_evaluate_info.store_credit.store_desccredit.text);
        tv_describe.setText(bean.store_evaluate_info.store_credit.store_desccredit.credit);
        tv_server_title.setText(bean.store_evaluate_info.store_credit.store_servicecredit.text);
        tv_server.setText(bean.store_evaluate_info.store_credit.store_servicecredit.credit);
        tv_logistics_title.setText(bean.store_evaluate_info.store_credit.store_deliverycredit.text);
        tv_logistics.setText(bean.store_evaluate_info.store_credit.store_deliverycredit.credit);

        left_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StoreInfoActivity.class);
                intent.putExtra("store_id", bean.store_id);
                activity.startActivity(intent);
                ActivitySlideAnim.slideInAnim(activity);
            }
        });
    }


}
