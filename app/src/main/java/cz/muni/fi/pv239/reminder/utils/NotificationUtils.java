package cz.muni.fi.pv239.reminder.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;

import java.util.Date;

import cz.muni.fi.pv239.reminder.R;
import cz.muni.fi.pv239.reminder.activity.ReminderDetailActivity;
import cz.muni.fi.pv239.reminder.activity.ReminderNewActivity;
import cz.muni.fi.pv239.reminder.model.Reminder;

/**
 * Created by Marek on 28-May-16.
 */
public class NotificationUtils {

    public static void showNotification(Context context, Reminder reminder) {

        Intent intent = new Intent(context, ReminderDetailActivity.class);
        intent.putExtra(ReminderNewActivity.REMINDER_ID, reminder.getId());
        int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, iUniqueId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_notifications_white_24dp)
                .setContentTitle(reminder.title)
                .setContentText(reminder.description)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(iUniqueId, builder.build());

        reminder.displayed = true;
        reminder.remindedOn = new Date(System.currentTimeMillis());
        reminder.save();
    }

}
