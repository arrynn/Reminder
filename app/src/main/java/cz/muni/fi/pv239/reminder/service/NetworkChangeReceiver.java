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

import java.util.Date;
import java.util.List;

import cz.muni.fi.pv239.reminder.R;
import cz.muni.fi.pv239.reminder.ReminderType;
import cz.muni.fi.pv239.reminder.activity.ReminderDetailActivity;
import cz.muni.fi.pv239.reminder.activity.ReminderNewActivity;
import cz.muni.fi.pv239.reminder.model.Reminder;
import cz.muni.fi.pv239.reminder.utils.NotificationUtils;

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
            NotificationUtils.showNotification(context, reminder);
        }
    }
}
