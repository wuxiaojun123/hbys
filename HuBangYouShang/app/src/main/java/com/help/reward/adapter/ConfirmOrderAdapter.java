package com.help.reward.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.AddressBean;
import com.help.reward.bean.MyOrderListBean;
import com.help.reward.bean.MyOrderShopBean;
import com.help.reward.bean.Response.CartInfoBean;
import com.help.reward.bean.Response.ConfirmOrderResponse;
import com.help.reward.bean.VoucherBean;
import com.help.reward.utils.GlideUtils;
import com.help.reward.view.VoucherDialog;
import com.idotools.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ADBrian on 04/04/2017.
 */

public class ConfirmOrderAdapter extends RecyclerView.Adapter<SuperViewHolder> {


    public static final int ITEM_HEAD = 1;
    public static final int ITEM_BOTTOM = ITEM_HEAD + 1;
    public static final int ITEM_NORMAL = ITEM_BOTTOM + 1;

    protected List<ConfirmOrderResponse.ConfirmCartList> mDataList = new ArrayList<ConfirmOrderResponse.ConfirmCartList>();

    protected Context mContext;

    protected LayoutInflater mInflater;
    private AddressBean address_info;

    private String discount_level;
    private String available_general_voucher;
    private ConfirmOrderResponse.AddressApi addressApi; // 里面包含了邮费

    private StringBuilder voucher = new StringBuilder(); // t_id|store_id|price, 优惠卷
    private boolean useGeneralVoucher = false; // 是否使用通用卷
    private StringBuilder pay_message = new StringBuilder(); // 店铺id|备注内容

    public ConfirmOrderAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);

    }

    public void setDataList(List<ConfirmOrderResponse.ConfirmCartList> mDataList) {
        if (mDataList != null) {
            this.mDataList.clear();
            this.mDataList.addAll(mDataList);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return ITEM_HEAD;
        } else if (position == mDataList.size() + 1) {
            return ITEM_BOTTOM;
        } else {
            return ITEM_NORMAL;
        }
    }

    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        switch (viewType) {
            case ITEM_HEAD:
                itemView = mInflater.inflate(R.layout.item_my_order_confirm_title, parent, false);
                break;
            case ITEM_BOTTOM:
                itemView = mInflater.inflate(R.layout.item_my_order_confirm_bottom, parent, false);
                break;
            default:
                itemView = mInflater.inflate(R.layout.item_my_order_confirm, parent, false);
                break;
        }
        return new SuperViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(SuperViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ITEM_HEAD:
                doHeadView(holder, position);
                break;
            case ITEM_BOTTOM:
                doBottomView(holder, position);
                break;
            default:
                doNormalView(holder, position);
                break;
        }

    }

    private void doNormalView(SuperViewHolder holder, int position) {
        TextView tv_store_name = holder.getView(R.id.tv_store_name); // 商家名称
        TextView tv_shipping_methods = holder.getView(R.id.tv_shipping_methods); // 配送方式
        EditText et_remarks = holder.getView(R.id.et_remarks); // 留言备注
        TextView tv_coupon = holder.getView(R.id.tv_coupon); // 优惠劵
        TextView tv_total_shop_and_money = holder.getView(R.id.tv_total_shop_and_money); // 共计：4件商品  合计：￥235元(含运费12元)

        LinearLayout ll_shop = holder.getView(R.id.ll_shop); // 商品列表

        final ConfirmOrderResponse.ConfirmCartList bean = mDataList.get(position - 1);

        int size = 0;
        if (ll_shop.getTag() == null && bean.goods_list != null) {
            size = bean.goods_list.size();
            setShopText(ll_shop, bean, size);
            ll_shop.setTag(bean.store_id);
        }

        tv_store_name.setText(bean.store_name); // 店铺名称
        bindVoucherView(tv_coupon, bean);

        bindRemarksView(et_remarks, bean);

        // 根据店铺id拿到邮费
        if (addressApi != null) {
            Map<String, String> postageMap = addressApi.content;
            if (postageMap != null) {
                String expressCharges = postageMap.get(bean.store_id); // 快递费用
//            LogUtils.e("快递费用是：" + expressCharges);
                if (TextUtils.isEmpty(expressCharges) || "0.00".equals(expressCharges)) {
                    tv_shipping_methods.setText("包邮"); //
                    tv_total_shop_and_money.setText("共计：" + size + "件商品  合计：￥" + bean.store_goods_total + "元(含运费0元)");
                } else {
                    tv_shipping_methods.setText(expressCharges + "元");
                    tv_total_shop_and_money.setText("共计：" + size + "件商品  合计：￥" + bean.store_goods_total + "元(含运费" + expressCharges + "元)");
                }
            }
        }

    }

    private HashMap<String, String> mPayMsgMap = new HashMap<>();

    /***
     * 绑定备注留言信息
     *
     * @param et_remarks
     * @param bean
     */
    private void bindRemarksView(EditText et_remarks, final ConfirmOrderResponse.ConfirmCartList bean) {
        et_remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    String content = s.toString();
                    if (content != null) {
                        if (mPayMsgMap.containsKey(bean.store_id)) {
                            mPayMsgMap.remove(bean.store_id);
                        }
                        mPayMsgMap.put(bean.store_id, content + ",");
//                        pay_message.append(bean.store_id + "|" + content+",");
                    }
                }
            }
        });
    }

    private HashMap<String, String> mVoucherMap = new HashMap<>();

    /***
     * 绑定优惠卷ui
     *
     * @param tv_coupon
     * @param bean
     */
    private void bindVoucherView(TextView tv_coupon, final ConfirmOrderResponse.ConfirmCartList bean) {
        VoucherBean defaultVoucherBean = bean.store_voucher_info;
        if (defaultVoucherBean == null) {
            tv_coupon.setText("无可用");
        } else {
            tv_coupon.setText("可用优惠劵");
            // 全部可用优惠卷  bean.store_voucher_list  全部不可用优惠卷 bean.store_voucher_list2
            String defaultId = defaultVoucherBean.voucher_id;
            mVoucherMap.put(defaultVoucherBean.voucher_id,
                    defaultVoucherBean.voucher_id + "|" + defaultVoucherBean.voucher_store_id + "|" + defaultVoucherBean.voucher_price+",");

            List<VoucherBean> list = new ArrayList<>();
            for (VoucherBean voucherBean : bean.store_voucher_list) {
                voucherBean.useable = true;
                if (defaultId != null) {
                    if (voucherBean.voucher_id.equals(defaultId)) {
                        voucherBean.isChecked = true;
                    }
                }
                list.add(voucherBean);
            }
            for (VoucherBean voucherBean : bean.store_voucher_list2) {
                voucherBean.useable = false;
                list.add(voucherBean);
            }
            final VoucherDialog dialog = new VoucherDialog(mContext, list, mVoucherMap);
            tv_coupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.showDialog();
                }
            });
        }
    }

    private void setShopText(LinearLayout ll_shop, ConfirmOrderResponse.ConfirmCartList bean, int size) {
        for (int i = 0; i < size; i++) {
            View shopView = mInflater.inflate(R.layout.layout_my_order_shop, ll_shop, false);
            ImageView iv_shop_img = (ImageView) shopView.findViewById(R.id.iv_shop_img); // 商品图片
            TextView tv_shop_name = (TextView) shopView.findViewById(R.id.tv_shop_name); // 商品名称
//            TextView tv_shop_atrribute = (TextView) shopView.findViewById(R.id.tv_shop_atrribute); // 商品属性:属性值
            TextView tv_single_shop_price = (TextView) shopView.findViewById(R.id.tv_single_shop_price); // 单个商品价格 ￥200.0
            TextView tv_shop_num = (TextView) shopView.findViewById(R.id.tv_shop_num); // 商品数量 x1

            ConfirmOrderResponse.GoodInfo goodInfoBean = bean.goods_list.get(i);

            GlideUtils.loadImage(goodInfoBean.goods_image_url, iv_shop_img);
            tv_shop_name.setText(goodInfoBean.goods_name);
//            tv_shop_atrribute.setText("商品属性:");
            tv_single_shop_price.setText(goodInfoBean.goods_price);
            tv_shop_num.setText("x" + goodInfoBean.goods_num);
            ll_shop.addView(shopView);
        }
    }

    private void doBottomView(SuperViewHolder holder, int position) {
        TextView mOrderDiscountPer = (TextView) holder.itemView.findViewById(R.id.tv_confim_order_discount);
        TextView mOrderDiscount = (TextView) holder.itemView.findViewById(R.id.tv_confim_order_discount_name);
        ToggleButton btn_switch = (ToggleButton) holder.itemView.findViewById(R.id.btn_switch); // 开关按钮
        // 有可能没有通用劵的情况，应该进行区分
        mOrderDiscountPer.setText("商家设置的通用券抵扣比例为:" + discount_level + "%");
        if (TextUtils.isEmpty(available_general_voucher) || "".equals(available_general_voucher)
                || "0".equals(available_general_voucher)) {
            mOrderDiscount.setText("无可用通用劵");
            btn_switch.setEnabled(false);
        } else {
//            final double amount = Double.parseDouble(available_general_voucher) * Double.parseDouble(discount_level);
//            mOrderDiscount.setText("可用" + available_general_voucher + "通用劵 抵" + amount + "元");
            btn_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    useGeneralVoucher = isChecked;
                }
            });
        }
    }

    private void doHeadView(SuperViewHolder holder, int position) {
        TextView mOrderReceiverPerson = (TextView) holder.itemView.findViewById(R.id.tv_order_receiver_person);
        TextView mOrderReceiverPhone = (TextView) holder.itemView.findViewById(R.id.tv_order_receiver_phone);
        TextView mOrderReceiverAdd = (TextView) holder.itemView.findViewById(R.id.tv_order_receiver_add);
        if (address_info != null) {
            mOrderReceiverPerson.setText("收货人:" + address_info.true_name);
            mOrderReceiverPhone.setText(address_info.mob_phone);
            String address = address_info.area_info + address_info.address;
            mOrderReceiverAdd.setText("收货地址:" + address);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size() + 2;
    }

    public void setAddressInfo(AddressBean address_info) {
        this.address_info = address_info;
    }

    public void setAddressApi(ConfirmOrderResponse.AddressApi addressApi) {
        this.addressApi = addressApi;
    }

    public void setDiscount_level(String discount_level) {
        this.discount_level = discount_level;
    }

    public void setAvailable_general_voucher(String available_general_voucher) {
        this.available_general_voucher = available_general_voucher;
    }

    public String getVoucher() {
        if (voucher.length() > 0) {
            voucher.delete(0, voucher.length() - 1);
        }
        for (Map.Entry<String, String> map : mVoucherMap.entrySet()) {
            voucher.append(map.getValue());
        }
        return voucher.toString();
    }

    public boolean getGeneral_voucher() {
        return useGeneralVoucher;
    }

    public String getPay_message() {
        // 遍历map
        if (pay_message.length() > 0) {
            pay_message.delete(0, pay_message.length() - 1);
        }
        for (Map.Entry<String, String> map : mPayMsgMap.entrySet()) {
            pay_message.append(map.getKey() + "|" + map.getValue());
        }
        return pay_message.toString();
    }


}
