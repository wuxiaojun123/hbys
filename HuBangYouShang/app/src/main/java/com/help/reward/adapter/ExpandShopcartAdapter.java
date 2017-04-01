package com.help.reward.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.bean.MyOrderListBean;
import com.help.reward.bean.MyOrderShopBean;
import com.help.reward.utils.GlideUtils;
import com.idotools.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADBrian on 01/04/2017.
 */

public class ExpandShopcartAdapter extends BaseExpandableListAdapter {

    private List<MyOrderShopBean> mCheckList;

    private Context context;

    protected List<MyOrderListBean.OrderList> mDataList = new ArrayList<MyOrderListBean.OrderList>();

    private ExpandableListView expandableListView;


    public  void setDataList(List<MyOrderListBean.OrderList> mList){
        if (mList != null) {
            mDataList.clear();
            mDataList.addAll(mList);
        }
        notifyDataSetChanged();
    }


    public ExpandShopcartAdapter(List<MyOrderShopBean> mCheckList, Context context, ExpandableListView lRecyclerview) {

        if (mCheckList == null) {
            throw new IllegalArgumentException("error input");
        }
        this.mCheckList = mCheckList;
        this.context = context;
        this.expandableListView = lRecyclerview;
    }

    @Override
    public int getGroupCount() {
        return mDataList == null ? 0: mDataList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mDataList.get(groupPosition).extend_order_goods== null ? 0 : mDataList.get(groupPosition).extend_order_goods.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mDataList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mDataList.get(groupPosition).extend_order_goods.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ViewHolderGroup holderGroup = null;

        if (convertView == null) {
            holderGroup = new ViewHolderGroup();
            convertView = View.inflate(context, R.layout.item_shop_cart_store,null);
            holderGroup.iv_store_check = (ImageView) convertView.findViewById(R.id.iv_store_check);
            holderGroup.tv_store_name = (TextView) convertView.findViewById(R.id.tv_store_name);
            convertView.setTag(holderGroup);
        } else {
            holderGroup = (ViewHolderGroup) convertView.getTag();
        }

        holderGroup.iv_store_check.setImageResource(R.mipmap.img_address_checkbox);

        final MyOrderListBean.OrderList bean = mDataList.get(groupPosition);

        if (isCurrentStoreSelectAll(bean.extend_order_goods)) {
            holderGroup.iv_store_check.setImageResource(R.mipmap.img_address_checkbox_checked);
        } else {
            holderGroup.iv_store_check.setImageResource(R.mipmap.img_address_checkbox);
        }

        holderGroup.iv_store_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCurrentStoreSelectAll(bean.extend_order_goods)) {
                    mCheckList.removeAll(bean.extend_order_goods);
                } else {
                    addAllList(bean.extend_order_goods);
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ViewHolderChild holderChild = null;

        if (convertView == null) {
            holderChild = new ViewHolderChild();
            convertView = View.inflate(context,R.layout.item_shop_cart_info,null);
            holderChild.iv_check = (ImageView) convertView.findViewById(R.id.iv_item_check);
            holderChild.iv_shop_img = (ImageView) convertView.findViewById(R.id.iv_shop_img); // 商品图片
            holderChild.tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name); // 商品名称
            holderChild.tv_shop_atrribute = (TextView) convertView.findViewById(R.id.tv_shop_atrribute); // 商品属性:属性值
            holderChild.tv_single_shop_price = (TextView) convertView.findViewById(R.id.tv_single_shop_price); // 单个商品价格 ￥200.0
            holderChild.mTvDelete = (TextView) convertView.findViewById(R.id.tv_delete);
            convertView.setTag(holderChild);
        } else {
            holderChild = (ViewHolderChild) convertView.getTag();
        }
        holderChild.iv_check.setImageResource(R.mipmap.img_address_checkbox);

        final MyOrderShopBean myOrderShopBean = mDataList.get(groupPosition).extend_order_goods.get(childPosition);

        GlideUtils.loadImage(myOrderShopBean.goods_image_url, holderChild.iv_shop_img);
        holderChild.tv_shop_name.setText(myOrderShopBean.goods_name);
//            tv_shop_atrribute.setText("商品属性:");
        holderChild.tv_single_shop_price.setText(myOrderShopBean.goods_price);

        if (isCurrentGoodSelect(myOrderShopBean)) {
            holderChild.iv_check.setImageResource(R.mipmap.img_address_checkbox_checked);
        } else {
            holderChild.iv_check.setImageResource(R.mipmap.img_address_checkbox);
        }

        holderChild.iv_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCurrentGoodSelect(myOrderShopBean)) {
                    mCheckList.remove(myOrderShopBean);
                } else {
                    mCheckList.add(myOrderShopBean);
                }
                notifyDataSetChanged();
            }
        });

        holderChild.mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //TODO 删除
            }
        });


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private void addAllList(List<MyOrderShopBean> extend_order_goods) {
        if (extend_order_goods == null || extend_order_goods.isEmpty()) {
            return ;
        }

        if (mCheckList.size() == 0) {
            mCheckList.addAll(extend_order_goods);
            return;
        }

        for (MyOrderShopBean orderBean: extend_order_goods) {
            if (mCheckList.contains(orderBean)) {
                mCheckList.remove(orderBean);
            }
        }
        mCheckList.addAll(extend_order_goods);
    }


    /**
     *
     * @return
     * @param extend_order_goods
     */
    private boolean isCurrentStoreSelectAll(List<MyOrderShopBean> extend_order_goods){
        if (mCheckList.size() == 0) {
            return false;
        }

        if (extend_order_goods == null || extend_order_goods.isEmpty()) {
            return false;
        }

        if (mCheckList.size() < extend_order_goods.size()) {
            return false;
        }

        if (mCheckList.containsAll(extend_order_goods)) {
            return true;
        } else {
            return false;
        }

    }

    private boolean isCurrentGoodSelect(MyOrderShopBean order_goods){
        if (mCheckList.size() == 0) {
            return false;
        }

        if (mCheckList.contains(order_goods)) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void notifyDataSetChanged() {
        int groupCount = getGroupCount();
        super.notifyDataSetChanged();
        for (int i=0; i<groupCount; i++) {
            LogUtils.e("========展开======="+i);
            expandableListView.expandGroup(i);
        }
    }




    static class ViewHolderGroup {
        ImageView iv_store_check;
        TextView tv_store_name;
    }

    static class ViewHolderChild {

        public ImageView iv_check;
        public ImageView iv_shop_img;
        public TextView tv_shop_name;
        public TextView tv_shop_atrribute;
        public TextView tv_single_shop_price;
        public TextView mTvDelete;
    }
}
