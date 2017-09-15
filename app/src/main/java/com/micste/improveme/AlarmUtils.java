package com.micste.improveme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 *  Alarm utility class
 */

public class AlarmUtils {

    private static final String TAG = "AlarmUtils";
    private final Context context;
    private final AlarmManager alarmManager;

    public AlarmUtils(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * Sets an alarm according to object Goal time and hour variable.
     * Always schedules the alarm weekly (INTERVAL_DAY * 7)
     */
    public void scheduleAlarm(Goal goal) {
        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        notificationIntent.putExtra("TITLE", goal.getTitle());
        notificationIntent.putExtra("DESCRIPTION", goal.getDescription());
        notificationIntent.putExtra("ID", goal.getNotificationId());

        PendingIntent broadcast = PendingIntent.getBroadcast(context, goal.getNotificationId(),
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, goal.getCalendarDay());
        Log.d(TAG, String.valueOf(goal.getHour()) + " " + String.valueOf(goal.getMinute()));
        calendar.set(Calendar.HOUR_OF_DAY, goal.getHour());
        calendar.set(Calendar.MINUTE, goal.getMinute());
        calendar.set(Calendar.SECOND, 0);


        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, broadcast);

    }

    public void cancelAlarm(Goal goal) {
        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        PendingIntent broadcast = PendingIntent.getBroadcast(context, goal.getNotificationId(),
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(broadcast);

    }

}
