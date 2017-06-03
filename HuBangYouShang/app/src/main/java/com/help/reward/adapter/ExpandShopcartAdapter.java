package com.help.reward.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.bean.MyOrderShopBean;
import com.help.reward.bean.Response.CartInfoBean;
import com.help.reward.utils.GlideUtils;
import com.help.reward.view.NumSetDialog;
import com.help.reward.view.SwipeMenuView;
import com.idotools.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADBrian on 01/04/2017.
 */

public class ExpandShopcartAdapter extends BaseExpandableListAdapter {

    public static final int SHOPCART_SELECTED = 1;
    public static final int SHOPCART_NUM_EDIT  = SHOPCART_SELECTED + 1;
    public static final int SHOPCART_DELETED = SHOPCART_NUM_EDIT + 1;

    public static final int MAX_NUM = 99;

    private List<CartInfoBean.GoodInfoBean> mCheckList;

    private Context context;
    NumSetDialog numSetDialog;
    private ShopCartOperateListener mListener;

    protected List<CartInfoBean> mDataList = new ArrayList<CartInfoBean>();

    private ExpandableListView expandableListView;


    public ExpandShopcartAdapter(List<CartInfoBean.GoodInfoBean> mCheckList, Context context, List<CartInfoBean> cart_list, ExpandableListView lRecyclerview, ShopCartOperateListener listener) {

        if (mCheckList == null) {
            throw new IllegalArgumentException("error input");
        }
        this.mCheckList = mCheckList;
        this.context = context;
        this.expandableListView = lRecyclerview;
        this.mListener = listener;
        this.mDataList = cart_list;
    }

    @Override
    public int getGroupCount() {
        return mDataList == null ? 0: mDataList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mDataList.get(groupPosition).goods== null ? 0 : mDataList.get(groupPosition).goods.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mDataList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mDataList.get(groupPosition).goods.get(childPosition);
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

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

        final CartInfoBean bean = mDataList.get(groupPosition);

        if (isCurrentStoreSelectAll(bean.goods)) {
            holderGroup.iv_store_check.setImageResource(R.mipmap.img_address_checkbox_checked);
        } else {
            holderGroup.iv_store_check.setImageResource(R.mipmap.img_address_checkbox);
        }

        holderGroup.iv_store_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCurrentStoreSelectAll(bean.goods)) {
                    mCheckList.removeAll(bean.goods);
                } else {
                    addAllList(bean.goods);
                }

                if (mListener != null) {
                    mListener.operate(null,SHOPCART_SELECTED,-1);
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final ViewHolderChild holderChild;
        final View closeView;
        if (convertView == null) {
            holderChild = new ViewHolderChild();
            convertView = View.inflate(context,R.layout.item_shop_cart_info,null);
            holderChild.iv_check = (ImageView) convertView.findViewById(R.id.iv_item_check);
            holderChild.iv_shop_img = (ImageView) convertView.findViewById(R.id.iv_shop_img); // 商品图片
            holderChild.tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name); // 商品名称
            holderChild.tv_shop_atrribute = (TextView) convertView.findViewById(R.id.tv_shop_atrribute); // 商品属性:属性值
            holderChild.tv_single_shop_price = (TextView) convertView.findViewById(R.id.tv_single_shop_price); // 单个商品价格 ￥200.0
            holderChild.mTvDelete = (TextView) convertView.findViewById(R.id.tv_delete);

            holderChild.mNumAdd = (ImageView) convertView.findViewById(R.id.iv_add);
            holderChild.mNumDes = (ImageView) convertView.findViewById(R.id.iv_sub);
            holderChild.mNumShow = (TextView) convertView.findViewById(R.id.tv_num_show);

            convertView.setTag(holderChild);
        } else {
            holderChild = (ViewHolderChild) convertView.getTag();
            ((SwipeMenuView) convertView).quickClose();
        }

        holderChild.iv_check.setImageResource(R.mipmap.img_address_checkbox);
        ((SwipeMenuView) convertView).setIos(false).setLeftSwipe(true);

        final CartInfoBean.GoodInfoBean myOrderShopBean = mDataList.get(groupPosition).goods.get(childPosition);

        GlideUtils.loadImage(myOrderShopBean.goods_image_url, holderChild.iv_shop_img);
        holderChild.tv_shop_name.setText(myOrderShopBean.goods_name);
//            tv_shop_atrribute.setText("商品属性:");

        List<CartInfoBean.GoodSpec> goods_spec = myOrderShopBean.goods_spec;
        if (goods_spec != null && !goods_spec.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (CartInfoBean.GoodSpec goodSpec:
                 goods_spec) {
                stringBuilder.append(goodSpec.sp_name+":" +goodSpec.sp_value_name+" ");
            }
            holderChild.tv_shop_atrribute.setText(stringBuilder.toString());
        } else {
            holderChild.tv_shop_atrribute.setText("");
        }
        holderChild.tv_single_shop_price.setText(myOrderShopBean.goods_price);
        holderChild.mNumShow.setText(myOrderShopBean.goods_num);
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

                if (mListener != null) {
                    mListener.operate(null,SHOPCART_SELECTED,-1);
                }
                notifyDataSetChanged();
            }
        });

        holderChild.mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //TODO 删除
                if (mListener != null) {
                    mListener.operate(myOrderShopBean,SHOPCART_DELETED,-1);
                }

                //if (extend_order_goods.size() > 1){
                //    mDataList.get(groupPosition).goods.remove(childPosition);
                //} else {
                //    mDataList.remove(groupPosition);
                //}
                notifyDataSetChanged();
            }
        });


        holderChild.mNumAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String num = holderChild.mNumShow.getText().toString().trim();
                if (!TextUtils.isEmpty(num)) {
                    int newNum = Integer.parseInt(num) + 1;
                    if (newNum > MAX_NUM) {
                        newNum = MAX_NUM;
                        holderChild.mNumAdd.setEnabled(false);
                        //holderChild.mNumShow.setText(MAX_NUM+"");
                        newNum = MAX_NUM;
                        //TODO
                    } else {
                        holderChild.mNumDes.setEnabled(true);
                        //holderChild.mNumShow.setText(newNum + "");
                    }

                    //mDataList.get(groupPosition).goods.get(childPosition).goods_num = newNum+"";
                    //myOrderShopBean.goods_num = newNum +"";

                    if (mListener != null) {
                        mListener.operate(myOrderShopBean,SHOPCART_NUM_EDIT,newNum);
                    }
                }
            }
        });

        holderChild.mNumDes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String num = holderChild.mNumShow.getText().toString().trim();
                if (!TextUtils.isEmpty(num)) {
                    int newNum = Integer.parseInt(num) - 1;
                    if (newNum <= 1) {
                        newNum = 1;
                        holderChild.mNumDes.setEnabled(false);
                        //holderChild.mNumShow.setText("1");
                    } else {
                        holderChild.mNumAdd.setEnabled(true);
                        //holderChild.mNumShow.setText(newNum + "");
                    }

                    //myOrderShopBean.goods_num = newNum +"";
                    if (mListener != null) {
                        mListener.operate(myOrderShopBean,SHOPCART_NUM_EDIT,newNum);
                    }

                }
            }
        });

        holderChild.mNumShow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (numSetDialog == null) {
                    numSetDialog = new NumSetDialog(context, R.style.MyDialogStyle,MAX_NUM);
                }

                numSetDialog.setCancleBtn(
                        context.getResources().getString(R.string.canclebtn),
                       -1,
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                numSetDialog.dismiss();
                            }
                        });
                numSetDialog.setOKbtn(
                        context.getResources().getString(R.string.surebtn),
                        -1,
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                int num = numSetDialog.getNumShow();
                                if (num != -1) {
                                    if (num == 1) {
                                        holderChild.mNumDes.setEnabled(false);
                                        holderChild.mNumAdd.setEnabled(true);
                                    } else {
                                        if (num == MAX_NUM) {
                                            holderChild.mNumDes.setEnabled(true);
                                            holderChild.mNumAdd
                                                    .setEnabled(false);
                                        } else {
                                            holderChild.mNumDes.setEnabled(true);
                                            holderChild.mNumAdd.setEnabled(true);
                                        }
                                    }
                                    //holderChild.mNumShow.setText(num + "");
                                    //TODO 赋值
                                    //myOrderShopBean.goods_num = num +"";
                                    if (mListener != null) {
                                        mListener.operate(myOrderShopBean,SHOPCART_NUM_EDIT,num);
                                    }

                                }
                                numSetDialog.dismiss();
                            }
                        });

//                if (list.get(position) != null
//                        && !TextUtils.isEmpty(list.get(position).getProdName())) {
//                    String brandName = UnitUtils.getBrandName(list
//                            .get(position).getDrugBrand());
//                    numSetDialog.setTitleText(brandName
//                            + list.get(position).getProdName());
//                }
                String num = holderChild.mNumShow.getText().toString().trim();
                if (!TextUtils.isEmpty(num)) {
                    numSetDialog.setNumShow(Integer.parseInt(num));
                }

                if (!numSetDialog.isShowing()) {
                    numSetDialog.show();
                }
            }
        });



        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private void addAllList(List<CartInfoBean.GoodInfoBean> extend_order_goods) {
        if (extend_order_goods == null || extend_order_goods.isEmpty()) {
            return ;
        }

        if (mCheckList.size() == 0) {
            mCheckList.addAll(extend_order_goods);
            return;
        }

        for (CartInfoBean.GoodInfoBean orderBean: extend_order_goods) {
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
    private boolean isCurrentStoreSelectAll(List<CartInfoBean.GoodInfoBean> extend_order_goods){
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

    private boolean isCurrentGoodSelect(CartInfoBean.GoodInfoBean order_goods){
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


    public interface ShopCartOperateListener{
        void operate(CartInfoBean.GoodInfoBean goodInfo,int action,int num);
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
        public ImageView mNumAdd;
        public TextView mNumShow;
        public ImageView mNumDes;

    }
}
