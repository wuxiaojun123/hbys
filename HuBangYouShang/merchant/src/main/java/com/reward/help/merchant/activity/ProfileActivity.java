package com.reward.help.merchant.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.utils.EaseUserUtils;
import com.idotools.utils.ImageFormatUtils;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;
import com.reward.help.merchant.App;
import com.reward.help.merchant.R;
import com.reward.help.merchant.bean.LoginBean;
import com.reward.help.merchant.bean.Response.UploadHeadImageReponse;
import com.reward.help.merchant.chat.DemoHelper;
import com.reward.help.merchant.chat.ui.BaseActivity;
import com.reward.help.merchant.network.PersonalNetwork;
import com.reward.help.merchant.network.base.BaseSubscriber;
import com.reward.help.merchant.utils.ChooseCameraPopuUtils;
import com.reward.help.merchant.utils.Constant;
import com.reward.help.merchant.utils.DealChoosePicUtils;
import com.reward.help.merchant.utils.GlideUtils;
import com.reward.help.merchant.utils.SpUtils;
import com.reward.help.merchant.view.ActionSheetDialog;
import com.reward.help.merchant.view.MyProcessDialog;

import java.io.File;
import java.util.HashMap;
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
 */

public class ProfileActivity extends BaseActivity implements View.OnClickListener, DealChoosePicUtils.DealChoosePicListener {

    @BindView(R.id.iv_title_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;

    @BindView(R.id.iv_profile_photo)
    ImageView mIvPhoto;

    @BindView(R.id.et_edit_nickname)
    EditText mEtNickname;

    @BindView(R.id.tv_profile_name)
    TextView mTvRealName;

    @BindView(R.id.tv_profile_sex)
    TextView mTvSex;

    @BindView(R.id.tv_profile_store)
    TextView mTvStoreName;


    DealChoosePicUtils dealChoosePicUtils;
    private File mFile;
    private String avatar;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mTvTitle.setText(getText(R.string.profile_info));
        mTvRight.setText(getText(R.string.commit));

        String username = DemoHelper.getInstance().getCurrentUsernName();
        GlideUtils.setUserAvatar(this, username, mIvPhoto);

        LoginBean userInfo = SpUtils.getUserInfo();
        if (userInfo != null && !TextUtils.isEmpty(userInfo.seller_name)) {
            mEtNickname.setText(userInfo.seller_name);
        } else {
            EaseUserUtils.setUserNick(username, mEtNickname);
        }

        if (userInfo != null && !TextUtils.isEmpty(userInfo.store_name)) {
            mTvStoreName.setText(userInfo.store_name);
        }
    }
    @OnClick({R.id.rl_choose_photo,R.id.tv_right,R.id.iv_title_back})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_choose_photo:
                //ChooseCameraPopuUtils.showPopupWindow(this, v);
                //if (dealChoosePicUtils == null) {
                 //   dealChoosePicUtils = new DealChoosePicUtils(this);
                 //   dealChoosePicUtils.setDealChoosePicListener(this);
                //}
                headPhoto();

                break;
            case R.id.iv_title_back:
                hideSoftKeyboard();
                this.finish();
                break;
            case R.id.tv_right:
                break;
        }
    }

    private void headPhoto() {
        new ActionSheetDialog(ProfileActivity.this)
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
            params.put("key", toRequestBody(App.getAppClientKey()));

            // 上传的图片
            //设置Content-Type:application/octet-stream
            RequestBody photoRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
            //设置Content-Disposition:form-data; name="photo"; filename="xuezhiqian.png"
            MultipartBody.Part photo = MultipartBody.Part.createFormData("file", mFile.getName(), photoRequestBody);

            MyProcessDialog.showDialog(ProfileActivity.this, "正在上传...");
            PersonalNetwork.getResponseApi()
                    .uploadImage(params, photo)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<UploadHeadImageReponse>() {
                        @Override
                        public void onError(Throwable e) {
                            MyProcessDialog.closeDialog();
                            e.printStackTrace();
                            ToastUtils.show(mContext, "上传失败");
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
                                    GlideUtils.loadCircleImage(response.data.url, mIvPhoto);
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

    @Override
    public void finishDeal(String path, int type) {
    }
}
