package com.reward.help.merchant.chat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fanjunqing on 18/03/2017.
 */

public class ChatSearchAdapter extends ArrayAdapter<EMConversation>  {


    private List<EMConversation> conversationList;

    public ChatSearchAdapter(@NonNull Context context) {
        super(context, 0);
        this.conversationList = new ArrayList<EMConversation>();
    }
    public void setList(List<EMConversation> conversationList){
        this.conversationList.clear();
        this.conversationList.addAll(conversationList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return conversationList == null ? 0 : conversationList.size();
    }

    @Override
    public EMConversation getItem(int position) {
        return conversationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(com.hbys.chatlibrary.R.layout.em_item_search, parent, false);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(com.hyphenate.easeui.R.id.name);
            holder.message = (TextView) convertView.findViewById(com.hyphenate.easeui.R.id.message);
            holder.time = (TextView) convertView.findViewById(com.hyphenate.easeui.R.id.time);
            holder.avatar = (ImageView) convertView.findViewById(com.hyphenate.easeui.R.id.avatar);
            holder.msgState = convertView.findViewById(com.hyphenate.easeui.R.id.msg_state);
            holder.motioned = (TextView) convertView.findViewById(com.hyphenate.easeui.R.id.mentioned);
            convertView.setTag(holder);
        }

        // get conversation
        EMConversation conversation = getItem(position);
        // get username or group id
        String username = conversation.conversationId();

        if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
            String groupId = conversation.conversationId();
            if(EaseAtMessageHelper.get().hasAtMeMsg(groupId)){
                holder.motioned.setVisibility(View.VISIBLE);
            }else{
                holder.motioned.setVisibility(View.GONE);
            }
            // group message, show group avatar
            holder.avatar.setImageResource(com.hyphenate.easeui.R.drawable.ease_group_icon);
            EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
            holder.name.setText(group != null ? group.getGroupName() : username);
        } else if(conversation.getType() == EMConversation.EMConversationType.ChatRoom){
            holder.avatar.setImageResource(com.hyphenate.easeui.R.drawable.ease_group_icon);
            EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(username);
            holder.name.setText(room != null && !TextUtils.isEmpty(room.getName()) ? room.getName() : username);
            holder.motioned.setVisibility(View.GONE);
        }else {
            EaseUserUtils.setUserAvatar(getContext(), username, holder.avatar);
            EaseUserUtils.setUserNick(username, holder.name);
            holder.motioned.setVisibility(View.GONE);
        }

        if (conversation.getAllMsgCount() != 0) {
            // show the content of latest message
            EMMessage lastMessage = conversation.getLastMessage();
            String content = null;
            //if(cvsListHelper != null){
            //    content = cvsListHelper.onSetItemSecondaryText(lastMessage);
            //}
            holder.message.setText(EaseSmileUtils.getSmiledText(getContext(), EaseCommonUtils.getMessageDigest(lastMessage, (this.getContext()))),
                    TextView.BufferType.SPANNABLE);
            if(content != null){
                holder.message.setText(content);
            }
            holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
            if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() == EMMessage.Status.FAIL) {
                holder.msgState.setVisibility(View.VISIBLE);
            } else {
                holder.msgState.setVisibility(View.GONE);
            }
        }

        return convertView;
    }



    private static class ViewHolder {
        /** who you chat with */
        TextView name;
        /** content of last message */
        TextView message;
        /** time of last message */
        TextView time;
        /** avatar */
        ImageView avatar;
        /** status of last message */
        View msgState;
        /** layout */
        TextView motioned;
    }

}
