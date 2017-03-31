package com.help.reward.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.help.reward.R;
import com.help.reward.activity.MainActivity;
import com.help.reward.activity.MsgCenterActivity;
import com.help.reward.activity.ShopcartActivity;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.DisplayUtil;
import com.idotools.utils.ToastUtils;

/*
 *商铺右上角menu
 */
public class StoreInfoMenuPop {

    public static void showPopupWindow(
            Activity activity, View root) {
        View popupWindow_view = LayoutInflater.from(activity).inflate(
                R.layout.pop_storeinfomenu, null);


        // 使用下面的构造方法
        PopupWindow popupWindow = new PopupWindow(popupWindow_view,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setWidth(DisplayUtil.dip2px(activity, 90));
        popupWindow_view.findViewById(R.id.layout_home).setOnClickListener(
                new ClickListener(activity, popupWindow));
        popupWindow_view.findViewById(R.id.layout_search).setOnClickListener(
                new ClickListener(activity, popupWindow));
        popupWindow_view.findViewById(R.id.layout_car).setOnClickListener(
                new ClickListener(activity, popupWindow));
        popupWindow_view.findViewById(R.id.layout_message).setOnClickListener(
                new ClickListener(activity, popupWindow));

        popupWindow_view.setOnClickListener(new ClickListener(activity, popupWindow));
        popupWindow.showAsDropDown(root, 0 - DisplayUtil.dip2px(activity, 20), 0 - DisplayUtil.dip2px(activity, 10));

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
                case R.id.layout_home:
                    mActivity.startActivity(new Intent(mActivity, MainActivity.class));
                    ActivitySlideAnim.slideInAnim(mActivity);
                    mActivity.finish();
                    break;
                case R.id.layout_search:
                    ToastUtils.show(mActivity, "搜索");
//                    mActivity.startActivity(new Intent(mActivity, ShopcartActivity.class));
//                    ActivitySlideAnim.slideInAnim(mActivity);
                    break;
                case R.id.layout_car:
                    mActivity.startActivity(new Intent(mActivity, ShopcartActivity.class));
                    ActivitySlideAnim.slideInAnim(mActivity);
                    break;
                case R.id.layout_message:
                    mActivity.startActivity(new Intent(mActivity, MsgCenterActivity.class));
                    ActivitySlideAnim.slideInAnim(mActivity);
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

}
