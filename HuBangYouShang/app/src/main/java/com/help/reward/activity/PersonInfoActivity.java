package com.help.reward.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.AssetsAreaBean;
import com.help.reward.bean.BusinessBean;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.BusinessResponse;
import com.help.reward.bean.Response.PersonInfoResponse;
import com.help.reward.bean.Response.UploadHeadImageReponse;
import com.help.reward.bean.SexBean;
import com.help.reward.manager.AddressManager;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.UpdateLoginDataRxbusType;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.ChooseCameraPopuUtils;
import com.help.reward.utils.Constant;
import com.help.reward.utils.GlideUtils;
import com.help.reward.utils.JsonUtils;
import com.help.reward.utils.PickerUtils;
import com.help.reward.view.ActionSheetDialog;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.FileUtils;
import com.idotools.utils.ImageFormatUtils;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chihane.jdaddressselector.BottomDialog;
import chihane.jdaddressselector.OnAddressSelectedListener;
import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;
import chihane.jdaddressselector.model.Street;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 个人信息
 * <p>
 * Created by wuxiaojun on 2017/1/8.
 */

public class PersonInfoActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.rl_head)
    RelativeLayout rl_head; // 用户点击头像即可拍照或者从相册选择
    @BindView(R.id.tv_area)
    TextView tv_area; // 所在地区
    @BindView(R.id.tv_sex)
    TextView tv_sex; // 性别
    @BindView(R.id.tv_code)
    TextView tv_code; // 邀请码
    @BindView(R.id.et_nicheng)
    TextView et_nicheng; // 昵称
    @BindView(R.id.iv_head)
    ImageView iv_head; // 头像
    @BindView(R.id.tv_work)
    TextView tv_work; // 行业
    @BindView(R.id.et_word_position)
    TextView et_word_position; // 职位
    @BindView(R.id.et_sign)
    EditText et_sign; // 个性签名

    private ArrayList<SexBean> sexList = new ArrayList<>(3);
    private File mFile;

    private String sexId; // 性别
    private String business; // 行业
    private String position; // 职位
    private String description; // 描述
    private String avatar; // 头像
    private String provinceID; // 省份
    private String cityID; // 城市
    private String countryID; // 地区
    private String name; // 昵称
    private ArrayList<BusinessBean> businessList = new ArrayList<>(); // 行业选择

    ChooseCameraPopuUtils chooseCameraPopuUtils;
    private String avatarUrl; // 头像连接

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);
        initView();
        initNetwork();
    }

    private void initView() {
        tv_title.setText(R.string.string_person_info_title);
        tv_title_right.setText("提交");
        sexList.add(new SexBean("0", "保密"));
        sexList.add(new SexBean("1", "男"));
        sexList.add(new SexBean("2", "女"));
        if (App.mLoginReponse != null) {
            avatarUrl = App.mLoginReponse.avator;
            GlideUtils.loadCircleImage(App.mLoginReponse.avator, iv_head);
        }

        chooseCameraPopuUtils = new ChooseCameraPopuUtils(this, "avatar");
        chooseCameraPopuUtils.setOnUploadImageListener(new ChooseCameraPopuUtils.OnUploadImageListener() {
            @Override
            public void onLoadError() {
                ToastUtils.show(mContext, "选择相片出错");
            }

            @Override
            public void onLoadSucced(String file_name, String url) {
                LogUtils.e("头像修改的路径是：" + url);
                GlideUtils.loadCircleImage(url, iv_head);
                avatar = file_name;
                avatarUrl = url;
            }
        });
    }

    private void initNetwork() {
        PersonalNetwork
                .getResponseApi()
                .getPersonInfoResponse(App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<PersonInfoResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(PersonInfoResponse response) {
                        if (response.code == 200) {
                            if (response.data != null) {
                                // 设置用户属性
                                PersonInfoResponse infoResponse = response.data;
//                                LogUtils.e("获取数据成功。。。" + response.data.member_name + "===头像路径是;" + infoResponse.member_avatar);
                                LogUtils.e("当前路径是：" + infoResponse.member_avatar);
                                GlideUtils.loadCircleImage(infoResponse.member_avatar, iv_head);

                                et_nicheng.setText(infoResponse.member_name);
                                tv_code.setText(infoResponse.invitation_code);
                                if ("0".equals(infoResponse.member_sex)) {
                                    tv_sex.setText("保密");
                                    sexId = "0";
                                } else if ("1".equals(infoResponse.member_sex)) {
                                    tv_sex.setText("男");
                                    sexId = "1";
                                } else {
                                    tv_sex.setText("女");
                                    sexId = "0";
                                }
                                tv_area.setText(infoResponse.area_info);
                                LogUtils.e("当前行业是：" + infoResponse.member_business);
                                business = infoResponse.member_business;
                                tv_work.setText(infoResponse.member_business);
                                et_word_position.setText(infoResponse.member_position);
                                et_sign.setText(infoResponse.description);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });

        PersonalNetwork
                .getResponseApi()
                .getBusinessResponse("business")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BusinessResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(BusinessResponse response) {
                        if (response.code == 200) {
                            if (response.data != null) {
                                // 设置用户属性
                                ArrayList<String> list = response.data.business;
                                for (String business : list) {
                                    businessList.add(new BusinessBean(business));
                                }
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    @OnClick({R.id.iv_title_back, R.id.tv_title_right, R.id.rl_head, R.id.ll_sex, R.id.ll_area, R.id.ll_work})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(PersonInfoActivity.this);

                break;
            case R.id.tv_title_right: // 提交
                commitPersonInfo();

                break;
            case R.id.rl_head: // 点击出现拍照，相册，取消
                chooseCameraPopuUtils.showPopupWindow();

                break;
            case R.id.ll_sex: // 性别
                selectSex();

                break;
            case R.id.ll_area: // 地区
//                selectArea();
                final BottomDialog dialog = new BottomDialog(PersonInfoActivity.this);
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
                        if (county != null) {
                            countryID = county.id + "";
                        } else {
                            countryID = "";
                        }

                        tv_area.setText(area);
                        dialog.dismiss();
                    }
                });
                dialog.getAddressSelector().setAddressProvider(new AddressManager());
                dialog.show();

                break;
            case R.id.ll_work: // 行业
                selectBusiness();

                break;
        }
    }

    /***
     * 更新个人信息
     */
    private void commitPersonInfo() {
        position = et_word_position.getText().toString().trim(); // 职位
        name = et_nicheng.getText().toString().trim(); //昵称
        description = et_sign.getText().toString().trim(); // 描述

        MyProcessDialog.showDialog(mContext, "提交中...", true, false);
        PersonalNetwork
                .getResponseApi()
                .getUpdatePersonInfoResponse(App.APP_CLIENT_KEY, sexId, business, position, description, avatar, provinceID, cityID, countryID, name)
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
                        ToastUtils.show(mContext, response.msg);
                        if (response.code == 200) {
                            if (response.data != null) {
                                // 设置用户属性
//                                LogUtils.e("修改个人信息成功。。。" + response.data);
                                if (avatarUrl != null) {
                                    App.mLoginReponse.avator = avatarUrl;
                                    App.mLoginReponse.username = name;
                                }
                                // 发送通知进行更新
                                UpdateLoginDataRxbusType updateLoginDataRxbusType = new UpdateLoginDataRxbusType(false);
                                updateLoginDataRxbusType.setUpdatePersonInfo(true);
                                RxBus.getDefault().post(updateLoginDataRxbusType);
                                finish();
                                ActivitySlideAnim.slideOutAnim(PersonInfoActivity.this);
                            }
                        }
                    }
                });
    }

    private void selectSex() {
        PickerUtils.alertBottomWheelOption(this, sexList, new PickerUtils.OnWheelViewClick() {
            @Override
            public void onClick(View view, int postion) {
                tv_sex.setText(sexList.get(postion).sex_name);
                sexId = sexList.get(postion).sex_id;
            }
        });
    }

    private void selectBusiness() {
        PickerUtils.alertBottomWheelOption(PersonInfoActivity.this, businessList, new PickerUtils.OnWheelViewClick() {
            @Override
            public void onClick(View view, int postion) {
                tv_work.setText(businessList.get(postion).business_name);
                business = businessList.get(postion).business_name;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (chooseCameraPopuUtils != null)
            chooseCameraPopuUtils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
