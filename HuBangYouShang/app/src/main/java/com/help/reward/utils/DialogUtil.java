package com.help.reward.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.help.reward.R;

public class DialogUtil {
	/*
	 * 显示确认取消弹框
	 */
	public static AlertDialog showConfirmCancleDialog(Activity context,
			CharSequence title, OnDialogUtilClickListener onDialogUtilClickListener) {
		return showConfirmCancleDialog(context, title, "取消", "确定",
				onDialogUtilClickListener);
	}

	public static AlertDialog showConfirmCancleDialog(Activity context,
			CharSequence title, CharSequence textCancelBtn, CharSequence textOKBtn,
			OnDialogUtilClickListener onDialogUtilClickListener) {
		return showConfirmCancleDialog(context, title, "", textCancelBtn,
				textOKBtn, onDialogUtilClickListener);
	}

	public static AlertDialog showConfirmCancleDialog(Activity context,
			CharSequence title, CharSequence content,
			OnDialogUtilClickListener onDialogUtilClickListener) {
		return showConfirmCancleDialog(context, title, content, "取消", "确定",
				onDialogUtilClickListener);
	}

	private static AlertDialog getDialog(Activity context, CharSequence title,
			CharSequence content, CharSequence textCancelBtn,
			CharSequence textOKBtn,
			final OnDialogUtilClickListener onDialogUtilClickListener) {
		final AlertDialog dialog = new AlertDialog.Builder(context).create();

		// dialog.setView(context.getLayoutInflater().inflate(R.layout.confirm_cancle_dialog,
		// null));
		dialog.show();
		Window window = dialog.getWindow();
		window.setGravity(Gravity.CENTER);
		int width = ScreenUtils.getScreenWidth(context) * 3 / 4;
		window.setLayout(width,
				android.view.WindowManager.LayoutParams.WRAP_CONTENT);
		View view = context.getLayoutInflater().inflate(
				R.layout.confirm_cancle_dialog, null);
		window.setContentView(view);//

		((TextView) view.findViewById(R.id.title)).setText(title);
		TextView cancel = (TextView) view.findViewById(R.id.cancle);
		TextView confirm = (TextView) view.findViewById(R.id.confirm);
		TextView contentTv = (TextView) view.findViewById(R.id.content);
		if (StringUtils.checkStr(content)) {
			contentTv.setVisibility(View.VISIBLE);
			contentTv.setText(content);
		} else {
			contentTv.setVisibility(View.GONE);
		}
		if (StringUtils.checkStr(textOKBtn)) {
			confirm.setText(textOKBtn);
		}
		if (StringUtils.checkStr(textCancelBtn)) {
			cancel.setText(textCancelBtn);
			cancel.setVisibility(View.VISIBLE);
		}else{
			cancel.setVisibility(View.GONE);
		}

		confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onDialogUtilClickListener!=null){
					onDialogUtilClickListener.onClick(true);
				}
				dialog.dismiss();
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onDialogUtilClickListener!=null){
					onDialogUtilClickListener.onClick(false);
				}
				dialog.dismiss();
			}
		});
		return dialog;
	}

	public static AlertDialog showConfirmCancleDialog(Activity context,
			CharSequence title, CharSequence msp, CharSequence textCancelBtn,
			CharSequence textOKBtn,
			OnDialogUtilClickListener onDialogUtilClickListener) {
		return getDialog(context, title, msp, textCancelBtn, textOKBtn,
				onDialogUtilClickListener);
	}



	public interface OnDialogUtilClickListener {
		void onClick(boolean isLeft);
	}

	OnDialogUtilClickListener onDialogUtilClickListener;

	public void setOnDialogUtilClickListener(
			OnDialogUtilClickListener onDialogUtilClickListener) {
		this.onDialogUtilClickListener = onDialogUtilClickListener;
	}
}
