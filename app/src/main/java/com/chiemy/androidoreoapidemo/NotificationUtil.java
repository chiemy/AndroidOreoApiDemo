package com.chiemy.androidoreoapidemo;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

/**
 * Created: chiemy
 * Date: 17/11/29
 * Description:
 */

public final class NotificationUtil {
    public static final String CHANNEL_1 = "channel1";
    public static final String CHANNEL_2 = "channel2";

    public static boolean supportNotificationChannel() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public static void createCommonNotificationChannel(Context context) {
        if (supportNotificationChannel()) {
            NotificationManager notificationManager = getNotificationManager(context);

            createNotificationChannel(
                    CHANNEL_1,
                    "渠道1",
                    "渠道1的描述",
                    notificationManager);
            createNotificationChannel(
                    CHANNEL_2,
                    "渠道2",
                    "渠道2的描述",
                    notificationManager
            );
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public static NotificationChannel getNotificationChannel(Context context, String channelId) {
        NotificationManager notificationManager = getNotificationManager(context);
        return notificationManager.getNotificationChannel(channelId);
    }

    public static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static String convertImportance(int value) {
        String valueStr;
        switch (value) {
            case NotificationManager.IMPORTANCE_NONE:
                valueStr = "IMPORTANCE_NONE";
                break;

            case NotificationManager.IMPORTANCE_MIN:
                valueStr = "IMPORTANCE_MIN";
                break;

            case NotificationManager.IMPORTANCE_LOW:
                valueStr = "IMPORTANCE_LOW";
                break;

            case NotificationManager.IMPORTANCE_DEFAULT:
                valueStr = "IMPORTANCE_DEFAULT";
                break;

            case NotificationManager.IMPORTANCE_HIGH:
                valueStr = "IMPORTANCE_HIGH";
                break;

            case NotificationManager.IMPORTANCE_UNSPECIFIED:
                valueStr = "IMPORTANCE_UNSPECIFIED";
                break;

            default:
                valueStr = String.valueOf(value);
        }
        return valueStr;
    }

    public static String convertVisibility(int value) {
        String valueStr;
        switch (value) {
            case Notification.VISIBILITY_PRIVATE:
                valueStr = "VISIBILITY_PRIVATE";
                break;

            case Notification.VISIBILITY_PUBLIC:
                valueStr = "VISIBILITY_PUBLIC";
                break;

            case Notification.VISIBILITY_SECRET:
                valueStr = "VISIBILITY_SECRET";
                break;

            default:
                valueStr = String.valueOf(value);
        }
        return valueStr;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public static void createNotificationChannel(String channelId,
                                                 String channelName,
                                                 String channelDesc,
                                                 NotificationManager notificationManager) {
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
        // Configure the notification channel.
        channel.setDescription(channelDesc);
        channel.enableLights(true);
        // Sets the notification light color for notifications posted to this
        // channel, if the device supports this feature.
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400});
        notificationManager.createNotificationChannel(channel);
    }

    public static void sendTestNotification(Context context,
                                          int notificationId,
                                          String channelId) {
        getNotificationManager(context).notify(notificationId, createTestNotification(context, channelId));
    }

    @NonNull
    private static Notification createTestNotification(Context context, String channelId) {
        NotificationCompat.Builder builder
                = new NotificationCompat.Builder(context, channelId)
                .setContentText("你好")
                .setContentTitle("123")
                .setSmallIcon(R.mipmap.push_notification_icon);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(Notification.PRIORITY_MAX);
        }
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        return notification;
    }
}
