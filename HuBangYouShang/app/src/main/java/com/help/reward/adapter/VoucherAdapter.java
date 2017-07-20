package com.help.reward.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.VoucherBean;
import com.idotools.utils.DateUtil;

import java.util.List;


/**
 * Created by wuxiaojun on 17-7-7.
 */

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {

    public static final int TYPE_VIEW_1 = 1;
    public static final int TYPE_VIEW_2 = 2;

    private List<VoucherBean> mDataList;
    private Context mContext;

    private int mDefaultIndex = -1;

    public VoucherAdapter(Context context, List<VoucherBean> mDataList) {
        this.mContext = context;
        this.mDataList = mDataList;
    }


    @Override
    public VoucherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VoucherViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_dialog_voucher, null));
    }

    @Override
    public void onBindViewHolder(VoucherViewHolder holder, int position) {


        VoucherBean bean = mDataList.get(position);
        VoucherBean lastBean = null;
        if (position != 0) {
            lastBean = mDataList.get(position - 1);
        }
        holder.tv_price.setText(bean.voucher_price);
        holder.tv_price_bottom.setText(bean.voucher_desc);
        holder.tv_store_name.setText(bean.store_name);
        holder.tv_date.setText(DateUtil.getDateToString1(bean.voucher_start_date) + "-" + DateUtil.getDateToString1(bean.voucher_end_date));

        int itemType = holder.getItemViewType();
        setType(position, holder.tv_type, holder.checkBox, bean, lastBean, itemType);
        setOnCheckedListener(position, holder.checkBox);
    }

    /****
     * 设置单选事件
     *
     * @param position
     * @param checkBox
     */
    private void setOnCheckedListener(final int position, CheckBox checkBox) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mDefaultIndex != -1) { // 设置原来的bean对象为没选中
                        VoucherBean lastDefaultBean = mDataList.get(mDefaultIndex);
                        lastDefaultBean.isChecked = false;
                        mDataList.set(mDefaultIndex, lastDefaultBean);
                    }
                    // 设置当前对象为选中对象
                    VoucherBean currentBean = mDataList.get(position);
                    currentBean.isChecked = true;
                    mDataList.set(position, currentBean);
                    /*int size = mDataList.size();
                    String checkedId = mDataList.get(position).voucher_id;
                    for (int i = 0; i < size; i++) {
                        VoucherBean bean = mDataList.get(i);
                        if (checkedId.equals(bean.voucher_id)) {
                            bean.isChecked = true;
                        } else {
                            bean.isChecked = false;
                        }
                        mDataList.set(i, bean);
                    }*/
                    // 刷新界面
                    notifyDataSetChanged();
                    // 更新索引
                    mDefaultIndex = position;
                }
            }
        });
    }

    /***
     * 设置类型
     *
     * @param tv_type
     * @param checkBox
     * @param bean
     * @param lastBean
     */
    private void setType(int position, TextView tv_type, CheckBox checkBox, VoucherBean bean, VoucherBean lastBean, int itemType) {
        if (itemType == TYPE_VIEW_1) {
            checkBox.setVisibility(View.VISIBLE);
            if (bean.isChecked) {
                checkBox.setChecked(true);
                mDefaultIndex = position;
            } else {
                checkBox.setChecked(false);
            }
            if (lastBean == null) {
                tv_type.setVisibility(View.VISIBLE);
                tv_type.setText(R.string.string_available_coupon);
            } else {
                if (lastBean.useable) { // 如果上一个bean也是可用优惠卷，则隐藏当前的tv_type
                    tv_type.setVisibility(View.GONE);
                } else { // 如果上一个bean不是可用优惠卷，则需要显示当前的tv_type
                    tv_type.setText(R.string.string_available_coupon);
                    tv_type.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (lastBean == null) {
                tv_type.setVisibility(View.VISIBLE);
                tv_type.setText(R.string.string_not_available_coupon);
            } else {
                if (!lastBean.useable) { // 如果上一个bean也是不可用优惠卷，则隐藏当前的tv_type
                    tv_type.setVisibility(View.GONE);
                } else { // 如果上一个bean不是不可用优惠卷，则需要显示当前的tv_type
                    tv_type.setText(R.string.string_not_available_coupon);
                    tv_type.setVisibility(View.VISIBLE);
                }
            }
            checkBox.setVisibility(View.GONE);
        }
        /*if (bean.useable) {
            if (bean.isChecked) {
                checkBox.setChecked(true);
                mDefaultIndex = position;
            }
            if (lastBean == null) {
                tv_type.setVisibility(View.VISIBLE);
                tv_type.setText(R.string.string_available_coupon);
            } else {
                if (lastBean.useable) { // 如果上一个bean也是可用优惠卷，则隐藏当前的tv_type
                    tv_type.setVisibility(View.GONE);
                } else { // 如果上一个bean不是可用优惠卷，则需要显示当前的tv_type
                    tv_type.setText(R.string.string_available_coupon);
                    tv_type.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (lastBean == null) {
                tv_type.setVisibility(View.VISIBLE);
                tv_type.setText(R.string.string_not_available_coupon);
            } else {
                if (!lastBean.useable) { // 如果上一个bean也是不可用优惠卷，则隐藏当前的tv_type
                    tv_type.setVisibility(View.GONE);
                } else { // 如果上一个bean不是不可用优惠卷，则需要显示当前的tv_type
                    tv_type.setText(R.string.string_not_available_coupon);
                    tv_type.setVisibility(View.VISIBLE);
                }
            }
        }*/
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        VoucherBean bean = mDataList.get(position);
        if (bean.useable) {
            return TYPE_VIEW_1;
        } else {
            return TYPE_VIEW_2;
        }
//        return super.getItemViewType(position);
    }

    public class VoucherViewHolder extends RecyclerView.ViewHolder {
        TextView tv_type;
        TextView tv_price;
        TextView tv_price_bottom;
        TextView tv_store_name;
        TextView tv_date;
        CheckBox checkBox;

        public VoucherViewHolder(View itemView) {
            super(itemView);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_price_bottom = (TextView) itemView.findViewById(R.id.tv_price_bottom);
            tv_store_name = (TextView) itemView.findViewById(R.id.tv_store_name);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            checkBox = (CheckBox) itemView.findViewById(R.id.id_cb_use_voucher);

        }


    }


}
