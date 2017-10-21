package com.help.reward.rxbus.event.type;

/**
 * Created by wuxiaojun on 17-6-15.
 */

public class UpdateLoginDataRxbusType {

    public boolean isUpdate;
    public boolean updatePersonInfo;

    public UpdateLoginDataRxbusType(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public void setUpdatePersonInfo(boolean updatePersonInfo) {
        this.updatePersonInfo = updatePersonInfo;
    }

}
