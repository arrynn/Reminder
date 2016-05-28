package cz.muni.fi.pv239.reminder.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

import cz.muni.fi.pv239.reminder.R;
import cz.muni.fi.pv239.reminder.ReminderType;
import cz.muni.fi.pv239.reminder.activity.NewReminderActivity;
import cz.muni.fi.pv239.reminder.model.Reminder;

/**
 * Created by Marek on 28-May-16.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.isConnectedOrConnecting()) {
            WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            String name = wifiInfo.getSSID();
            showNotifications(context, name);
        }
    }

    private void showNotifications(Context context, String name) {
        List<Reminder> reminders = Reminder.getReminderByTypeAndIdentifier(ReminderType.TYPE_WIFI, name);
        for (Reminder reminder : reminders) {
            showNotification(context, reminder);
        }
    }

    private void showNotification(Context context, Reminder reminder) {

        Intent intent = new Intent(context, NewReminderActivity.class);
        intent.putExtra(NewReminderActivity.REMINDER_ID, reminder.getId());
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
        reminder.save();
    }
}
