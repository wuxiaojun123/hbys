package com.help.reward.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.activity.SearchShopResultActivity;
import com.help.reward.bean.GoodsSecondTypeBeans;
import com.help.reward.bean.GoodsTypeBean;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.view.MyGridView;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */

public class GoodsSecondTypeAdapter extends BaseAdapter {
    Activity mActivity;
    List<GoodsSecondTypeBeans> mDatas = new ArrayList<>();
    private LayoutInflater mInflater;

    public GoodsSecondTypeAdapter(Activity context) {
        this.mActivity = context;
        mInflater = LayoutInflater.from(context);

    }

    public void setmDatas(List<GoodsSecondTypeBeans> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder vh = null;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_goodssecondtype, null);
            vh = new ViewHolder();
            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            vh.gridView = (MyGridView) convertView.findViewById(R.id.myGridView);
            vh.title_layout = convertView.findViewById(R.id.title_layout);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final GoodsSecondTypeBeans item = mDatas.get(position);

        vh.tv_name.setText(item.gc_name);
        vh.gridView.setAdapter(new GirdViewAdapter(item.next_class_list));
        vh.title_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.show(mActivity, item.gc_name);
            }
        });
        vh.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) { // TODO Auto-generated method
                try {
                    ToastUtils.show(mActivity, item.next_class_list.get(arg2).gc_name);
                    Intent mIntent = new Intent(mActivity,SearchShopResultActivity.class);
                    mIntent.putExtra("keyword",item.next_class_list.get(arg2).gc_name);
                    mIntent.putExtra("searchType","goods");
                    mActivity.startActivity(mIntent);
                    ActivitySlideAnim.slideInAnim(mActivity);

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView tv_name;
        MyGridView gridView;
        View title_layout;
    }


    /*
     *
	 */
    class GirdViewAdapter extends BaseAdapter {
        List<GoodsTypeBean> dataLists;

        public GirdViewAdapter(List<GoodsTypeBean> dataLists) {
            this.dataLists = dataLists;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (dataLists == null) {
                return 0;
            }
            return dataLists.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int arg0, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub
            GridHolder vh = null;
            if (null == convertView) {
                vh = new GridHolder();
                convertView = mInflater.inflate(
                        R.layout.item_goodssecondtype_grid, null);
                vh.tv_name = (TextView) convertView
                        .findViewById(R.id.tv_name);
                vh.right_line = convertView
                        .findViewById(R.id.right_line);
                vh.bottom_line = convertView
                        .findViewById(R.id.bottom_line);
                convertView.setTag(vh);
            } else {
                vh = (GridHolder) convertView.getTag();
            }
            vh.tv_name.setText(dataLists.get(arg0).gc_name);
            if ((arg0 + 1) % 3 == 0) {
                vh.right_line.setVisibility(View.GONE);
            } else {
                vh.right_line.setVisibility(View.VISIBLE);
            }
            vh.bottom_line.setVisibility(View.VISIBLE);
            switch (dataLists.size() % 3) {
                case 0:
                    if (arg0 == dataLists.size() - 3)
                        vh.bottom_line.setVisibility(View.GONE);
                case 2:
                    if (arg0 == dataLists.size() - 2)
                        vh.bottom_line.setVisibility(View.GONE);
                case 1:
                    if (arg0 == dataLists.size() - 1)
                        vh.bottom_line.setVisibility(View.GONE);
                    break;
            }
            return convertView;
        }

        class GridHolder {
            TextView tv_name;
            View right_line, bottom_line;
        }

    }


}