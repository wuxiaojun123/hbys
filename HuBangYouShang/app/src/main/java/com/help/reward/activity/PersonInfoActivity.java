package com.help.reward.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.Response.MyCollectionStoreResponse;
import com.help.reward.bean.Response.PersonInfoResponse;
import com.help.reward.bean.Response.UploadHeadImageReponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.Constant;
import com.help.reward.utils.GlideUtils;
import com.help.reward.view.ActionSheetDialog;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ImageFormatUtils;
import com.idotools.utils.LogUtils;
import com.idotools.utils.SharedPreferencesHelper;
import com.idotools.utils.ToastUtils;

import java.io.File;

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


                break;
            case R.id.rl_head: // 点击出现拍照，相册，取消
                headPhoto();

                break;
            case R.id.ll_sex: // 性别


                break;
            case R.id.ll_area: // 地区


                break;
            case R.id.ll_work: // 行业


                break;
        }
    }

    private File mFile;

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



    /**
     * 上传头像
     */
    public void uploadHeadPhoto() {
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
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
        // MultipartBody.Part  和后端约定好Key，这里的partName是用image
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", mFile.getName(), requestFile);
        /*// key
        String keyStr = "key="+App.APP_CLIENT_KEY;
        RequestBody key =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), keyStr);
        String typeStr = "type="+App.APP_CLIENT_KEY;
        RequestBody type =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), typeStr);*/
        // avatar
        MyProcessDialog.showDialog(PersonInfoActivity.this,"正在上传...");
        PersonalNetwork.getResponseApi()
                .getUploadHeadImageReponse(requestFile,"avatar",App.APP_CLIENT_KEY)
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
                            LogUtils.e("获取数据成功。。。" + response.data);
                            if (response.data != null) {
                                LogUtils.e("返回上传图片的数据是："+response.data.default_dir);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });

    }

    private void resetFileAndBitmap(){
        mFile = null;
        if(mBitmap != null && !mBitmap.isRecycled()){
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    /***
     * 压缩图片
     *
     * @return
     */
    /*private Bitmap compressBitmap() {
        try {
            // 获得图片的宽和高，并不把图片加载到内存中
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mFile.getAbsolutePath(), options);
            options.inSampleSize = ImageUtil.calculateInSampleSize(options, iv_head.getWidth(), iv_head.getHeight());
            // 使用获得到的InSampleSize再次解析图片
            options.inJustDecodeBounds = false;
            Bitmap bm = BitmapFactory.decodeFile(mFile.getAbsolutePath(), options);
            return bm;
        } catch (Exception e) {
        }
        return null;
    }*/

}
