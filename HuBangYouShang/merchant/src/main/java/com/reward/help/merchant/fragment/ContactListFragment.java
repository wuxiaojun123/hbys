package com.reward.help.merchant.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.ui.EaseBaseFragment;
import com.reward.help.merchant.R;
import com.reward.help.merchant.activity.ApplyCreateGroupActivity;
import com.reward.help.merchant.activity.QueryApplyGroupActivity;
import com.reward.help.merchant.chat.Constant;
import com.reward.help.merchant.chat.adapter.GroupAdapter;
import com.reward.help.merchant.chat.ui.ChatActivity;
import com.reward.help.merchant.chat.ui.GroupActivity;

import java.util.List;

public class ContactListFragment extends EaseBaseFragment {

    protected List<EMGroup> grouplist;
    private ListView groupListView;
    private ImageView mIvBack;
    private TextView mRight;
    private TextView mTitle;
    private RelativeLayout mRlApply;

    private GroupAdapter groupAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if(savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        groupListView = (ListView) getView().findViewById(R.id.list);
        mIvBack = (ImageView) getView().findViewById(R.id.iv_title_back);
        mIvBack.setVisibility(View.GONE);

        mTitle = (TextView) getView().findViewById(R.id.tv_title);
        mRight = (TextView) getView().findViewById(R.id.tv_right);
        mRlApply = (RelativeLayout) getView().findViewById(R.id.rl_contact_apply);

        mTitle.setText(getText(R.string.contact_title));
        mRight.setText(getText(R.string.contact_title_right));


        grouplist = EMClient.getInstance().groupManager().getAllGroups();
        //show group list
        groupAdapter = new GroupAdapter(getActivity(), 1, grouplist);
        groupListView.setAdapter(groupAdapter);


        mRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 申请建群
                startActivity(new Intent(getActivity(), ApplyCreateGroupActivity.class));
            }
        });

        mRlApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 进度查看
                startActivity(new Intent(getActivity(), QueryApplyGroupActivity.class));
            }
        });


        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // enter group chat
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                // it is group chat
                intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                intent.putExtra("userId", groupAdapter.getItem(position).getGroupId());
                startActivityForResult(intent, 0);


            }

        });
    }

    @Override
    protected void setUpView() {

    }

    @Override
    public void onResume() {
        refresh();
        super.onResume();
    }

    private void refresh(){
        grouplist = EMClient.getInstance().groupManager().getAllGroups();
        groupAdapter = new GroupAdapter(getActivity(), 1, grouplist);
        groupListView.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();
    }
}
