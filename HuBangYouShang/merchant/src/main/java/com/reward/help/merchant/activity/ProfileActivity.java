package com.reward.help.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.utils.EaseUserUtils;
import com.idotools.utils.ToastUtils;
import com.reward.help.merchant.R;
import com.reward.help.merchant.chat.DemoHelper;
import com.reward.help.merchant.chat.ui.BaseActivity;
import com.reward.help.merchant.utils.ChooseCameraPopuUtils;
import com.reward.help.merchant.utils.DealChoosePicUtils;
import com.reward.help.merchant.utils.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人信息
 */

public class ProfileActivity extends BaseActivity implements View.OnClickListener, DealChoosePicUtils.DealChoosePicListener {

    @BindView(R.id.iv_title_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;

    @BindView(R.id.iv_profile_photo)
    ImageView mIvPhoto;

    @BindView(R.id.et_edit_nickname)
    EditText mEtNickname;

    DealChoosePicUtils dealChoosePicUtils;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mTvTitle.setText(getText(R.string.profile_info));
        mTvRight.setText(getText(R.string.commit));

        String username = DemoHelper.getInstance().getCurrentUsernName();
        GlideUtils.setUserAvatar(this, username, mIvPhoto);
        EaseUserUtils.setUserNick(username, mEtNickname);
    }
    @OnClick({R.id.rl_choose_photo,R.id.tv_right,R.id.iv_title_back})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_choose_photo:
                ChooseCameraPopuUtils.showPopupWindow(this, v);
                if (dealChoosePicUtils == null) {
                    dealChoosePicUtils = new DealChoosePicUtils(this);
                    dealChoosePicUtils.setDealChoosePicListener(this);
                }
                break;
            case R.id.iv_title_back:
                hideSoftKeyboard();
                this.finish();
                break;
            case R.id.tv_right:
                break;
        }
    }

    @Override
    public void finishDeal(String path, int type) {
    }
}
