package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.help.reward.R;
import com.help.reward.fragment.BaseFragment;
import com.help.reward.fragment.MyBalanceFragment;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.MyAccountHelpRewardRxbusType;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 余额---明细
 * <p>
 * item_my_balance.xml
 * <p>
 * <p>
 * Created by wuxiaojun on 2017/1/10.
 */

public class MyBalanceActivity extends BaseActivity implements View.OnClickListener {

	@BindView(R.id.tv_balance) TextView			tv_balance;

	@BindView(R.id.id_viewpager) ViewPager		viewPager;

	@BindView(R.id.tabs) PagerSlidingTabStrip	tabStrip;

	@BindView(R.id.rl_content) RelativeLayout	rl_content;

	private List<BaseFragment>					fragmentList;

	@Override protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_balance);
		ButterKnife.bind(this);

		initEvent();
		initData();
		getRxBusData();
	}

	private void initData() {
		fragmentList = new ArrayList<>(3);
		MyBalanceFragment fragmentAll = new MyBalanceFragment();

		MyBalanceFragment fragmentPay = new MyBalanceFragment();
		// 支出
		Bundle bundle = new Bundle();
		bundle.putString("type", "2");
		fragmentPay.setArguments(bundle);

		MyBalanceFragment fragmentInCome = new MyBalanceFragment();
		// 收入
		Bundle bundle2 = new Bundle();
		bundle2.putString("type", "1");
		fragmentInCome.setArguments(bundle2);

		fragmentList.add(fragmentAll);
		fragmentList.add(fragmentPay);
		fragmentList.add(fragmentInCome);
		viewPager.setOffscreenPageLimit(3);
		viewPager.setAdapter(new MyFragmentPageAdapter(getSupportFragmentManager()));
		tabStrip.setViewPager(viewPager);
	}

	private void initEvent() {

	}

	@Override protected void setStatusBar() {
		StatusBarUtil.setTranslucentForImageViewInFragment(MyBalanceActivity.this, StatusBarUtil.DEFAULT_ALPHA, null);
	}

	private Subscription subscribe;

	/**
	 * 获取rxbus传递过来的数据
	 */
	public void getRxBusData() {
		subscribe = RxBus.getDefault().toObservable(MyAccountHelpRewardRxbusType.class).subscribe(new Action1<MyAccountHelpRewardRxbusType>() {

			@Override public void call(MyAccountHelpRewardRxbusType type) {
				tv_balance.setText(type.points);
			}
		});
	}

	@OnClick({ R.id.iv_back, R.id.tv_balance_recharge, R.id.tv_help_score, R.id.tv_general_volume }) @Override public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.iv_back:
				// 退出
				finish();
				ActivitySlideAnim.slideOutAnim(MyBalanceActivity.this);

				break;

			case R.id.tv_balance_recharge:
				// 余额充值
				startActivity(new Intent(MyBalanceActivity.this, PrepaidBalanceActivity.class));
				ActivitySlideAnim.slideInAnim(MyBalanceActivity.this);

				break;
			case R.id.tv_help_score:
				// 兑换帮赏分
				startActivity(new Intent(MyBalanceActivity.this, BalanceExchangeHelpScoreActivity.class));
				ActivitySlideAnim.slideInAnim(MyBalanceActivity.this);

				break;
			case R.id.tv_general_volume:
				// 兑换通用卷
				startActivity(new Intent(MyBalanceActivity.this, BalanceExchangeVolumeActivity.class));
				ActivitySlideAnim.slideInAnim(MyBalanceActivity.this);

				break;
		}
	}

	private class MyFragmentPageAdapter extends FragmentPagerAdapter {

		@Override public CharSequence getPageTitle(int position) {
			if (position == 0) {
				return getMString(R.string.string_all);
			} else if (position == 1) {
				return getMString(R.string.string_expenditure);
			} else {
				return getMString(R.string.string_support);
			}
		}

		public MyFragmentPageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override public Fragment getItem(int position) {
			return fragmentList.get(position);
		}

		@Override public int getCount() {
			return fragmentList.size();
		}
	}

	private String getMString(int resId) {
		return mContext.getString(resId);
	}

	@Override protected void onDestroy() {
		if (subscribe != null && !subscribe.isUnsubscribed()) {
			subscribe.unsubscribe();
		}
		super.onDestroy();
	}

}
