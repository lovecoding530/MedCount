package com.gregapp.medcount.receiver;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.gregapp.medcount.Helper.MyHelper;
import com.gregapp.medcount.MainActivity;
import com.gregapp.medcount.MyPreference;
import com.gregapp.medcount.R;
import com.gregapp.medcount.SplashActivity;

public class AlarmReceiver extends BroadcastReceiver {

    public static final long REMIND_DELAY = AlarmManager.INTERVAL_DAY * 15;
    public static final String REMIND_MESSAGE = "Please take a photograph of your remaining medications that clearly shows how many are left. Please complete this in 24 hours.";

    @Override
    public void onReceive(Context context, Intent intent) {
        MyPreference myPreference = new MyPreference(context);
        long currentTime = System.currentTimeMillis();
        long lastRemindTime = myPreference.getLastRemindTime();
        long nextRemindTime = myPreference.getNextRemindtime();
        if(currentTime >= nextRemindTime){
            notification(context);
            myPreference.setLastRemindTime();
            int randomDate = MyHelper.getRandomNumberInRange(10, 15);
            long randomPeriod = randomDate * AlarmManager.INTERVAL_DAY;
            myPreference.setNextRemindTime(randomPeriod);
        }
    }

    private void notification(Context context) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(REMIND_MESSAGE))
                        .setContentText(REMIND_MESSAGE);

        Intent notificationIntent = new Intent(context, SplashActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }
}
