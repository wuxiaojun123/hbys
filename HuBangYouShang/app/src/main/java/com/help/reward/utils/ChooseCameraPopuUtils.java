package com.help.reward.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.help.reward.R;

import java.io.File;

/*
 * 图片选择弹出框
 */
public class ChooseCameraPopuUtils {

    private static final String TAG = "PopupShareMenuUtil";
    public static final int TYPE_OPEN_IMAGE = 0;
    public static final String PIC_CAMERA_IMG_DIR = "helprewardupImg";
    public static final String PIC_CAMERA_IMG_NAME = "camera.png";
    /**
     * 调用摄像头或从相册选取照片
     */
    public static final int PIC_RROM_CAMERA = 13;
    public static final int PIC_RROM_PHONO = 14;

    public static void showPopupWindow(
            Activity activity, View root) {
        final PopupWindow poup = initPopuptWindow(activity);
        poup.setWidth(LayoutParams.MATCH_PARENT);
        poup.setHeight(LayoutParams.MATCH_PARENT);
        poup.showAtLocation(root, Gravity.BOTTOM, 0, 0);
        setClickListener(activity, poup);

        final OnClickListener l = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (poup.isShowing())
                    poup.dismiss();
            }
        };

        poup.getContentView().setOnClickListener(l);
        poup.getContentView().findViewById(R.id.cancle_query)
                .setOnClickListener(l);
    }


    private static PopupWindow initPopuptWindow(Activity activity) {
        // 获取自定义布局文件dialog.xml的视图
        View popupWindow_view = LayoutInflater.from(activity).inflate(
                R.layout.choose_camera_pic_way, null);


        // 使用下面的构造方法
        PopupWindow popupWindow = new PopupWindow(popupWindow_view,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        // 这里注意 必须要有一个背景 ，有了背景后
        // 当你点击对话框外部的时候或者按了返回键的时候对话框就会消失，当然前提是使用的构造函数中Focusable为true
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        return popupWindow;
    }

    private static void setClickListener(
            Activity activity, PopupWindow poup) {
        View view = poup.getContentView();
        view.findViewById(R.id.camera_pic).setOnClickListener(
                new ClickListener(activity, poup));
        view.findViewById(R.id.galler_pic).setOnClickListener(
                new ClickListener(activity, poup));
    }

    private static class ClickListener implements OnClickListener {
        Activity mActivity;
        PopupWindow mPopupWindow;

        ClickListener(Activity activity,
                      PopupWindow poup) {
            mPopupWindow = poup;
            mActivity = activity;
        }

        @Override
        public void onClick(View view) {
            dissPoup(mPopupWindow);
            switch (view.getId()) {
                case R.id.camera_pic:
                    String status = Environment.getExternalStorageState();
                    if (status.equals(Environment.MEDIA_MOUNTED)) {
                        try {
                            File dir = new File(
                                    Environment.getExternalStorageDirectory() + "/"
                                            + PIC_CAMERA_IMG_DIR);
                            if (!dir.exists())
                                dir.mkdirs();
                            File f = new File(dir, PIC_CAMERA_IMG_NAME);// localTempImgDir和localTempImageFileName是自己定义的名字
                            Uri u = Uri.fromFile(f);
                            Intent intent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);

                            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                            mActivity.startActivityForResult(intent,
                                    PIC_RROM_CAMERA);
                        } catch (ActivityNotFoundException e) {
                            // TODO Auto-generated catch block
                            Toast.makeText(mActivity, "没有找到储存目录", Toast.LENGTH_LONG)
                                    .show();
                        }
                    } else {
                        Toast.makeText(mActivity, "没有储存卡", Toast.LENGTH_LONG)
                                .show();
                    }

                    break;
                case R.id.galler_pic:
                    openImage(TYPE_OPEN_IMAGE);
                    break;
                default:
                    break;
            }
        }


        private void openImage(int type) {
            try {
//				Uri uri =  Uri.parse("content://media/external/images/media/*");
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                mActivity.startActivityForResult(intent,
                        PIC_RROM_PHONO);

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();

                Intent localIntent = new Intent();
                localIntent.setType("image/*");
                localIntent.setAction("android.intent.action.GET_CONTENT");
                Intent localIntent2 = Intent.createChooser(localIntent, "选择图片");
                mActivity.startActivityForResult(localIntent2,
                        PIC_RROM_PHONO);
            }
        }
    }

    private static void dissPoup(PopupWindow poup) {
        if (null != poup && poup.isShowing())
            poup.dismiss();
    }


}
