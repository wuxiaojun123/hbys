package com.help.reward.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.help.reward.R;

public class NumSetDialog extends Dialog implements
		View.OnClickListener {

	private Context context;
	private TextView mDialogTitle;
	private Button mBtOk;
	private Button mBtCancel;
	private ImageButton mIbNumDes;
	private ImageButton mIbNumAdd;
	private EditText mEtNum;
	private int numShow = -1;
	/**
	 * 确定按钮文字,背景资源,id
	 */
	private String mOkText;
	private int mOKResid;
	private View.OnClickListener mOKListener;
	/**
	 * 确定按钮文字,背景资源,id
	 */
	private String mCancleText;
	private int mCancleResid;
	private String titleText;
	private View.OnClickListener mCancleListener;
	private  int Max_num = 99;

	public NumSetDialog(Context context) {
		super(context);
		this.context = context;
	}

	public NumSetDialog(Context context, int theme,int Max_num) {
		super(context, theme);
		this.context = context;
		this.Max_num = Max_num;
	}

	public NumSetDialog(Context context, int theme, String title) {
		super(context, theme);
		this.context = context;
		this.titleText = title;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.number_dialog);
		getWindow().setSoftInputMode(
				LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		mDialogTitle = (TextView) findViewById(R.id.num_dialog_title_txt);
		if (titleText != null) {
			mDialogTitle.setText(titleText);
		}
		// mBtOk = (Button) findViewById(R.id.num_dialog_button_ok);
		// mBtCancel = (Button) findViewById(R.id.num_dialog_button_cancel);
		mBtCancel = (Button) findViewById(R.id.num_dialog_button_ok);
		mBtOk = (Button) findViewById(R.id.num_dialog_button_cancel);

		mBtOk.setOnClickListener(this);
		mBtCancel.setOnClickListener(this);

		mEtNum = (EditText) findViewById(R.id.et_dialog_number_show);
		mIbNumDes = (ImageButton) findViewById(R.id.ib_dialog_number_des);
		mIbNumDes.setOnClickListener(this);
		mIbNumAdd = (ImageButton) findViewById(R.id.ib_dialog_number_add);
		mIbNumAdd.setOnClickListener(this);

		mEtNum.setFocusable(true);
		mEtNum.setFocusableInTouchMode(true);
		mEtNum.requestFocus();
		mEtNum.setEnabled(true);
		if (numShow != -1) {
			setButtonState(numShow);
			mEtNum.setText(Integer.toString(numShow));
		} else {
			numShow = 1;
			mEtNum.setText("1");
			mIbNumDes.setEnabled(false);
			mIbNumAdd.setEnabled(true);
		}
		mEtNum.selectAll();
		mEtNum.setOnClickListener(this);
		mEtNum.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if ("0".equals(s.toString())) {
					mEtNum.setText("1");
					mEtNum.setSelection(1);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!TextUtils.isEmpty(s.toString().trim())) {
					numShow = Integer.parseInt(s.toString().trim());
					setButtonState(numShow);
				} else {
					numShow = -1;
				}

			}

		});

		if (mOkText != null) {
			mBtOk.setText(mOkText);
		}
		// if (mOKListener != null) {
		// mBtOk.setOnClickListener(mOKListener);
		// }
		if (mOKResid != -1) {
			mBtOk.setBackgroundResource(mOKResid);
		}

		// if (mCancleListener == null) {
		// mBtCancel.setVisibility(View.GONE);
		// } else {
		// mBtCancel.setOnClickListener(mCancleListener);
		if (mCancleResid != -1) {
			mBtCancel.setBackgroundResource(mCancleResid);
		}

		if (mCancleText != null) {
			mBtCancel.setText(mCancleText);
		}
		// }

	}

	private void setButtonState(int num) {
		if (numShow <= 1) {
			numShow = 1;
			mIbNumAdd.setEnabled(true);
			mIbNumDes.setEnabled(false);
		} else {
			if (numShow == 99) {
				mIbNumAdd.setEnabled(false);
				mIbNumDes.setEnabled(true);
			} else {
				mIbNumAdd.setEnabled(true);
				mIbNumDes.setEnabled(true);
			}
		}
	}

	public void setTitleText(CharSequence text) {
		if (text != null) {
			this.titleText = text.toString();
			if(mDialogTitle != null){
				mDialogTitle.setText(titleText);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_dialog_number_des:
			if (numShow != -1) {
				int newNum = numShow - 1;
				if (newNum <= 1) {
					mEtNum.setText("1");
					mEtNum.setSelection(1);
				} else {
					mEtNum.setText(newNum + "");
					mEtNum.setSelection(Integer.toString(newNum).length());
				}

			} else {
				mEtNum.setText("1");
				mEtNum.setSelection(1);
			}
			break;
		case R.id.ib_dialog_number_add:
			if (numShow != -1) {
				int newNum = numShow + 1;
				if (newNum > Max_num) {
					mEtNum.setText(Max_num+"");
					mEtNum.setSelection(2);
				} else {
					mEtNum.setText(newNum + "");
					mEtNum.setSelection(Integer.toString(newNum).length());
				}

			} else {
				mEtNum.setText("1");
				mEtNum.setSelection(1);
			}
			break;
		case R.id.et_dialog_number_show:
			if (numShow != -1) {
				mEtNum.setSelection(Integer.toString(numShow).length());
			}
			break;

		case R.id.num_dialog_button_cancel: // 注:确定
			
			if (mOKListener != null && numShow > 0) {
				mOKListener.onClick(v);
			}
			break;

		case R.id.num_dialog_button_ok: // 注:取消

			if (mCancleListener != null) {
				mCancleListener.onClick(v);
			}
			break;

		default:
			break;
		}

	}

	/**
	 * 
	 * @param text
	 *            提示的内容
	 * @param backgroudResid
	 *            确定按钮的 背景图片
	 * @param oklistener
	 *            确定按钮的回掉处理
	 */
	public void setOKbtn(String text, int backgroudResid,
			View.OnClickListener oklistener) {
		mOkText = text;
		mOKResid = backgroudResid;
		mOKListener = oklistener;
	}

	/**
	 * 
	 * @param text
	 *            提示的内容
	 * @param backgroudResid
	 *            取消按钮的 背景图片
	 * @param canlelistener
	 *            取消按钮的回掉处理
	 */
	public void setCancleBtn(String text, int backgroudResid,
			View.OnClickListener canlelistener) {
		mCancleText = text;
		mCancleResid = backgroudResid;
		mCancleListener = canlelistener;
	}

	public int getNumShow() {

		return numShow;
	}

	public void setNumShow(int numShow) {
		this.numShow = numShow;
		if(mEtNum != null){
			if (numShow != -1) {
				setButtonState(numShow);
				mEtNum.setText(Integer.toString(numShow));
			} else {
				numShow = 1;
				mEtNum.setText("1");
				mIbNumDes.setEnabled(false);
				mIbNumAdd.setEnabled(true);
			}
			mEtNum.selectAll();
		}
		
	}	
}
