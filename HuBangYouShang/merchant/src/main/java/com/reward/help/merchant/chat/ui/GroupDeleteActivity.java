package com.reward.help.merchant.chat.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnItemClickListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.idotools.utils.MetricsUtils;
import com.reward.help.merchant.R;
import com.reward.help.merchant.adapter.CouponListAdapter;
import com.reward.help.merchant.chat.adapter.GroupMemberAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ADBrian on 04/04/2017.
 */

public class GroupDeleteActivity extends BaseActivity implements View.OnClickListener{


    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;


    @BindView(R.id.iv_title_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;

    private ArrayList<String> delList = new ArrayList<String>();
    private GroupMemberAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private String groupId;
    private EMGroup group;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_delete);
        ButterKnife.bind(this);

        groupId = getIntent().getStringExtra("groupId");
        group = EMClient.getInstance().groupManager().getGroup(groupId);
        initView();
    }

    private void initView() {
        mTvTitle.setText("删除成员");
        mTvRight.setText("删除");


        lRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupMemberAdapter(this,delList);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        //禁用下拉刷新功能
        lRecyclerview.setPullRefreshEnabled(false);
        //禁用自动加载更多功能
        lRecyclerview.setLoadMoreEnabled(false);
        lRecyclerview.setItemAnimator(new DefaultItemAnimator());
//        lRecyclerview.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                //super.getItemOffsets(outRect, view, parent, state);
//                if (parent.getChildPosition(view) != 0) {
//                    outRect.top = MetricsUtils.dipToPx(10);
//                }
//            }
//        });

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

               if (adapter.getDataList() != null &&adapter.getDataList().size() > position) {

                   String name = adapter.getDataList().get(position);
                   if (delList.contains(name)) {
                       delList.remove(name);
                   } else {
                       delList.add(name);
                   }
                   adapter.notifyDataSetChanged();
               }
            }
        });
        adapter.setDataList(group.getMembers());
    }

    @OnClick({R.id.iv_title_back,R.id.tv_right})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_right:
                del();
                break;
        }
    }

    private void del() {
        if (!delList.isEmpty()) {


            List<String> del = delList;
            for (String name:
                    del) {
                try {
                    EMClient.getInstance().groupManager().removeUserFromGroup(groupId, name);
                    delList.remove(name);
                }catch (Exception e){

                }

            }
            refreshMembers();
        }
    }

    private void refreshMembers(){

        adapter.setDataList(group.getMembers());
        adapter.notifyDataSetChanged();
    }
}
