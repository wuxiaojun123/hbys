package com.help.reward.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.help.reward.App;
import com.help.reward.chat.db.UserDao;
import com.help.reward.chat.ui.GroupActivity;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.LoginSuccessRxbusType;
import com.help.reward.rxbus.event.type.LoginSuccessRxbusType2;
import com.help.reward.service.DemoIntentService;
import com.help.reward.service.DemoPushService;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.LoginUtils;
import com.help.reward.utils.StatusBarUtil;
import com.help.reward.view.MyProcessDialog;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.help.reward.R;
import com.help.reward.chat.Constant;
import com.help.reward.chat.DemoHelper;
import com.help.reward.chat.db.InviteMessgeDao;
import com.help.reward.chat.runtimepermissions.PermissionsManager;
import com.help.reward.chat.ui.ChatActivity;
import com.help.reward.chat.ui.ConversationListFragment;
import com.help.reward.fragment.ConsumptionFragment;
import com.help.reward.fragment.HelpFragment;
import com.help.reward.fragment.IntegrationFragment;
import com.help.reward.fragment.MyFragment;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;
import com.igexin.sdk.PushManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

/***
 *
 *
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
//    protected static final String TAG = "MainActivity";

    @BindView(R.id.fl_content)
    FrameLayout fl_content;
    @BindView(R.id.radio_help)
    RadioButton radio_help;
    @BindView(R.id.radio_integration)
    RadioButton radio_integration;//积分
    @BindView(R.id.radio_benefit)
    RadioButton radio_benefit;
    @BindView(R.id.radio_consumption)
    RadioButton radio_consumption;//积分
    @BindView(R.id.radio_my)
    RadioButton radio_my;

    private FragmentManager mFragmentManager;
    private HelpFragment mHelpFragment;
    private IntegrationFragment mIntegrationFragment;//积分
    private ConversationListFragment mBenefitFragment; //获益
    private ConsumptionFragment mConSumptionFragment;//消费
    private MyFragment mMyFragment;//我的
    private Subscription loginSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // com.getui.demo.DemoPushService 为第三方自定义推送服务
        PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), DemoIntentService.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();
        initChat();
    }

    /**
     * 初始化聊天设置
     */
    private void initChat() {

        inviteMessgeDao = new InviteMessgeDao(this);
        UserDao userDao = new UserDao(this);
        registerBroadcastReceiver();
        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        //debug purpose only
        registerInternalDebugReceiver();
    }

    private void initData() {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        radio_help.performClick();
    }

    @OnClick({R.id.radio_help, R.id.radio_integration, R.id.radio_benefit, R.id.radio_consumption, R.id.radio_my})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        showFragment(id);
    }

    private int currentIndex = 0;

    private void showFragment(int id) {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        hideFragment(mFragmentTransaction);
        switch (id) {
            case R.id.radio_help:
                if (mHelpFragment == null) {
                    mHelpFragment = new HelpFragment();
                    mFragmentTransaction.add(R.id.fl_content, mHelpFragment);
                } else {
                    mFragmentTransaction.show(mHelpFragment);
                }
                currentIndex = 0;

                break;
            case R.id.radio_integration:
                if (mIntegrationFragment == null) {
                    mIntegrationFragment = new IntegrationFragment();
                    mFragmentTransaction.add(R.id.fl_content, mIntegrationFragment);
                } else {
                    mFragmentTransaction.show(mIntegrationFragment);
                }
                currentIndex = 1;

                break;
            case R.id.radio_benefit:
                if (TextUtils.isEmpty(App.APP_USER_ID)) { // 未登录
                    radio_benefit.setChecked(false);
                    Intent mIntent = new Intent(MainActivity.this, LoginActivity.class);
                    mIntent.putExtra(LoginUtils.LOGIN_TYPE, true);
                    startActivity(mIntent);
                    initSubscribe();
                    showRadiaButton();
                    return;
                } else {
                    radio_benefit.setChecked(true);
                    if (TextUtils.isEmpty(App.APP_CLIENT_KEY)) {
                        logoutHuanxin();
                    }
                    if (mBenefitFragment == null) {
                        //mBenefitFragment = new BenefitFragment();
                        mBenefitFragment = new ConversationListFragment();
                        mFragmentTransaction.add(R.id.fl_content, mBenefitFragment);
                    } else {
                        mFragmentTransaction.show(mBenefitFragment);
                    }
                    currentIndex = 2;
                }

                break;
            case R.id.radio_consumption:
                if (mConSumptionFragment == null) {
                    mConSumptionFragment = new ConsumptionFragment();
                    mFragmentTransaction.add(R.id.fl_content, mConSumptionFragment);
                } else {
                    mFragmentTransaction.show(mConSumptionFragment);
                }
                currentIndex = 3;

                break;
            case R.id.radio_my:
                if (mMyFragment == null) {
                    mMyFragment = new MyFragment();
                    mFragmentTransaction.add(R.id.fl_content, mMyFragment);
                } else {
                    mFragmentTransaction.show(mMyFragment);
                }
                currentIndex = 4;

                break;
        }
        mFragmentTransaction.commitAllowingStateLoss();
    }


    /***
     * 订阅获益登录成功的消息
     */
    private void initSubscribe() {
        if (loginSubscribe != null) {
            return;
        }
        loginSubscribe = RxBus.getDefault().toObservable(LoginSuccessRxbusType2.class).subscribe(new Action1<LoginSuccessRxbusType2>() {
            @Override
            public void call(LoginSuccessRxbusType2 loginSuccessRxbusType2) {
                if (loginSuccessRxbusType2.loginFlag) {
                    showFragment(R.id.radio_benefit);
                }
            }
        });
    }

    private void logoutHuanxin() {
        DemoHelper.getInstance().logout(false, new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        // show login screen
                        //ToastUtils.show(mContext, "退出成功");
                        App.APP_CLIENT_KEY = null;
                        App.APP_CLIENT_COOKIE = null;
                        App.mLoginReponse = null;
                        // 应该清除个人信息页面的信息
                        RxBus.getDefault().post(new LoginSuccessRxbusType("logout"));
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
            }
        });

    }

    private void hideFragment(FragmentTransaction mFragmentTransaction) {
        if (mHelpFragment != null && !mHelpFragment.isHidden()) {
            mFragmentTransaction.hide(mHelpFragment);
        }
        if (mIntegrationFragment != null && !mIntegrationFragment.isHidden()) {
            mFragmentTransaction.hide(mIntegrationFragment);
        }
        if (mBenefitFragment != null && !mBenefitFragment.isHidden()) {
            mFragmentTransaction.hide(mBenefitFragment);
        }
        if (mConSumptionFragment != null && !mConSumptionFragment.isHidden()) {
            mFragmentTransaction.hide(mConSumptionFragment);
        }
        if (mMyFragment != null && !mMyFragment.isHidden()) {
            mFragmentTransaction.hide(mMyFragment);
        }
    }


    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //red packet code : 处理红包回执透传消息
            for (EMMessage message : messages) {
                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                final String action = cmdMsgBody.action();//获取自定义action
//                if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
//                    RedPacketUtil.receiveRedPacketAckMessage(message);
//                }
            }
            //end of red packet code
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
//                updateUnreadLabel();
//                if (currentTabIndex == 0) {
//                    // refresh conversation list
//                    if (conversationListFragment != null) {
//                        conversationListFragment.refresh();
//                    }
//                }
            }
        });
    }


    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        //intentFilter.addAction(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                updateUnreadAddressLable();
//                if (currentTabIndex == 0) {
//                    // refresh conversation list
//                    if (conversationListFragment != null) {
//                        conversationListFragment.refresh();
//                    }
//                } else if (currentTabIndex == 1) {
//                    if(contactListFragment != null) {
//                        contactListFragment.refresh();
//                    }
//                }
                String action = intent.getAction();
                if (action.equals(Constant.ACTION_GROUP_CHANAGED)) {
                    if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupActivity.class.getName())) {
                        GroupActivity.instance.onResume();
                    }
                }
                //red packet code : 处理红包回执透传消息
//                if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)){
//                    if (conversationListFragment != null){
//                        conversationListFragment.refresh();
//                    }
//                }
                //end of red packet code
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
        }

        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
                            username.equals(ChatActivity.activityInstance.toChatUsername)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
                                .show();
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
        }

        @Override
        public void onContactInvited(String username, String reason) {
        }

        @Override
        public void onFriendRequestAccepted(String username) {
        }

        @Override
        public void onFriendRequestDeclined(String username) {
        }
    }

    private void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }


    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
//        if (count > 0) {
//            unreadLabel.setText(String.valueOf(count));
//            unreadLabel.setVisibility(View.VISIBLE);
//        } else {
//            unreadLabel.setVisibility(View.INVISIBLE);
//        }
    }

    /**
     * update the total unread count
     */
    public void updateUnreadAddressLable() {
        runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();
//                if (count > 0) {
//                    unreadAddressLable.setVisibility(View.VISIBLE);
//                } else {
//                    unreadAddressLable.setVisibility(View.INVISIBLE);
//                }
            }
        });

    }

    /**
     * get unread event notification count, including application, accepted, etc
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }

    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMessageCount();
        for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }


    private InviteMessgeDao inviteMessgeDao;

    @Override
    protected void onResume() {
        super.onResume();

//        if (!isConflict && !isCurrentAccountRemoved) {
//            updateUnreadLabel();
//            updateUnreadAddressLable();
//        }

        // unregister this event listener when this activity enters the
        // background
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //outState.putBoolean("isConflict", isConflict);
        //outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    private android.app.AlertDialog.Builder exceptionBuilder;
    private boolean isExceptionDialogShow = false;
    private BroadcastReceiver internalDebugReceiver;
    private ConversationListFragment conversationListFragment;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;

    private int getExceptionMessageId(String exceptionType) {
        if (exceptionType.equals(Constant.ACCOUNT_CONFLICT)) {
            return R.string.connect_conflict;
        } else if (exceptionType.equals(Constant.ACCOUNT_REMOVED)) {
            return R.string.em_user_remove;
        } else if (exceptionType.equals(Constant.ACCOUNT_FORBIDDEN)) {
            return R.string.user_forbidden;
        }
        return R.string.Network_error;
    }

    /**
     * show the dialog when user met some exception: such as login on another device, user removed or user forbidden
     */
    private void showExceptionDialog(String exceptionType) {
        isExceptionDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (exceptionBuilder == null)
                    exceptionBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                exceptionBuilder.setTitle(st);
                exceptionBuilder.setMessage(getExceptionMessageId(exceptionType));
                exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exceptionBuilder = null;
                        isExceptionDialogShow = false;
                        finish();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                exceptionBuilder.setCancelable(false);
                exceptionBuilder.create().show();
                //isConflict = true;
            } catch (Exception e) {
                LogUtils.e("---------color conflictBuilder error" + e.getMessage());
            }
        }
    }

    private void showExceptionDialogFromIntent(Intent intent) {
        if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)) {
            showExceptionDialog(Constant.ACCOUNT_CONFLICT);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false)) {
            showExceptionDialog(Constant.ACCOUNT_REMOVED);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_FORBIDDEN, false)) {
            showExceptionDialog(Constant.ACCOUNT_FORBIDDEN);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showExceptionDialogFromIntent(intent);
    }

    /**
     * debug purpose only, you can ignore this
     */
    private void registerInternalDebugReceiver() {
        internalDebugReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                DemoHelper.getInstance().logout(false, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                finish();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String message) {
                    }
                });
            }
        };
        IntentFilter filter = new IntentFilter(getPackageName() + ".em_internal_debug");
        registerReceiver(internalDebugReceiver, filter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this, 30, null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt("index", currentIndex);
//        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentIndex = savedInstanceState.getInt("index");
        showRadiaButton();
    }

    private void showRadiaButton() {
        switch (currentIndex) {
            case 0:
                radio_help.setChecked(true);
                break;
            case 1:
                radio_integration.setChecked(true);
                break;
            case 2:
                radio_benefit.setChecked(true);
                break;
            case 3:
                radio_consumption.setChecked(true);
                break;
            case 4:
                radio_my.setChecked(true);
                break;
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (mHelpFragment == null && fragment instanceof HelpFragment) {
            mHelpFragment = (HelpFragment) fragment;
        }
        if (mIntegrationFragment == null && fragment instanceof IntegrationFragment) {
            mIntegrationFragment = (IntegrationFragment) fragment;

        }
        if (mBenefitFragment == null && fragment instanceof ConversationListFragment) {
            mBenefitFragment = (ConversationListFragment) fragment;
        }
        if (mConSumptionFragment == null && fragment instanceof ConsumptionFragment) {
            mConSumptionFragment = (ConsumptionFragment) fragment;
        }
        if (mMyFragment == null && fragment instanceof MyFragment) {
            mMyFragment = (MyFragment) fragment;
        }
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
//        exit();
    }

    private long mExitTime;

    /**
     * 退出应用
     */
    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            ToastUtils.show(mContext, "再按一次退出");
            mExitTime = System.currentTimeMillis();
            return;
        } else {
            App.mLoginReponse = null;
            App.APP_CLIENT_KEY = null;
            App.APP_USER_ID = null;
            App.APP_CLIENT_COOKIE = null;
            App.GETUI_CLIENT_ID = null;
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (exceptionBuilder != null) {
            exceptionBuilder.create().dismiss();
            exceptionBuilder = null;
            isExceptionDialogShow = false;
        }
        unregisterBroadcastReceiver();

        try {
            unregisterReceiver(internalDebugReceiver);
        } catch (Exception e) {
        }

    }
}
