package com.wxj.hbys.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wxj.hbys.R;
import com.wxj.hbys.fragment.BenefitFragment;
import com.wxj.hbys.fragment.ConsumptionFragment;
import com.wxj.hbys.fragment.HelpFragment;
import com.wxj.hbys.fragment.IntegrationFragment;
import com.wxj.hbys.fragment.MyFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 *
 *
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{

    /*@BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.iv_email)
    ImageView iv_email;
    @BindView(R.id.tv_text)
    TextView tv_text;*/


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
    private BenefitFragment mBenefitFragment;//获益
    private ConsumptionFragment mConSumptionFragment;//消费
    private MyFragment mMyFragment;//我的


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();

    }

    private void initData() {
        if(mFragmentManager == null){
            mFragmentManager = getSupportFragmentManager();
        }
        radio_help.performClick();
    }

    @OnClick({R.id.radio_help,R.id.radio_integration,R.id.radio_benefit,R.id.radio_consumption,R.id.radio_my})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        showFragment(id);
        switch (id){
            case R.id.radio_help:

                break;
            case R.id.radio_integration:

                break;
            case R.id.radio_benefit:

                break;
            case R.id.radio_consumption:

                break;
            case R.id.radio_my:

                break;
        }
    }

    private void showFragment(int id) {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        hideFragment(mFragmentTransaction);
        switch (id){
            case R.id.radio_help:
                if(mHelpFragment == null){
                    mHelpFragment = new HelpFragment();
                    mFragmentTransaction.add(R.id.fl_content,mHelpFragment);
                }else{
                    mFragmentTransaction.show(mHelpFragment);
                }

                break;
            case R.id.radio_integration:
                if(mIntegrationFragment == null){
                    mIntegrationFragment = new IntegrationFragment();
                    mFragmentTransaction.add(R.id.fl_content,mIntegrationFragment);
                }else{
                    mFragmentTransaction.show(mIntegrationFragment);
                }

                break;
            case R.id.radio_benefit:
                if(mBenefitFragment == null){
                    mBenefitFragment = new BenefitFragment();
                    mFragmentTransaction.add(R.id.fl_content,mBenefitFragment);
                }else{
                    mFragmentTransaction.show(mBenefitFragment);
                }

                break;
            case R.id.radio_consumption:
                if(mConSumptionFragment == null){
                    mConSumptionFragment = new ConsumptionFragment();
                    mFragmentTransaction.add(R.id.fl_content,mConSumptionFragment);
                }else{
                    mFragmentTransaction.show(mConSumptionFragment);
                }

                break;
            case R.id.radio_my:
                if(mMyFragment == null){
                    mMyFragment = new MyFragment();
                    mFragmentTransaction.add(R.id.fl_content,mMyFragment);
                }else {
                    mFragmentTransaction.show(mMyFragment);
                }

                break;
        }
        mFragmentTransaction.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction mFragmentTransaction){
        if(mHelpFragment != null && !mHelpFragment.isHidden()){
            mFragmentTransaction.hide(mHelpFragment);
        }
        if(mIntegrationFragment != null && !mIntegrationFragment.isHidden()){
            mFragmentTransaction.hide(mIntegrationFragment);
        }
        if(mBenefitFragment != null && !mBenefitFragment.isHidden()){
            mFragmentTransaction.hide(mBenefitFragment);
        }
        if(mConSumptionFragment != null && !mConSumptionFragment.isHidden()){
            mFragmentTransaction.hide(mConSumptionFragment);
        }
        if(mMyFragment != null && !mMyFragment.isHidden()){
            mFragmentTransaction.hide(mMyFragment);
        }
    }

}
