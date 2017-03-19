package com.reward.help.merchant.utils;

import android.app.Activity;

import com.reward.help.merchant.R;

/**
 * Created by wuxiaojun on 2017/2/22.
 */

public class ActivitySlideAnim {

    public static void slideInAnim(Activity activity) {
        activity.overridePendingTransition(R.anim.anim_slide_right_in, R.anim.anim_slide_left_out);
    }


    public static void slideOutAnim(Activity activity) {
        //anim_slide_left_out
        activity.overridePendingTransition(R.anim.anim_slide_left_in, R.anim.anim_slide_right_out);
    }

}
