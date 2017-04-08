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
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
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
    @BindView(R.id.tv_area1)
    TextView tv_area1; // 所在地区
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
    private String province; // 省份
    private String city; // 城市
    private String area; // 地区
    private String name; // 昵称
    private ArrayList<BusinessBean> businessList = new ArrayList<>(); // 行业选择


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
        GlideUtils.loadCircleImage(App.mLoginReponse.avator, iv_head);
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
                                LogUtils.e("获取数据成功。。。" + response.data.member_name + "===头像路径是;" + infoResponse.member_avatar);
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
                                tv_area1.setText(infoResponse.area_info);
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
                headPhoto();

                break;
            case R.id.ll_sex: // 性别
                selectSex();

                break;
            case R.id.ll_area: // 地区
                selectArea();

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

        PersonalNetwork
                .getResponseApi()
                .getUpdatePersonInfoResponse(App.APP_CLIENT_KEY, sexId, business, position, description, avatar, province, city, area, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<String>>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(BaseResponse<String> response) {
                        if (response.code == 200) {
                            if (response.data != null) {
                                // 设置用户属性
                                LogUtils.e("修改个人信息成功。。。" + response.data);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    /*private List<AssetsAreaBean.AreaBean> provincesList = new ArrayList<>();
    private List<List<AssetsAreaBean.AreaBean>> citiesList = new ArrayList<>();
    private List<List<List<AssetsAreaBean.AreaBean>>> areasList = new ArrayList<>();*/

    private List<AssetsAreaBean.AreaBean> options1Items = new ArrayList<>();
    private List<List<AssetsAreaBean.AreaBean>> options2Items = new ArrayList<>();
    private List<List<List<AssetsAreaBean.AreaBean>>> options3Items = new ArrayList<>();

    private void selectArea() {
        String areaJson = FileUtils.getAssetsFile(mContext);
        if (!TextUtils.isEmpty(areaJson)) {
            try { // 解析json
                if (options1Items.size() == 0) {
                    initArea();
                }
                OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        String tx = options1Items.get(options1).getPickerViewText() +
                                options2Items.get(options1).get(options2) +
                                options3Items.get(options1).get(options2).get(options3);

                        Toast.makeText(mContext, tx, Toast.LENGTH_SHORT).show();
                    }
                }).build();
                pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
                pvOptions.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initArea() {
        String areaJson = FileUtils.getAssetsFile(mContext);
        if (!TextUtils.isEmpty(areaJson)) {
            try { // 解析json
                LogUtils.e("解析省份的json");
                AssetsAreaBean bean = (AssetsAreaBean) JsonUtils.toObject(areaJson, AssetsAreaBean.class);
                if (options1Items.isEmpty()) {
                    options1Items.addAll(bean.provinces);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
            }
        });
    }

    private void headPhoto() {
        // 点击选择照片和拍照上传
        new ActionSheetDialog(PersonInfoActivity.this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                try {
                                    // 调用系统摄像头，进行拍照
                                    String state = Environment.getExternalStorageState();
                                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                                        Intent phoneIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        String saveDir = Constant.ROOT;
                                        File dir = new File(saveDir);
                                        if (!dir.exists()) {
                                            dir.mkdir();
                                        }
                                        mFile = new File(saveDir, System.currentTimeMillis() + ".png");
                                        phoneIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
                                        startActivityForResult(phoneIntent, 3);
                                    }
                                } catch (Exception e) {
                                    LogUtils.e(e);
                                }
                            }
                        })
                .addSheetItem("从相册选择", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                try {
                                    // 选择本地文件
                                    Intent fileIntent = new Intent(
                                            Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                            /* 取得相片后返回本画面 */
                                    startActivityForResult(fileIntent, 2);
                                } catch (Exception e) {
                                    LogUtils.e(e);
                                }
                            }
                        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK) {
            // 获取选择本地的图片
            Uri uri = data.getData();
            if (mFile != null) {
                mFile = null;
            }
            if (uri != null) {
                String[] proj = {
                        MediaStore.Images.Media.DATA
                };
                Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
                int actual_image_column_index = actualimagecursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                String img_path = actualimagecursor
                        .getString(actual_image_column_index);
                mFile = new File(img_path);
                if (Build.VERSION.SDK_INT < 14) {
                    actualimagecursor.close();
                }
                uploadHeadPhoto();
            } else {
                // 获取图片
                Bundle extras = data.getExtras();
                mBitmap = (Bitmap) extras.get("data");
                uploadHeadPhoto();
            }
        } else if (requestCode == 3 && resultCode == RESULT_OK) {
            // 获取拍照的图片
            if (mFile != null) {
                // 上传图片
                uploadHeadPhoto();
            }
        }
    }

    private Bitmap mBitmap;

    public void uploadHeadPhoto() {
        if (mFile == null) {
            if (mBitmap != null) {
                String fileName = System.currentTimeMillis() + "";
                ImageFormatUtils.saveBitmapFile(mBitmap, fileName);
                mFile = new File(fileName);
            }
        }
        if (mFile == null) {
            ToastUtils.show(mContext, "请选择图片");
            return;
        }
        // 请求携带的参数
        Map<String, RequestBody> params = new HashMap<>();
        params.put("type", toRequestBody("avatar"));
        params.put("key", toRequestBody(App.APP_CLIENT_KEY));

        // 上传的图片
        //设置Content-Type:application/octet-stream
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
        //设置Content-Disposition:form-data; name="photo"; filename="xuezhiqian.png"
        MultipartBody.Part photo = MultipartBody.Part.createFormData("file", mFile.getName(), photoRequestBody);

        MyProcessDialog.showDialog(PersonInfoActivity.this, "正在上传...");
        PersonalNetwork.getResponseApi()
                .uploadImage(params, photo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<UploadHeadImageReponse>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(UploadHeadImageReponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            resetFileAndBitmap();
                            if (response.data != null) {
                                LogUtils.e("返回上传图片的数据是：" + response.data.url);
                                avatar = response.data.file_name;
                                // 发送更新到个人首页
                                GlideUtils.loadCircleImage(response.data.url, iv_head);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });

    }

    public RequestBody toRequestBody(String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }

    private void resetFileAndBitmap() {
        mFile = null;
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

}
