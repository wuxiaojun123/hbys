package com.help.reward.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.activity.ApplyForRefundActivity;
import com.help.reward.activity.BaseActivity;
import com.help.reward.activity.OrderDetailsActivity;
import com.help.reward.activity.OrderPulishedEvaluateActivity;
import com.help.reward.activity.PayTypeActivity;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.GoodsSpecBean;
import com.help.reward.bean.MyOrderListBean;
import com.help.reward.bean.MyOrderShopBean;
import com.help.reward.biz.MyOrderBiz;
import com.help.reward.manager.OrderOperationManager;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.GlideUtils;
import com.idotools.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 我的订单 Created by wuxiaojun on 2017/2/26.
 */

public class MyOrderAdapter extends BaseRecyclerAdapter implements OrderOperationManager.OnItemRemoveOrderClickListener {

	private OrderOperationManager	mOperationManager;

	private HashMap<Integer, View>	mLayoutMap	= new HashMap<>();

	private Context					mContext;

	private MyOrderBiz				mMyOrderBiz;

	public MyOrderAdapter(Context context) {
		super(context);
		mContext = context;
		mMyOrderBiz = new MyOrderBiz(context);
		mOperationManager = new OrderOperationManager(context);
		mOperationManager.setOnItemRemoveOrderClickListener(this);
	}

	@Override public int getLayoutId() {
		return R.layout.item_my_order;
	}

	@Override public void onBindItemHolder(SuperViewHolder holder, final int position) {
		TextView tv_store_name = holder.getView(R.id.tv_store_name); // 商家名称
		TextView tv_shop_state = holder.getView(R.id.tv_shop_state); // 商品状态，是已支付还是未支付
		TextView tv_total_shop_and_money = holder.getView(R.id.tv_total_shop_and_money); // 共计：4件商品 合计：￥235元(含运费12元)
		TextView tv_cancel_order = holder.getView(R.id.tv_cancel_order); // 取消订单
		final TextView tv_remove_order = holder.getView(R.id.tv_remove_order); // 删除订单
		final TextView tv_evaluate_order = holder.getView(R.id.tv_evaluate_order); // 评价订单
		LinearLayout ll_click = holder.getView(R.id.ll_click); // 各种按钮
		LinearLayout ll_shop = holder.getView(R.id.ll_shop); // 商品列表

		final MyOrderListBean.OrderList bean = (MyOrderListBean.OrderList) mDataList.get(position);

		int size = 0;
		if (bean.extend_order_goods != null) {
			size = bean.extend_order_goods.size();
			if (mLayoutMap.get(position) != null) {
				View mLayout = mLayoutMap.get(position);
				if (ll_shop.getChildCount() > 0) {
					ll_shop.removeAllViews();
				}
				ViewGroup parentView = (ViewGroup) mLayout.getParent();
				if (parentView != null) {
					parentView.removeAllViews();
				}
				ll_shop.addView(mLayout);
			} else {
				setShopText(ll_shop, bean, size, position);
			}
		}
		setOrderState(tv_shop_state, ll_click, tv_cancel_order, tv_remove_order, tv_evaluate_order, bean);
		setEvent(position, tv_cancel_order, tv_remove_order, tv_evaluate_order, ll_shop, bean, size);
		tv_store_name.setText(bean.store_name);
		setEvaluationState(tv_evaluate_order, bean);

		double shippingFee = Double.parseDouble(bean.shipping_fee);
		if (shippingFee > 0) {
			tv_total_shop_and_money.setText("共计：" + size + "件商品  合计：￥" + bean.order_amount + "(含运费" + shippingFee + ")");
		} else {
			tv_total_shop_and_money.setText("共计：" + size + "件商品  合计：￥" + bean.order_amount + "(免运费)");
		}
	}

	/***
	 * 设置各种事件
	 *
	 * @param position
	 * @param tv_cancel_order
	 * @param tv_remove_order
	 * @param tv_evaluate_order
	 * @param bean
	 */
	private void setEvent(final int position, TextView tv_cancel_order, final TextView tv_remove_order, final TextView tv_evaluate_order, LinearLayout ll_shop, final MyOrderListBean.OrderList bean,
			final int size) {
		tv_remove_order.setOnClickListener(new View.OnClickListener() {

			@Override public void onClick(View v) { // 删除订单逻辑
				String tag = (String) tv_remove_order.getTag();
				if (tag != null) {
					Intent mIntent = new Intent(mContext, ApplyForRefundActivity.class);
					mIntent.putExtra("order_id", bean.order_id);
					mContext.startActivity(mIntent);
					ActivitySlideAnim.slideInAnim((BaseActivity) mContext);
				} else {
					mOperationManager.showRemoveDialog(bean.order_id, position);
				}
			}
		});
		tv_cancel_order.setOnClickListener(new View.OnClickListener() {

			@Override public void onClick(View v) { // 取消订单
				mOperationManager.showCancelDialog(bean.order_id, position);
			}
		});
		tv_evaluate_order.setOnClickListener(new View.OnClickListener() {

			@Override public void onClick(View v) {
				String tag = (String) tv_evaluate_order.getTag();
				if (tag != null) {
					if ("1".equals(tag)) { // 立即付款
						Intent mIntent = new Intent(mContext, PayTypeActivity.class);
						mIntent.putExtra("pay_sn", bean.pay_sn);
						mContext.startActivity(mIntent);
						ActivitySlideAnim.slideInAnim((Activity) mContext);

					} else if ("2".equals(tag)) {// 确认收货
						mMyOrderBiz.validateHasConfirmPwd(bean.order_id);
					} else if ("3".equals(tag)) {// 评价页面
						gotoEvaluateActivity(size, bean);
					}
				}
			}
		});
		ll_shop.setOnClickListener(new View.OnClickListener() {

			@Override public void onClick(View v) { // 跳往订单详情
				Intent mIntent = new Intent(mContext, OrderDetailsActivity.class);
				mIntent.putExtra("order_id", bean.order_id);
				mContext.startActivity(mIntent);
				ActivitySlideAnim.slideInAnim((BaseActivity) mContext);
			}
		});
	}

	/***
	 * 待付款（lock_state=0,order_state=10） 待发货（lock_state=0,order_state=20）
	 * 待收货（lock_state=0,order_state=30） 已完成（lock_state=0,order_state=40）
	 * 已取消（lock_state=0,order_state=0） 退款中（lock_state=1）
	 * <p>
	 * 最终版 待付款的显示：取消订单+立即付款 待发货的不显示 待收货的显示：确认收货 已完成的显示：删除订单+评价 已取消的显示：删除订单 退款中的不显示
	 * <p>
	 * 待付款（lock_state=0,order_state=10） 可以：取消/立即付款 待发货（lock_state=0,order_state=20）
	 * 用户不能操作。可以投诉商家。 待收货（lock_state=0,order_state=30）可以：确认收货/投诉商家
	 * 已完成（lock_state=0,order_state=40）可以：评价/删除 已取消（lock_state=0,order_state=0）可以：删除
	 * 退款中（lock_state=1）
	 */
	private void setOrderState(TextView tv_shop_state, LinearLayout ll_click, TextView tv_cancel_order, TextView tv_remove_order, TextView tv_evaluate_order, MyOrderListBean.OrderList bean) {
		String order_state = bean.order_state; // 0:已取消,10(默认):未付款;20:已付款;30:已发货;40:已收货;50:已提交;60已确认;
		StringBuilder sb = new StringBuilder();

		if ("0".equals(bean.lock_state)) {
			ll_click.setVisibility(View.VISIBLE);
			if ("0".equals(order_state)) { // 已取消 删除订单
				tv_remove_order.setVisibility(View.VISIBLE);
				tv_cancel_order.setVisibility(View.GONE);
				tv_evaluate_order.setVisibility(View.GONE);
				tv_evaluate_order.setTag("0");
				sb.append("已取消");

			} else if ("10".equals(order_state)) { // 待付款 立即付款/取消订单
				tv_remove_order.setVisibility(View.GONE);
				tv_cancel_order.setVisibility(View.VISIBLE);
				tv_evaluate_order.setVisibility(View.VISIBLE);
				tv_evaluate_order.setText("立即付款");
				tv_evaluate_order.setTag("1");
				sb.append("待付款");

			} else if ("20".equals(order_state)) { // 待发货 不显示
				tv_remove_order.setVisibility(View.GONE);
				tv_cancel_order.setVisibility(View.GONE);
				tv_evaluate_order.setVisibility(View.GONE);
				tv_evaluate_order.setTag("0");
				sb.append("待发货");

			} else if ("30".equals(order_state)) { // 待收货=退款/退货 确认收货/退款退货
				tv_remove_order.setText("退款退货");
				tv_remove_order.setTag("1");
				tv_remove_order.setVisibility(View.VISIBLE);
				tv_cancel_order.setVisibility(View.GONE);
				tv_evaluate_order.setVisibility(View.VISIBLE);
				tv_evaluate_order.setText("确认收货");
				tv_evaluate_order.setTag("2");
				sb.append("待收货");

			} else if ("40".equals(order_state)) { // 已完成的显示：删除订单+评价
				tv_remove_order.setVisibility(View.VISIBLE);
				tv_cancel_order.setVisibility(View.GONE);
				tv_evaluate_order.setVisibility(View.VISIBLE);
				tv_evaluate_order.setText("评价");
				tv_evaluate_order.setTag("3");
				sb.append("已完成");

			}
		} else if ("1".equals(bean.lock_state)) { // 退款中 什么按钮都不显示
			tv_remove_order.setVisibility(View.GONE);
			tv_cancel_order.setVisibility(View.GONE);
			tv_evaluate_order.setVisibility(View.GONE);
			tv_evaluate_order.setTag("0");
			ll_click.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(bean.refund_desc)) {
			sb.append("("+bean.refund_desc+")");
		}
		tv_shop_state.setText(sb);
	}

	/***
	 * 设置评论状态
	 *
	 * @param tv_evaluate_order
	 * @param bean
	 */
	private void setEvaluationState(TextView tv_evaluate_order, MyOrderListBean.OrderList bean) {
		boolean isEvaluated = false;
		StringBuilder sb = new StringBuilder();
		if (bean.evaluation_state.equals("0")) {// 评价状态 0未评价，1已评价，2已过期未评价
			sb.append("未评价");
			isEvaluated = true;
		} else if (bean.evaluation_state.equals("1")) {
			sb.append("已评价");
		} else {
			sb.append("已过期未评价");
		}
		if (!TextUtils.isEmpty(bean.refund_desc)) {
			sb.append(bean.refund_desc);
		}
		// tv_shop_state.setText(sb);
		if (tv_evaluate_order.getTag().equals("3")) {
			if (isEvaluated) {
				tv_evaluate_order.setVisibility(View.VISIBLE);
			} else {
				if (tv_evaluate_order.getVisibility() != View.GONE) {
					tv_evaluate_order.setVisibility(View.GONE);
				}
			}
		}
	}

	private void gotoEvaluateActivity(int size, MyOrderListBean.OrderList bean) {
		final String[] goods_id = new String[size];
		final String[] goods_img = new String[size];
		final String[] goods_name = new String[size];
		final String[] rec_id = new String[size]; // 购买商品记录id
		int length = goods_id.length;

		for (int i = 0; i < size; i++) {
			MyOrderShopBean myOrderShopBean = bean.extend_order_goods.get(i);
			if (i < length) {
				goods_id[i] = myOrderShopBean.goods_id;
				goods_img[i] = myOrderShopBean.goods_image_url;
				goods_name[i] = myOrderShopBean.goods_name;
				rec_id[i] = myOrderShopBean.rec_id;
			}
		}
		Intent mIntent = new Intent(mContext, OrderPulishedEvaluateActivity.class);
		mIntent.putExtra("order_id", bean.order_id);
		mIntent.putExtra("goods_id", goods_id);
		mIntent.putExtra("goods_img", goods_img);
		mIntent.putExtra("goods_name", goods_name);
		mIntent.putExtra("rec_id", rec_id);
		mContext.startActivity(mIntent);
		ActivitySlideAnim.slideInAnim((Activity) mContext);
	}

	/***
	 * 设置商品数量
	 *
	 * @param ll_shop
	 * @param bean
	 * @param size
	 */
	private void setShopText(LinearLayout ll_shop, MyOrderListBean.OrderList bean, int size, int position) {
		final String[] goods_id = new String[size];
		final String[] goods_img = new String[size];
		final String[] goods_name = new String[size];
		final String[] rec_id = new String[size]; // 购买商品记录id

		LinearLayout mLayout = new LinearLayout(mContext);
		mLayout.setOrientation(LinearLayout.VERTICAL);
		int length = goods_id.length;
		for (int i = 0; i < size; i++) {
			View shopView = mInflater.inflate(R.layout.layout_my_order_shop, ll_shop, false);
			ImageView iv_shop_img = (ImageView) shopView.findViewById(R.id.iv_shop_img); // 商品图片
			TextView tv_shop_name = (TextView) shopView.findViewById(R.id.tv_shop_name); // 商品名称
			TextView tv_shop_atrribute = (TextView) shopView.findViewById(R.id.tv_shop_atrribute);// 商品属性值
			TextView tv_single_shop_price = (TextView) shopView.findViewById(R.id.tv_single_shop_price); // 单个商品价格 ￥200.0
			TextView tv_shop_num = (TextView) shopView.findViewById(R.id.tv_shop_num); // 商品数量 x1

			MyOrderShopBean myOrderShopBean = bean.extend_order_goods.get(i);
			if (i < length) {
				goods_id[i] = myOrderShopBean.goods_id;
				goods_img[i] = myOrderShopBean.goods_image_url;
				goods_name[i] = myOrderShopBean.goods_name;
				rec_id[i] = myOrderShopBean.rec_id;
			}
			GlideUtils.loadImage(myOrderShopBean.goods_image_url, iv_shop_img);
			tv_shop_name.setText(myOrderShopBean.goods_name);
			List<GoodsSpecBean> specList = myOrderShopBean.goods_spec;
			if (specList != null && !specList.isEmpty()) {
				tv_shop_atrribute.setVisibility(View.VISIBLE);
				StringBuilder specSb = new StringBuilder();
				for (GoodsSpecBean spec : specList) {
					specSb.append(spec.sp_name + ":" + spec.sp_value_name);
				}
				tv_shop_atrribute.setText("商品属性:" + specSb.toString());
			} else {
				tv_shop_atrribute.setVisibility(View.GONE);
			}
			tv_single_shop_price.setText(myOrderShopBean.goods_price);
			tv_shop_num.setText("x" + myOrderShopBean.goods_num);

			mLayout.addView(shopView);
		}
		if (ll_shop.getChildCount() > 0) {
			ll_shop.removeAllViews();
		}
		ViewGroup parentView = (ViewGroup) mLayout.getParent();
		if (parentView != null) {
			parentView.removeAllViews();
		}
		ll_shop.addView(mLayout);
		mLayoutMap.put(position, mLayout);
	}

	@Override public void onItemRemoveOrderClickListener(int position) {
		mLayoutMap.clear();
		this.mDataList.remove(position);
		notifyItemRemoved(position);
		if (position != (getDataList().size())) { // 如果移除的是最后一个，忽略
			notifyItemRangeChanged(position, this.mDataList.size() - position);
		}
		// notifyItemRangeChanged(position, getItemCount());
	}

}
