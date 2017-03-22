package com.help.reward.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.help.reward.R;
import com.help.reward.utils.DialogUtil;

/*
 * 图片选择弹出框
 */
public class ChooseCommentTypePop {

    public void showPopupWindow(
            Activity activity, View root) {
        View popupWindow_view = LayoutInflater.from(activity).inflate(
                R.layout.pop_choosecommenttype, null);


        // 使用下面的构造方法
        PopupWindow popupWindow = new PopupWindow(popupWindow_view,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow_view.findViewById(R.id.tv_type1).setOnClickListener(
                new ClickListener(activity, popupWindow));
        popupWindow_view.findViewById(R.id.tv_type2).setOnClickListener(
                new ClickListener(activity, popupWindow));
        popupWindow_view.setOnClickListener(new ClickListener(activity, popupWindow));

        popupWindow_view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int[] location = new int[2];
        root.getLocationOnScreen(location);
        popupWindow.showAtLocation(root, Gravity.NO_GRAVITY, location[0], location[1]-popupWindow_view.getMeasuredHeight()*7/6);
    }


    private class ClickListener implements OnClickListener {
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
                case R.id.tv_type1:
                    if (onTypeChooseListener != null) {
                        onTypeChooseListener.onType("公聊");
                    }
                    break;
                case R.id.tv_type2:
                    DialogUtil.showConfirmCancleDialog(mActivity, "系统提示", "您即将进入私聊模式\n聊天内容仅您与发帖人可见", new DialogUtil.OnDialogUtilClickListener() {
                        @Override
                        public void onClick(boolean isLeft) {
                            if (isLeft&&onTypeChooseListener != null) {
                                onTypeChooseListener.onType("私聊");
                            }
                        }
                    });

                    break;
                default:
                    break;
            }
        }
    }

    private static void dissPoup(PopupWindow poup) {
        if (null != poup && poup.isShowing())
            poup.dismiss();
    }

    OnTypeChooseListener onTypeChooseListener;

    public interface OnTypeChooseListener {
        void onType(String type);
    }

    public void setOnTypeChooseListener(OnTypeChooseListener onTypeChooseListener) {
        this.onTypeChooseListener = onTypeChooseListener;
    }
}
