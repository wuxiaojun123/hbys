package com.help.reward.chat.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.chat.Constant;
import com.help.reward.chat.adapter.ChatSearchAdapter;
import com.help.reward.chat.adapter.GroupAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupActivity extends BaseActivity {
	public static final String TAG = "GroupActivity";

	private ListView groupListView;
	protected List<EMGroup> grouplist;
	private GroupAdapter groupAdapter;
	private InputMethodManager inputMethodManager;
	public static GroupActivity instance;

	private EditText mEtSearch;
	private ImageView clearSearch;
	private ImageView mIvBack;
	private TextView mCancel;

	boolean isFromConversationSearch = false;
	List<EMConversation> emConversations;
	ChatSearchAdapter chatSearchAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_benefit);

		isFromConversationSearch = getIntent().getBooleanExtra(TAG,false);
		instance = this;
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		groupListView = (ListView) findViewById(R.id.list);
		mEtSearch = (EditText) findViewById(R.id.et_benefit_search);
		clearSearch = (ImageView) findViewById(R.id.search_clear);
		mIvBack = (ImageView) findViewById(R.id.iv_benefit_back);
		mCancel = (TextView) findViewById(R.id.iv_benifit_cancel);

		if (isFromConversationSearch) {
			mIvBack.setVisibility(View.GONE);
			mCancel.setVisibility(View.VISIBLE);
			initSearch();
		} else {
			mIvBack.setVisibility(View.VISIBLE);
			mCancel.setVisibility(View.GONE);
			initGroup();
		}

		setListener();
	}

	private void initSearch() {
		emConversations = loadConversationList();
		chatSearchAdapter = new ChatSearchAdapter(this);
		groupListView.setAdapter(chatSearchAdapter);
	}


	private void initGroup() {
		grouplist = EMClient.getInstance().groupManager().getAllGroups();
		//show group list
		groupAdapter = new GroupAdapter(this, 1, grouplist);
		groupListView.setAdapter(groupAdapter);
	}

	private void setListener() {
		groupListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// enter group chat
					Intent intent = new Intent(GroupActivity.this, ChatActivity.class);

					if (isFromConversationSearch) {
						EMConversation conversation = chatSearchAdapter.getItem(position);
						String username = conversation.conversationId();
						if(conversation.isGroup()){
							if(conversation.getType() == EMConversation.EMConversationType.ChatRoom){
								// it's group chat
								intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
							}else{
								intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
							}

						}
						// it's single chat
						intent.putExtra(Constant.EXTRA_USER_ID, username);
						startActivity(intent);
					} else {
						// it is group chat
						intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
						intent.putExtra("userId", groupAdapter.getItem(position).getGroupId());
						startActivityForResult(intent, 0);
					}

			}

		});
		groupListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});


		mEtSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				//conversationListView.filter(s);
				Log.i("Brian",charSequence.toString());
				if (isFromConversationSearch) {
					setSearchData(charSequence.toString() );
				} else {
					groupAdapter.getFilter().filter(charSequence);
				}

				if (charSequence.length() > 0) {
					clearSearch.setVisibility(View.VISIBLE);
				} else {
					clearSearch.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});
		clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				mEtSearch.getText().clear();
                hideSoftKeyboard();
            }
        });

		groupListView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					hideSoftKeyboard();
					return false;
				}
			});

		mIvBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				hideSoftKeyboard();
				GroupActivity.this.finish();
			}
		});
		mCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hideSoftKeyboard();
				GroupActivity.this.finish();
			}
		});
	}



	/**
	 * load conversation list
	 *
	 * @return
	+    */
	protected List<EMConversation> loadConversationList(){
		// get all conversations
		Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		/**
		 * lastMsgTime will change if there is new message during sorting
		 * so use synchronized to make sure timestamp of last message won't change.
		 */
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0) {
					sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
				}
			}
		}

		List<EMConversation> list = new ArrayList<EMConversation>();
		for (Pair<Long, EMConversation> sortItem : sortList) {
			list.add(sortItem.second);
		}
		return list;
	}

	/**
	 * 搜索界面时，搜索数据
	 * @param prefix
	 */
	private void setSearchData(String prefix) {
		final ArrayList<EMConversation> newValues = new ArrayList<EMConversation>();
		if (emConversations != null && !TextUtils.isEmpty(prefix)) {
			String prefixString = prefix.toString();
			for (int i = 0; i < emConversations.size(); i++) {
				final EMConversation value = emConversations.get(i);
				String username = value.conversationId();

				EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
				if (group != null) {
					username = group.getGroupName();
				} else {
					EaseUser user = EaseUserUtils.getUserInfo(username);
					// TODO: not support Nick anymore
//                        if(user != null && user.getNick() != null)
//                            username = user.getNick();
				}

				// First match against the whole ,non-splitted value
				if (username.startsWith(prefixString)) {
					newValues.add(value);
				} else {
					final String[] words = username.split(" ");
					final int wordCount = words.length;
					// Start at index 0, in case valueText starts with space(s)
					for (String word : words) {
						//if (word.startsWith(prefixString)) {
						if (word.contains(prefixString)) {
							newValues.add(value);
							break;
						}
					}
				}
			}
		}
		//return newValues;

		chatSearchAdapter.setList(newValues);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume() {
		if (!isFromConversationSearch){
			refresh();
		}
		super.onResume();
	}
	
	private void refresh(){
	    grouplist = EMClient.getInstance().groupManager().getAllGroups();
        groupAdapter = new GroupAdapter(this, 1, grouplist);
        groupListView.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
	}

	public void back(View view) {
		finish();
	}
}
