package com.wxj.hbys.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.wxj.hbys.R;

public class LoadingDialog extends Dialog {

	private ImageView iv_loading;
	private Context context;
	public LoadingDialog(Context context) {
		super(context, R.style.style_dialog);
		this.context = context;
		setDisplay();
	}

	private void setDisplay() {
		setContentView(R.layout.dialog_loading);
		setCancelable(false);
		iv_loading = (ImageView) findViewById(R.id.iv_loading);
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.loading_rotate);
		iv_loading.startAnimation(anim);
		setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dismiss();
			}
		});
		setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				iv_loading.clearAnimation();
			}
		});
		setProperty();
	}

	public void setProperty() {
		Window window = getWindow();// 　　　得到对话框的窗口．
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;// 设置对话框的位置．0为中间
		wl.y = 0;
		wl.alpha = 1;// 设置对话框的透明度
		wl.gravity = Gravity.CENTER;
		window.setAttributes(wl);
	}
}
