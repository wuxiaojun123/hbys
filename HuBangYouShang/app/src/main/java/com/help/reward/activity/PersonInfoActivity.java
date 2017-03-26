package com.help.reward.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.AssetsAreaBean;
import com.help.reward.bean.Response.AeraResponse;
import com.help.reward.bean.Response.MyCollectionStoreResponse;
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
import com.idotools.utils.SharedPreferencesHelper;
import com.idotools.utils.ToastUtils;
import com.lvfq.pickerview.OptionsPickerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
        sexList.add(new SexBean("0","保密"));
        sexList.add(new SexBean("1","男"));
        sexList.add(new SexBean("2","女"));
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
                            LogUtils.e("获取数据成功。。。" + response.data.member_name);
                            if (response.data != null) {
                                // 设置用户属性
                                PersonInfoResponse infoResponse = response.data;
                                GlideUtils.loadCircleImage(infoResponse.member_avatar, iv_head);

                                et_nicheng.setText(infoResponse.member_name);
                                tv_code.setText(infoResponse.invitation_code);
                                if ("0".equals(infoResponse.member_sex)) {
                                    tv_sex.setText("保密");
                                } else if ("1".equals(infoResponse.member_sex)) {
                                    tv_sex.setText("男");
                                } else {
                                    tv_sex.setText("女");
                                }
                                tv_area.setText(infoResponse.area_info);
                                tv_work.setText(infoResponse.member_business);
                                et_word_position.setText(infoResponse.member_position);
                                et_sign.setText(infoResponse.description);
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


                break;
        }
    }

    /***
     * 更新个人信息
     */
    private void commitPersonInfo() {



    }

    private ArrayList<AssetsAreaBean.AreaBean> provincesList;
    private ArrayList<ArrayList<AssetsAreaBean.AreaBean>> citiesList = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<AssetsAreaBean.AreaBean>>> areasList = new ArrayList<>();

    private void selectArea() {
        String areaJson = FileUtils.getAssetsFile(mContext);
        if(!TextUtils.isEmpty(areaJson)){
            try { // 解析json
                AssetsAreaBean bean = (AssetsAreaBean) JsonUtils.fromJsonArray(areaJson, AssetsAreaBean.class);
                provincesList = bean.provinces;
                ArrayList<AssetsAreaBean.AreaBean> citiesList1 = bean.cities;
                ArrayList<AssetsAreaBean.AreaBean> areasList1 = bean.areas;

                for (AssetsAreaBean.AreaBean provincesBean:provincesList){

                    ArrayList citySonArrayList = new ArrayList<AssetsAreaBean.AreaBean>();

                    citiesList.add(citySonArrayList);

                    for (AssetsAreaBean.AreaBean citiesbean:citiesList1){

                        if(citiesbean.area_parent_id.equals(provincesBean.area_id)){ // 如果城市的父类id等于省份的id，那么需要添加
                            citySonArrayList.add(citiesbean);

                            ArrayList areasSonArrayList = new ArrayList<AssetsAreaBean.AreaBean>();

                            for (AssetsAreaBean.AreaBean areasbean:areasList1){


                            }
                        }
                    }

                }

                OptionsPickerView<AssetsAreaBean.AreaBean> mAreaPickerView = new OptionsPickerView<>(this);
                mAreaPickerView.setPicker(provincesList,citiesList,areasList,true);

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

    public void uploadHeadPhoto(){
        if(mFile == null){
            if(mBitmap != null){
                String fileName = System.currentTimeMillis()+"";
                ImageFormatUtils.saveBitmapFile(mBitmap,fileName);
                mFile = new File(fileName);
            }
        }
        if(mFile == null){
            ToastUtils.show(mContext,"请选择图片");
            return;
        }
        // 请求携带的参数
        Map<String,RequestBody> params = new HashMap<>();
        params.put("type",toRequestBody("avatar"));
        params.put("key",toRequestBody(App.APP_CLIENT_KEY));

        // 上传的图片
        //设置Content-Type:application/octet-stream
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
        //设置Content-Disposition:form-data; name="photo"; filename="xuezhiqian.png"
        MultipartBody.Part photo = MultipartBody.Part.createFormData("file", mFile.getName(), photoRequestBody);

        MyProcessDialog.showDialog(PersonInfoActivity.this,"正在上传...");
        PersonalNetwork.getResponseApi()
                .uploadImage(params,photo)
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
                                LogUtils.e("返回上传图片的数据是："+response.data.url+"===="+response.data.file_name);
                                // 发送更新到个人首页
                                GlideUtils.loadCircleImage(response.data.url,iv_head);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });

    }

    public RequestBody toRequestBody(String value){
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"),value);
        return body;
    }

    private void resetFileAndBitmap(){
        mFile = null;
        if(mBitmap != null && !mBitmap.isRecycled()){
            mBitmap.recycle();
            mBitmap = null;
        }
    }

}
