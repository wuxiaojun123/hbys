package com.help.reward.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnItemClickListener;
import com.base.recyclerview.OnLoadMoreListener;
import com.base.recyclerview.OnRefreshListener;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.activity.HelpSeekInfoActivity;
import com.help.reward.adapter.HelpSeekAdapter;
import com.help.reward.bean.HelpSeekBean;
import com.help.reward.bean.Response.HelpSeekResponse;
import com.help.reward.network.HelpNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 互帮-求助
 * Created by MXY on 2017/2/19.
 */

public class HelpSeekFragment extends BaseFragment {

    private View contentView;
    private HelpSeekAdapter adapter;
    @BindView(R.id.lv_fragment_help1)
    LRecyclerView lRecyclerview;

    @BindView(R.id.ll_empty)
    View ll_empty;
    @BindView(R.id.tv_no_result)
    TextView tv_no_result;
    private int numSize = 15;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    List<HelpSeekBean> mDatas = new ArrayList<>();
    int curpage = 1;
    String searchStr;
    String type;
    String board_id = "";
    String area_id = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_help1, null);
        }
        ButterKnife.bind(this, contentView);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("type")) {
            type = bundle.getString("type");
        }
        if (bundle != null && bundle.containsKey("keyword")) {
            searchStr = bundle.getString("keyword");
        }
        if (bundle != null && bundle.containsKey("board_id")) {
            board_id = bundle.getString("board_id");
        }
        if (bundle != null && bundle.containsKey("area_id")) {
            area_id = bundle.getString("area_id");
        }

        initData();
        return contentView;
    }

    private void initData() {

        tv_no_result.setText("抱歉没有找到符合条件的帖子");
        if ("search".equalsIgnoreCase(type)) {
            lRecyclerview.setEmptyView(ll_empty);
        }
        lRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new HelpSeekAdapter(mContext);
        adapter.setDataList(mDatas);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        //禁用下拉刷新功能
        lRecyclerview.setPullRefreshEnabled(true);
        //禁用自动加载更多功能
        lRecyclerview.setLoadMoreEnabled(false);
        lRecyclerview.setItemAnimator(new DefaultItemAnimator());
        initRefreshListener();
        initLoadMoreListener();
        initItemClickListener();
        requestData();
    }

    public void searchData(String searchStr) {
        if (this.searchStr.equals(searchStr)) {
            return;
        }
        this.searchStr = searchStr;
        MyProcessDialog.showDialog(mContext);
        requestData();
    }

    /**
     * 设置筛选条件
     */
    public void setSelectInfo(String boardId, String areaId) {
        if (boardId.equals(board_id) && areaId.equals(area_id)) {
            return;
        }
        board_id = boardId;
        area_id = areaId;
        MyProcessDialog.showDialog(mContext);
        requestData();
    }

    private void initRefreshListener() {
        lRecyclerview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtils.e("执行下拉刷新的方法");
                curpage = 1;
                requestData();
            }
        });
    }

    private void initLoadMoreListener() {
        lRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                curpage++;
                requestData();
            }
        });
    }

    private void initItemClickListener() {
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, HelpSeekInfoActivity.class);
                intent.putExtra("id", adapter.getDataList().get(position).id);
                startActivity(intent);
                ActivitySlideAnim.slideInAnim(getActivity());
            }

        });
    }


    private void requestData() {
        LogUtils.e(curpage+ App.APP_CLIENT_KEY+ searchStr+ board_id+ area_id);
        subscribe = HelpNetwork
                .getHelpApi()
                .getHelpSeekBean("seek_help", curpage, App.APP_CLIENT_KEY, searchStr, board_id, area_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<HelpSeekResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                        lRecyclerview.refreshComplete(numSize);
                        ToastUtils.show(mContext, R.string.string_error);
                        if (curpage != 1) {
                            curpage--;
                        }
                    }

                    @Override
                    public void onNext(HelpSeekResponse response) {
                        lRecyclerview.refreshComplete(numSize);
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if (response.data != null) {
                                if (curpage == 1) {
                                    adapter.setDataList(response.data.post_list);
                                    if (adapter.getDataList().size() == 0) {
//                                        ToastUtils.show(mContext, "暂无数据");
                                    }
                                } else {
                                    adapter.addAll(response.data.post_list);
                                }
                            }
                            if (!response.hasmore) {
                                lRecyclerview.setLoadMoreEnabled(false);
                            } else {
                                lRecyclerview.setLoadMoreEnabled(true);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });

    }


}
