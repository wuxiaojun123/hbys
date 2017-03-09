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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMGroup;
import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GroupTypesAdapter extends RecyclerView.Adapter<SuperViewHolder> {
    //为了区分加入标签
    private final int SEARCH = 0, GROUPITEM = 1;
    private LayoutInflater inflater;
    private String newGroup;
    private String addPublicGroup;
    protected List<EMGroup> mDataList = new ArrayList<>();


    public GroupTypesAdapter(Context context, int newGroup, int addPublicGroup) {
        this.inflater = LayoutInflater.from(context);
        this.newGroup = context.getResources().getString(R.string.The_new_group_chat);
        this.addPublicGroup = context.getResources().getString(R.string.add_public_group_chat);
    }


    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        SuperViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case SEARCH:
                View v1 = inflater.inflate(R.layout.em_search_bar_with_padding, parent, false);
                viewHolder = new SuperViewHolder(v1);
                break;
            /*case GROUPITEM:
				View v2 = inflater.inflate(R.layout.layout_viewholder2, parent, false);
				break;*/
            default:
                View v = inflater.inflate(R.layout.em_row_group, parent, false);
                viewHolder = new SuperViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SuperViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case SEARCH:
                //TODO
                break;
            default:
                ImageView mImg = holder.getView(R.id.avatar);
                TextView mName = holder.getView(R.id.name);
                mName.setText(mDataList.get(position).getGroupName());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        /*if (position == 0) {
            return SEARCH;
        } /*else if (position == 1) {
			return 1;
		} else if (position == 2) {
			return 2;
		} else {*/
            return GROUPITEM;
        /*}*/
    }

    @Override
    public int getItemCount() {
        //return getDataList().size() + 1;
        return getDataList().size();
    }

    public void setDataList(Collection<EMGroup> list) {
        this.mDataList.clear();
        this.mDataList.addAll(list);
        notifyDataSetChanged();
    }

    public void addAll(Collection<EMGroup> list) {
        int lastIndex = this.mDataList.size();
        if (this.mDataList.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    public void remove(int position) {
        this.mDataList.remove(position);
        notifyItemRemoved(position);

        if (position != (getDataList().size())) { // 如果移除的是最后一个，忽略
            notifyItemRangeChanged(position, this.mDataList.size() - position);
        }
    }

    public List<EMGroup> getDataList() {
        return mDataList;
    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }
}
