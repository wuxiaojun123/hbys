package com.help.reward.chat.db;

import android.content.Context;

import java.util.List;
import java.util.Map;

/**
 * Created by ADBrian on 08/04/2017.
 */

public class TopUserDao {

    public static final String TABLE_NAME = "topuser";
    public static final String COLUMN_NAME_ID = "topuser_id";
    public static final String COLUMN_NAME_TIME = "time";
    public static final String COLUMN_NAME_IS_GROUP = "is_group";
    public TopUserDao(Context context) {
    }

    public Map<String,TopUser> getTopUserList() {
        return  DemoDBManager.getInstance().getTopUserList();
    }

    public void saveTopUserList(List<TopUser> contactList) {
        DemoDBManager.getInstance().setTopUserList(contactList);
    }

    public void saveTopUser(TopUser topUser){
        DemoDBManager.getInstance().saveTopUser(topUser);
    }

    public void deleteTopUser(TopUser topUser) {
        DemoDBManager.getInstance().deleteTopUser(topUser);
    }
}
