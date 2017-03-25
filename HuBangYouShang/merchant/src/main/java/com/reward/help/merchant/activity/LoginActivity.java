/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.reward.help.merchant.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;
import com.reward.help.merchant.App;
import com.reward.help.merchant.R;
import com.reward.help.merchant.bean.Response.LoginResponse;
import com.reward.help.merchant.chat.DemoHelper;
import com.reward.help.merchant.chat.db.DemoDBManager;
import com.reward.help.merchant.chat.ui.BaseActivity;
import com.reward.help.merchant.network.PersonalNetwork;
import com.reward.help.merchant.network.base.BaseSubscriber;
import com.reward.help.merchant.rxbus.RxBus;
import com.reward.help.merchant.utils.ActivitySlideAnim;
import com.reward.help.merchant.utils.Constant;
import com.reward.help.merchant.view.MyProcessDialog;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Login screen
 * 
 */
public class LoginActivity extends BaseActivity {
	private static final String TAG = "LoginActivity";
	public static final int REQUEST_CODE_SETNICK = 1;
	private EditText usernameEditText;
	private EditText passwordEditText;
	private ImageView mIvBack;
	private TextView mTvTitle;
	private TextView mTvForgetPassword;

	private boolean progressShow;
	private boolean autoLogin = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// enter the main activity if already logged in
		if (DemoHelper.getInstance().isLoggedIn()) {
			autoLogin = true;
			startActivity(new Intent(LoginActivity.this, MainActivity.class));

			return;
		}
		setContentView(R.layout.activity_login);

		usernameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);

		mIvBack = (ImageView) findViewById(R.id.iv_title_back);
		mIvBack.setVisibility(View.GONE);

		mTvTitle = (TextView) findViewById(R.id.tv_title);
		mTvTitle.setText(getText(R.string.login));

		mTvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);

		// if user changed, clear the password
		usernameEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				passwordEditText.setText(null);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		if (DemoHelper.getInstance().getCurrentUsernName() != null) {
			usernameEditText.setText(DemoHelper.getInstance().getCurrentUsernName());
		}

		mTvForgetPassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this,CheckPhoneNumberActivity.class));
			}
		});
	}


	/***
	 * 登录逻辑
	 */
	private void loginRequest(final String username, final String password) {

		MyProcessDialog.showDialog(LoginActivity.this);
		subscribe = PersonalNetwork
				.getLoginApi()
				.getLoginBean(username, password, Constant.PLATFORM_CLIENT).subscribeOn(Schedulers.io()) // 请求放在io线程中
				.observeOn(AndroidSchedulers.mainThread()) // 请求结果放在主线程中
				.subscribe(new BaseSubscriber<LoginResponse>() {
					@Override
					public void onError(Throwable e) {
						MyProcessDialog.closeDialog();
						e.printStackTrace();
						if (e instanceof UnknownHostException) {
							ToastUtils.show(mContext, "请求到错误服务器");
						} else if (e instanceof SocketTimeoutException) {
							ToastUtils.show(mContext, "请求超时");
						}
					}

					@Override
					public void onNext(LoginResponse res) {
						//MyProcessDialog.closeDialog();
						if (res.code == 200) {
							LogUtils.e("请求到的key是：" + res.data.key + "=======" + res.data.key);
							App.setAppClientKey(res.data.key);
							RxBus.getDefault().post("loginSuccess");

							loginToHuanxin(username,password);
							//finish();
							//ActivitySlideAnim.slideOutAnim(LoginActivity.this);
						} else {
							ToastUtils.show(mContext, res.msg);
						}
					}
				});
	}

	/**
	 * login
	 * 
	 * @param view
	 */
	public void login(View view) {
		if (!EaseCommonUtils.isNetWorkConnected(this)) {
			Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
			return;
		}
		String currentUsername = usernameEditText.getText().toString().trim();
		String currentPassword = passwordEditText.getText().toString().trim();

		if (TextUtils.isEmpty(currentUsername)) {
			Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(currentPassword)) {
			Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
			return;
		}

		loginRequest(currentUsername,currentPassword);

//		progressShow = true;
//		final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
//		pd.setCanceledOnTouchOutside(false);
//		pd.setOnCancelListener(new OnCancelListener() {
//
//			@Override
//			public void onCancel(DialogInterface dialog) {
//				Log.d(TAG, "EMClient.getInstance().onCancel");
//				progressShow = false;
//			}
//		});
//		pd.setMessage(getString(R.string.Is_landing));
//		pd.show();

		// After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
		// close it before login to make sure DemoDB not overlap
	}

	private void loginToHuanxin(String currentUsername, String currentPassword) {
		DemoDBManager.getInstance().closeDB();

		// reset current user name before login
		DemoHelper.getInstance().setCurrentUserName(currentUsername);

		final long start = System.currentTimeMillis();
		// call login method
		Log.d(TAG, "EMClient.getInstance().login");
		EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

			@Override
			public void onSuccess() {
				Log.d(TAG, "login: onSuccess");
				MyProcessDialog.closeDialog();


				// ** manually load all local groups and conversation
			    EMClient.getInstance().groupManager().loadAllGroups();
			    EMClient.getInstance().chatManager().loadAllConversations();

			    // update current user's display name for APNs
				boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
						App.currentUserNick.trim());
				if (!updatenick) {
					Log.e("LoginActivity", "update current user nick fail");
				}

//				if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
//				    pd.dismiss();
//				}
				// get user's info (this should be get from App's server or 3rd party service)
				DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);

				finish();
			}

			@Override
			public void onProgress(int progress, String status) {
				Log.d(TAG, "login: onProgress");
			}

			@Override
			public void onError(final int code, final String message) {
				Log.d(TAG, "login: onError: " + code);
				//if (!progressShow) {
				//	return;
				//}
				runOnUiThread(new Runnable() {
					public void run() {
						MyProcessDialog.closeDialog();
						Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (autoLogin) {
			return;
		}
	}
}
