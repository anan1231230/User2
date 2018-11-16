package com.hclz.client.base.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by hjm on 16/7/27.
 */

public class StartAlarmReceiver {

    private static AlarmManager alarmManager;
    private static StartAlarmReceiver startAlarmReceiver = new StartAlarmReceiver();
    private static Context context;
    private PendingIntent pIntent;
    private static boolean isInit = true;

    private StartAlarmReceiver() {

    }

    public static StartAlarmReceiver getInstence(Context con) {
        context = con;
        if (isInit) {
            isInit = false;
            alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        }
        return startAlarmReceiver;
    }

    public void startAlarmReceiver() {
        Intent intent = new Intent("ALARM_RECE" +
                "IVER");
        pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60 * 1000, pIntent);
    }

    public void stopAlarmReceiver() {
        if (alarmManager != null && pIntent != null) {
            alarmManager.cancel(pIntent);
        }
    }
}
