package com.chiemy.androidoreoapidemo;

import android.app.Application;

/**
 * Created: chiemy
 * Date: 17/11/29
 * Description:
 */

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationUtil.createCommonNotificationChannel(this);
    }

}

