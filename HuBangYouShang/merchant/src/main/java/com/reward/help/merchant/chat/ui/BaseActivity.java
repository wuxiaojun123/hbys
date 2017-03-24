/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.reward.help.merchant.chat.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.reward.help.merchant.utils.ActivitySlideAnim;

import rx.Subscription;

@SuppressLint("Registered")
public class BaseActivity extends EaseBaseActivity {

    protected Subscription subscribe;
    protected Context mContext;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mContext = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // umeng
    }

    @Override
    protected void onStart() {
        super.onStart();
        // umeng
    }

    @Override
    public void onBackPressed() {
        finish();
        ActivitySlideAnim.slideOutAnim(this);
//        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        super.onDestroy();
    }
}
