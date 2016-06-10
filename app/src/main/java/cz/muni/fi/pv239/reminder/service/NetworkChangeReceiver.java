package cz.muni.fi.pv239.reminder.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.muni.fi.pv239.reminder.model.Condition;
import cz.muni.fi.pv239.reminder.model.ConditionType;
import cz.muni.fi.pv239.reminder.model.Reminder;
import cz.muni.fi.pv239.reminder.utils.NotificationUtils;

/**
 * Created by Marek on 28-May-16.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Set<Long> reminderIdsToCheck = new HashSet<>();

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.isConnectedOrConnecting()) {
            WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            String name = wifiInfo.getSSID();

            List<Condition> conditions = Condition.getUnfulfilledWifiBasedConditions();


            for (Condition c : conditions) {

                boolean wifiEquals = c.getWifiSsid().equals(name);

                switch (c.getType()) {

                    case TIME:
                    case LOCATION_REACHED:
                    case LOCATION_LEFT:
                        break;
                    case WIFI_REACHED:
                        if (wifiEquals) {
                            if (c.isPreconditionFulfilled()) {
                                c.setFulfilled(true);
                                c.save();
                                reminderIdsToCheck.add(c.getReminderId());
                            }
                        } else if (!c.isPreconditionFulfilled()) {
                            c.setPreconditionFulfilled(true);
                            c.save();
                        }
                        break;
                    case WIFI_LOST:
                        if (wifiEquals) {
                            if (!c.isPreconditionFulfilled()) {
                                c.setFulfilled(true);
                                c.save();
                            }
                        } else if (c.isPreconditionFulfilled()) {
                            c.setFulfilled(true);
                            c.save();
                            reminderIdsToCheck.add(c.getReminderId());
                        }
                        break;
                }
            }


        } else if (activeNetwork == null) {

            {
                // wifi-reached conditions - all have prerequisite fullfiled
                List<Condition> conditions = Condition.getConditionsByFulfilledAndType(false, false, ConditionType.WIFI_REACHED);
                for (Condition c : conditions) {
                    c.setPreconditionFulfilled(true);
                    c.save();
                }
            }

            {
                // wifi-lost conditions - if precondition fulfilled, the condition will become fulfilled
                List<Condition> conditions = Condition.getConditionsByFulfilledAndType(true, false, ConditionType.WIFI_LOST);
                for (Condition c : conditions) {
                    c.setFulfilled(true);
                    c.save();
                    reminderIdsToCheck.add(c.getReminderId());
                }
            }

        }


        // FIXME - optimize this via sql query
        for (Long reminderId : reminderIdsToCheck) {
            Reminder reminder = Reminder.getReminderById(reminderId);
            boolean showReminder = true;
            for (Condition c : reminder.conditions) {
                showReminder &= c.isFulfilled();
            }
            if (showReminder) {
                NotificationUtils.showNotification(context, reminder);
            }
        }


    }

    private void showNotifications(Context context, String name) {
        List<Reminder> reminders = new ArrayList<>();
             /*   Reminder.getReminderByTypeAndIdentifier(ReminderType.TYPE_WIFI, name);*/
        for (Reminder reminder : reminders) {
            NotificationUtils.showNotification(context, reminder);
        }
    }
}
