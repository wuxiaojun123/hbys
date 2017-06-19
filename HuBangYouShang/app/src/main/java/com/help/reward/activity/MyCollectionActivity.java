package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.MyCollectionGoodsAdapter;
import com.help.reward.adapter.MyCollectionPostAdapter;
import com.help.reward.adapter.MyCollectionStoreAdapter;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.fragment.BaseFragment;
import com.help.reward.fragment.MyCollectionGoodsFragment;
import com.help.reward.fragment.MyCollectionPostFragment;
import com.help.reward.fragment.MyCollectionStoreFragment;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.api.PersonalApi;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.view.AlertDialog;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的收藏
 * <p>
 * Created by wuxiaojun on 2017/2/8.
 */
public class MyCollectionActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.id_viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabStrip;

    private List<BaseFragment> fragmentList;
    private int currentPage; // 当前页面

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        ButterKnife.bind(this);

        fragmentList = new ArrayList<>(3);
        fragmentList.add(new MyCollectionPostFragment());
        fragmentList.add(new MyCollectionGoodsFragment());
        fragmentList.add(new MyCollectionStoreFragment());

        initView();
    }

    private void initView() {
        tv_title.setText(R.string.string_my_collection_title);
        tv_title_right.setText(R.string.string_my_clean);
        initViewPager();
    }

    private void initViewPager() {
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabStrip.setViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.iv_title_back, R.id.tv_title_right})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(MyCollectionActivity.this);

                break;
            case R.id.tv_title_right:
                // 清空消息
                if (currentPage == 0) {
                    MyCollectionPostAdapter myCollectionPostAdapter = ((MyCollectionPostFragment) fragmentList.get(0)).getMyCollectionPostAdapter();
                    if (myCollectionPostAdapter != null) {
                        if (myCollectionPostAdapter.getItemCount() != 0) {
                            showDialogClean();
                        }
                    }
                } else if (currentPage == 1) {
                    MyCollectionGoodsAdapter myCollectionGoodsAdapter = ((MyCollectionGoodsFragment) fragmentList.get(1)).getMyCollectionGoodsAdapter();
                    if (myCollectionGoodsAdapter != null) {
                        if (myCollectionGoodsAdapter.getItemCount() != 0) {
                            showDialogClean();
                        }
                    }
                } else if (currentPage == 2) {
                    MyCollectionStoreAdapter myCollectionStoreAdapter = ((MyCollectionStoreFragment) fragmentList.get(2)).getMyCollectionStoreAdapter();
                    if (myCollectionStoreAdapter != null) {
                        if (myCollectionStoreAdapter.getItemCount() != 0) {
                            showDialogClean();
                        }
                    }
                }

                break;

        }
    }

    private void showDialogClean() {
        new AlertDialog(MyCollectionActivity.this)
                .builder()
                .setTitle(R.string.exit_title)
                .setMsg("确定清空吗?")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cleanMsg();
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .show();
    }

    /***
     * 清空收藏（帖子）
     * /mobile/index.php?act=member_favorites_post&op=clean_up
     * 清空收藏(商品)
     * /mobile/index.php?act=member_favorites&op=clean_up
     * 清空收藏(店铺)
     * /mobile/index.php?act=member_favorites_store&op=clean_up
     */
    private void cleanMsg() {
        if (App.APP_CLIENT_KEY == null) {
            return;
        }
        PersonalApi responseApi = PersonalNetwork.getResponseApi();
        Observable<BaseResponse> cleanPostResponse = null;
        switch (currentPage) {
            case 0:
                cleanPostResponse = responseApi.getCleanPostResponse(App.APP_CLIENT_KEY);

                break;
            case 1:
                cleanPostResponse = responseApi.getCleanGoodsResponse(App.APP_CLIENT_KEY);

                break;
            case 2:
                cleanPostResponse = responseApi.getCleanStoreResponse(App.APP_CLIENT_KEY);

                break;
        }
        if (cleanPostResponse != null) {
            cleanPostResponse
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<BaseResponse>() {
                        @Override
                        public void onNext(BaseResponse baseResponse) {
                            ToastUtils.show(mContext, baseResponse.msg);
                            if (baseResponse.code == 200) {
                                // 更新各个fragment里面的数据
                                if (fragmentList != null) {
                                    if (currentPage == 0) {
                                        ((MyCollectionPostFragment) fragmentList.get(0)).refreshRecycler();
                                    } else if (currentPage == 1) {
                                        ((MyCollectionGoodsFragment) fragmentList.get(1)).refreshRecycler();
                                    } else if (currentPage == 2) {
                                        ((MyCollectionStoreFragment) fragmentList.get(2)).refreshRecycler();
                                    }
                                }
                            }
                        }
                    });
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private String[] TITLES = new String[3];

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            TITLES[0] = getResources().getString(R.string.string_post);
            TITLES[1] = getResources().getString(R.string.string_goods);
            TITLES[2] = getResources().getString(R.string.string_store);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }

}
