/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.help.reward.chat.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.recyclerview.OnItemClickListener;
import com.hyphenate.chat.EMGroup;
import com.help.reward.R;
import com.help.reward.adapter.BaseRecyclerAdapter;
import com.help.reward.adapter.viewholder.SuperViewHolder;

public class GroupAdapter extends BaseRecyclerAdapter<EMGroup>{

    private OnItemClickListener listener;

    public GroupAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.em_row_group;
    }

    @Override
    public void onBindItemHolder(final SuperViewHolder holder, final int position) {
        ImageView mImg = holder.getView(R.id.avatar);
        TextView mName = holder.getView(R.id.name);
        mName.setText(mDataList.get(position).getGroupName());

        if (listener != null) {
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   listener.onItemClick(holder.itemView,position);
               }
           });
        }
    }


    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
