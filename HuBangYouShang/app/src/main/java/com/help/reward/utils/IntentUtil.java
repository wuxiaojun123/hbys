package com.help.reward.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.help.reward.activity.HelpSeekInfoActivity;
import com.help.reward.activity.OrderDetailsActivity;
import com.help.reward.activity.PersonHomepageActivity;
import com.idotools.utils.ToastUtils;

/**
 * Created by daling on 2017/7/17.
 */

public class IntentUtil {
    public static void startPersonalHomePage(final Activity activity, final String u_id, View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PersonHomepageActivity.class);
                intent.putExtra("u_id", u_id);
                activity.startActivity(intent);
                ActivitySlideAnim.slideInAnim(activity);
            }
        });
    }
}
