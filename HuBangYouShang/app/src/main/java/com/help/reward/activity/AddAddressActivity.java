package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.AddressBean;
import com.help.reward.bean.Response.AddAddressResponse;
import com.help.reward.bean.Response.AddressResponse;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.manager.AddressManager;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chihane.jdaddressselector.BottomDialog;
import chihane.jdaddressselector.OnAddressSelectedListener;
import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;
import chihane.jdaddressselector.model.Street;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 添加地址
 * Created by wuxiaojun on 2017/1/8.
 */

public class AddAddressActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_phone_number)
    EditText et_phone_number;
    @BindView(R.id.tv_area)
    TextView tv_area;
    @BindView(R.id.et_address)
    EditText et_address;

    private String provinceID; // 省份
    private String cityID; // 城市
    private String countryID; // 地区

    private String address_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        address_id = getIntent().getStringExtra("address_id");
        initView();
        if(!TextUtils.isEmpty(address_id)){
            initNet();
        }
    }

    private void initNet() {
        // AddAddressResponse
        PersonalNetwork
                .getResponseApi()
                .getEditAddressResponse(App.APP_CLIENT_KEY,address_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<AddAddressResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(AddAddressResponse response) {
                        if (response.code == 200) {
                            if (response.data != null) {
                                bindView(response.data);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    /****
     * 绑定数据
     * @param data
     */
    private void bindView(AddressBean data) {
        et_name.setText(data.true_name);
        et_phone_number.setText(data.mob_phone);
        et_address.setText(data.address);
    }

    private void initView() {
        tv_title.setText(R.string.string_add_address_title);
        tv_title_right.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_title_back, R.id.tv_area, R.id.btn_save_address})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(AddAddressActivity.this);

                break;
            case R.id.tv_area:
                final BottomDialog dialog = new BottomDialog(AddAddressActivity.this);
                dialog.setOnAddressSelectedListener(new OnAddressSelectedListener() {
                    @Override
                    public void onAddressSelected(Province province, City city, County county, Street street) {
                        LogUtils.e("选择的是:" + province.name + "----" + city + "----country" + county);
                        String area = (province == null ? "" : province.name) +
                                (city == null ? "" : "" + city.name) +
                                (county == null ? "" : "" + county.name) +
                                (street == null ? "" : "" + street.name);

                        provinceID = province.id + "";
                        cityID = city.id + "";
                        countryID = county.id + "";

                        tv_area.setText(area);
                        dialog.dismiss();
                    }
                });
                dialog.getAddressSelector().setAddressProvider(new AddressManager());
                dialog.show();

                break;
            case R.id.btn_save_address:
                saveAddress();

                break;
        }
    }

    /***
     * 保存地址
     */
    private void saveAddress() {
        String trueName = et_name.getText().toString().trim();
        if (TextUtils.isEmpty(trueName)) {
            ToastUtils.show(mContext, "请输入收货人姓名");
            return;
        }
        String phoneNumber = et_phone_number.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtils.show(mContext, "请输入收货人的手机号码");
            return;
        }
        String detailAddress = et_address.getText().toString().trim();
        if (TextUtils.isEmpty(detailAddress)) {
            ToastUtils.show(mContext, "请输入详细地址");
            return;
        }

        /*PersonalNetwork
                .getResponseApi()
                .getAddAddressResponse(App.APP_CLIENT_KEY, trueName,)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<String>>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(BaseResponse<String> response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if (response.data != null) { // 删除成功
                                ToastUtils.show(mContext, response.msg);
                                remove(position);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });*/

    }


}
