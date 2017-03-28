package com.help.reward.utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.Response.UploadHeadImageReponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.view.ActionSheetDialog;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ImageFormatUtils;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/*
 * 图片选择弹出框
 */
public class ChooseCameraPopuUtils {
    Activity activity;

    private File mFile;
    private Bitmap mBitmap;
    String type;
    public ChooseCameraPopuUtils(Activity activity,String type){
        this.activity=activity;
        this.type=type;
    }

    public void showPopupWindow() {
        // 点击选择照片和拍照上传
        new ActionSheetDialog(activity)
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
                                        activity.startActivityForResult(phoneIntent, 3);
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
                                    activity.startActivityForResult(fileIntent, 2);
                                } catch (Exception e) {
                                    LogUtils.e(e);
                                }
                            }
                        }).show();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                Cursor actualimagecursor = activity.managedQuery(uri, proj, null, null, null);
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


    public void uploadHeadPhoto(){
        if(mFile == null){
            if(mBitmap != null){
                String fileName = System.currentTimeMillis()+"";
                ImageFormatUtils.saveBitmapFile(mBitmap,fileName);
                mFile = new File(fileName);
            }
        }
        if(mFile == null){
            ToastUtils.show(activity,"请选择图片");
            return;
        }
        // 请求携带的参数
        Map<String,RequestBody> params = new HashMap<>();
        params.put("type",toRequestBody(type));
        params.put("key",toRequestBody(App.APP_CLIENT_KEY));

        // 上传的图片
        //设置Content-Type:application/octet-stream
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
        //设置Content-Disposition:form-data; name="photo"; filename="xuezhiqian.png"
        MultipartBody.Part photo = MultipartBody.Part.createFormData("file", mFile.getName(), photoRequestBody);

        MyProcessDialog.showDialog(activity,"正在上传...");
        PersonalNetwork.getResponseApi()
                .uploadImage(params,photo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<UploadHeadImageReponse>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        ToastUtils.show(activity, R.string.string_error);
                    }

                    @Override
                    public void onNext(UploadHeadImageReponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            resetFileAndBitmap();
                            if (response.data != null) {
                                LogUtils.e("返回上传图片的数据是："+response.data.url+"===="+response.data.file_name);
                                // 发送更新到个人首页
                                if (onUploadImageListener != null) {
                                    onUploadImageListener.onLoadSucced(response.data.file_name,response.data.url);
                                }
                            }
                        } else {
                            ToastUtils.show(activity, response.msg);
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

    public interface OnUploadImageListener {
        void onLoadError();

        void onLoadSucced(String file_name,String url);
    }
    OnUploadImageListener onUploadImageListener;
    public void
    setOnUploadImageListener(OnUploadImageListener onUploadImageListener) {
        this.onUploadImageListener = onUploadImageListener;
    }
}
