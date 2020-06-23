package com.markjmind.uni.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.markjmind.uni.common.Jwc;

/**
 * 퍼미션 추가 : <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
 * manifest : <service android:name=".ForeService"/>
 *
 * UniNotification unf = new UniNotification(this, UniNotification.IMPORTANCE_HIGH, "uni_foreservice", "알림설정");
 * Notification notification = unf.getSimpleNotification(MainActivity.class, R.drawable.ic_check_on, "제목", "내용");
 * startForeground(10000, notification); //foreground 서비스일경우
 *
 */
public class UniNotification {
    //아무데도 보이지않고 차단
    public static final int IMPORTANCE_NONE = NotificationManagerCompat.IMPORTANCE_NONE;

    public static final int IMPORTANCE_MIN = NotificationManagerCompat.IMPORTANCE_MIN;
    public static final int IMPORTANCE_LOW = NotificationManagerCompat.IMPORTANCE_LOW;
    public static final int IMPORTANCE_DEFAULT = NotificationManagerCompat.IMPORTANCE_DEFAULT;

    //헤드업 알림 추가
    public static final int IMPORTANCE_HIGH = NotificationManagerCompat.IMPORTANCE_HIGH;

    private NotificationCompat.Builder builder;
    private Context context;

    public UniNotification(Context context, int importance, String channelId, String channelDesc){
        this.context = context;
        makeBuilder(channelId, importance, channelDesc);
    }

    public UniNotification(Context context, String channelId, String channelDesc){
        this(context, IMPORTANCE_HIGH, channelId, channelDesc);
    }

    private void makeBuilder(String channelId, int importance, String channelDesc) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            prepareChannel(channelId, importance, channelDesc);
            builder = new NotificationCompat.Builder(context, channelId);
        } else {
            builder = new NotificationCompat.Builder(context);
        }
    }

    /**
     * 채널 정보 셋팅하는것 생각해야함
     * @param id
     * @param importance
     * @param channelDesc
     */
    @TargetApi(26)
    private void prepareChannel(String id, int importance, String channelDesc) {
        final String appName = Jwc.getAppName(context);
        final NotificationManager nm = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);

        if(nm != null) {
            NotificationChannel nChannel = nm.getNotificationChannel(id);

            if (nChannel == null) {
                nChannel = new NotificationChannel(id, appName, importance);
                nChannel.setDescription(channelDesc);
                nm.createNotificationChannel(nChannel);
            }
        }
    }

    public void setStyle(String summary, String bigText){
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.setSummaryText(summary);
        style.bigText(bigText);
        style.setBigContentTitle(null);
        builder.setStyle(style);
    }

    public Notification getSimpleNotification(Class<?> activityClass, int icon, String title, String contents) {
        Intent intent = new Intent(context, activityClass);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setOngoing(true)
                .setContentIntent(pIntent)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(contents);

        Notification notification = builder.build();
        return notification;
    }

    public static void cancelNotification(Context context, int notificationId){
        NotificationManager nm = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
        nm.cancel(notificationId);
    }
}
