package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.common.Logger;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.GoodsSpecBean;
import com.help.reward.bean.MyOrderShopBean;
import com.help.reward.bean.OrderInfoBean;
import com.help.reward.bean.Response.ApplyForRefundResponse;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.OrderInfoResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.BooleanRxbusType;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.ChooseCameraPopuUtils;
import com.help.reward.utils.GlideUtils;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 申请退款 Created by wxj on 2018/2/4.
 */

public class ApplyForRefundActivity extends BaseActivity {

	@BindView(R.id.tv_title) TextView				tv_title;

	@BindView(R.id.tv_title_right) TextView			tv_title_right;

	@BindView(R.id.id_tv_mobile) TextView			id_tv_mobile;					// 联系方式

	@BindView(R.id.ll_shop) LinearLayout			ll_shop;						// 商品

	@BindView(R.id.id_tv_total_price) TextView		id_tv_total_price;				// 订单金额

	@BindView(R.id.id_et_explain) EditText			id_et_explain;					// 退款说明(选填)

	@BindView(R.id.iv_release_addphoto) ImageView	ivReleaseAddphoto;				// 上传图标

	@BindView(R.id.iv_photo1) ImageView				iv_photo1;

	@BindView(R.id.iv_delete1) ImageView			iv_delete1;

	@BindView(R.id.iv_photo2) ImageView				iv_photo2;

	@BindView(R.id.iv_delete2) ImageView			iv_delete2;

	@BindView(R.id.iv_photo3) ImageView				iv_photo3;

	@BindView(R.id.iv_delete3) ImageView			iv_delete3;

	@BindView(R.id.iv_photo4) ImageView				iv_photo4;

	@BindView(R.id.iv_delete4) ImageView			iv_delete4;

	@BindView(R.id.tv_photonum) TextView			tv_photonum;

	ChooseCameraPopuUtils							chooseCameraPopuUtils;

	List<String>									photoUrl	= new ArrayList<>();

	List<String>									file_names	= new ArrayList<>();

	private String									order_id;

	@Override protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_for_refund);
		ButterKnife.bind(this);
		initView();
		initNetwork();
	}

	private void initView() {
		tv_title.setText("申请退款");
		tv_title_right.setVisibility(View.GONE);
		ivReleaseAddphoto.setImageResource(R.mipmap.img_upload_photos_refund);
		chooseCameraPopuUtils = new ChooseCameraPopuUtils(this, "refund");
		chooseCameraPopuUtils.setOnUploadImageListener(new ChooseCameraPopuUtils.OnUploadImageListener() {

			@Override public void onLoadError() {
				ToastUtils.show(mContext, "获取图片失败");
			}

			@Override public void onLoadSucced(String file_name, String url) {
				photoUrl.add(url);
				file_names.add(file_name);
				showPhoto();
			}
		});
		order_id = getIntent().getStringExtra("order_id");
	}

	private void initNetwork() {
		if (TextUtils.isEmpty(order_id)) {
			ToastUtils.show(mContext, "订单号不存在");
			return;
		}
		MyProcessDialog.showDialog(mContext);
		PersonalNetwork.getResponseApi().getApplyForRefundResponse("member_order", "refund_apply", order_id, App.APP_CLIENT_KEY).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
				.subscribe(new BaseSubscriber<OrderInfoResponse>() {

					@Override public void onError(Throwable e) {
						e.printStackTrace();
						MyProcessDialog.closeDialog();
						ToastUtils.show(mContext, R.string.string_error);
					}

					@Override public void onNext(OrderInfoResponse response) {
						MyProcessDialog.closeDialog();
						if (response.code == 200) {
							if (response.data != null) { // 显示数据
								OrderInfoBean bean = response.data.order_info;
								bindData(bean);
								setShopText(bean);
							}
						} else {
							ToastUtils.show(mContext, response.msg);
						}
					}
				});
	}

	@OnClick({ R.id.iv_title_back, R.id.iv_release_addphoto, R.id.tv_commit, R.id.iv_delete1, R.id.iv_delete2, R.id.iv_delete3, R.id.iv_delete4 }) public void onClick(View view) {
		int id = view.getId();
		switch (id) {
			case R.id.iv_title_back:
				finish();
				ActivitySlideAnim.slideOutAnim(ApplyForRefundActivity.this);

				break;
			case R.id.tv_commit: // 提交
				commitApply();
				break;
			case R.id.iv_release_addphoto: // 添加图片
				chooseCameraPopuUtils.showPopupWindow();
				break;
			case R.id.iv_delete1:
				photoUrl.remove(0);
				file_names.remove(0);
				showPhoto();
				break;
			case R.id.iv_delete2:
				photoUrl.remove(1);
				file_names.remove(1);
				showPhoto();
				break;
			case R.id.iv_delete3:
				photoUrl.remove(2);
				file_names.remove(2);
				showPhoto();
				break;
			case R.id.iv_delete4:
				photoUrl.remove(3);
				file_names.remove(3);
				showPhoto();
				break;
		}
	}

	/***
	 * 提交申请
	 */
	private void commitApply() {
		String message = id_et_explain.getText().toString().trim();
		String pic_str = null;
		StringBuilder picSb = new StringBuilder();
		if (!file_names.isEmpty()) {
			for (String str : file_names) {
				picSb.append(str + ",");
			}
		}
		if (picSb.length() > 0) {
			pic_str = picSb.substring(0, picSb.length() - 1);
		}
		MyProcessDialog.showDialog(mContext);
		PersonalNetwork.getResponseApi().getApplyFOrRefundCommitResponse(order_id, message, pic_str, App.APP_CLIENT_KEY).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
				.subscribe(new BaseSubscriber<ApplyForRefundResponse>() {

					@Override public void onError(Throwable e) {
						e.printStackTrace();
						MyProcessDialog.closeDialog();
						ToastUtils.show(mContext, R.string.string_error);
					}

					@Override public void onNext(ApplyForRefundResponse response) {
						MyProcessDialog.closeDialog();
						if (response.code == 200) {
							if (response.data != null) {
								RxBus.getDefault().post(new BooleanRxbusType(true));
								finish();
								ActivitySlideAnim.slideOutAnim(ApplyForRefundActivity.this);
							}
						} else {
							ToastUtils.show(mContext, response.msg);
						}
					}
				});

	}

	private void bindData(OrderInfoBean bean) {
		id_tv_mobile.setText(bean.store_phone);
		id_tv_total_price.setText("￥" + bean.goods_amount);
	}

	private void setShopText(OrderInfoBean bean) {
		int size = bean.goods_list.size();
		LayoutInflater mInflater = LayoutInflater.from(mContext);

		for (int i = 0; i < size; i++) {
			View shopView = mInflater.inflate(R.layout.layout_my_order_shop, ll_shop, false);
			ImageView iv_shop_img = (ImageView) shopView.findViewById(R.id.iv_shop_img); // 商品图片
			TextView tv_shop_name = (TextView) shopView.findViewById(R.id.tv_shop_name); // 商品名称
			TextView tv_shop_atrribute = (TextView) shopView.findViewById(R.id.tv_shop_atrribute);// 商品属性值
			TextView tv_single_shop_price = (TextView) shopView.findViewById(R.id.tv_single_shop_price); // 单个商品价格 ￥200.0
			TextView tv_shop_num = (TextView) shopView.findViewById(R.id.tv_shop_num); // 商品数量 x1

			MyOrderShopBean myOrderShopBean = bean.goods_list.get(i);
			GlideUtils.loadImage(myOrderShopBean.image_url, iv_shop_img);
			tv_shop_name.setText(myOrderShopBean.goods_name);
			List<GoodsSpecBean> specList = myOrderShopBean.goods_spec;
			if (specList != null && !specList.isEmpty()) {
				tv_shop_atrribute.setVisibility(View.VISIBLE);
				StringBuilder specSb = new StringBuilder();
				for (GoodsSpecBean spec : specList) {
					LogUtils.e("商品属性是" + spec.sp_name + "--" + spec.sp_value_name);
					specSb.append(spec.sp_name + ":" + spec.sp_value_name);
				}
				tv_shop_atrribute.setText("商品属性:" + specSb.toString());
			} else {
				tv_shop_atrribute.setVisibility(View.GONE);
			}
			tv_single_shop_price.setText(myOrderShopBean.goods_price);
			tv_shop_num.setText("x" + myOrderShopBean.goods_num);

			ll_shop.addView(shopView);
		}
	}

	void showPhoto() {
		iv_photo1.setVisibility(View.GONE);
		iv_delete1.setVisibility(View.GONE);
		iv_photo2.setVisibility(View.GONE);
		iv_delete2.setVisibility(View.GONE);
		iv_photo3.setVisibility(View.GONE);
		iv_delete3.setVisibility(View.GONE);
		iv_photo4.setVisibility(View.GONE);
		iv_delete4.setVisibility(View.GONE);
		ivReleaseAddphoto.setVisibility(View.VISIBLE);
		switch (photoUrl.size()) {
			case 4:
				ivReleaseAddphoto.setVisibility(View.GONE);
				iv_photo4.setVisibility(View.VISIBLE);
				iv_delete4.setVisibility(View.VISIBLE);
				GlideUtils.loadImage(photoUrl.get(3), iv_photo4);
			case 3:
				iv_photo3.setVisibility(View.VISIBLE);
				iv_delete3.setVisibility(View.VISIBLE);
				GlideUtils.loadImage(photoUrl.get(2), iv_photo3);
			case 2:
				iv_photo2.setVisibility(View.VISIBLE);
				iv_delete2.setVisibility(View.VISIBLE);
				GlideUtils.loadImage(photoUrl.get(1), iv_photo2);
			case 1:
				iv_photo1.setVisibility(View.VISIBLE);
				iv_delete1.setVisibility(View.VISIBLE);
				GlideUtils.loadImage(photoUrl.get(0), iv_photo1);
				break;
		}
		tv_photonum.setText("还可上传（" + (4 - photoUrl.size()) + "）张");
	}

	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (chooseCameraPopuUtils != null) chooseCameraPopuUtils.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

}
