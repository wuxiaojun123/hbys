package com.help.reward.utils;

import android.content.Context;

import com.help.reward.App;
import com.help.reward.bean.Response.UploadImageResponse;
import com.help.reward.network.UploadImageNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ToastUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 2017/3/15.
 */

public class UploadImageUtils {
    OnUploadImageListener onUploadImageListener;
    Context mContext;

    public UploadImageUtils(Context mContext) {
        this.mContext = mContext;
    }

    public interface OnUploadImageListener {
        void onLoadError();

        void onLoadSucced(String default_dir, String file_name);
    }

    public void setOnUploadImageListener(OnUploadImageListener onUploadImageListener) {
        this.onUploadImageListener = onUploadImageListener;
    }

    public void upImage(String imagePath, String type) {
        if (null != imagePath) {
            try {
                File file = new File(imagePath);
                // 创建 RequestBody，用于封装构建RequestBody
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file);

                // MultipartBody.Part  和后端约定好Key，这里的partName是用image
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                // 添加描述
                String descriptionString = "hello, 这是文件描述";
                RequestBody description =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), descriptionString);
                MyProcessDialog.showDialog(mContext);
                UploadImageNetwork
                        .getUploadApi()
                        .getUploadBean(App.APP_CLIENT_KEY, type, description, body)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseSubscriber<UploadImageResponse>() {
                            @Override
                            public void onError(Throwable e) {
                                MyProcessDialog.closeDialog();
                                e.printStackTrace();
                                ToastUtils.show(mContext, "上传图片失败");
                            }

                            @Override
                            public void onNext(UploadImageResponse response) {
                                MyProcessDialog.closeDialog();
                                if (response.code == 200) {
                                    if (onUploadImageListener != null) {
                                        onUploadImageListener.onLoadSucced(response.data.default_dir, response.data.default_dir);
                                    }
                                } else {
                                    ToastUtils.show(mContext, response.msg);
                                }
                            }
                        });
            } catch (Exception e) {

            }
        }
    }
}
