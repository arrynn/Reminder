package cz.muni.fi.pv239.reminder.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.muni.fi.pv239.reminder.ReminderType;
import cz.muni.fi.pv239.reminder.model.Condition;
import cz.muni.fi.pv239.reminder.model.Reminder;
import cz.muni.fi.pv239.reminder.utils.NotificationUtils;

/**
 * Created by Marek on 28-May-16.
 */
public class LocationChangedReceiver extends BroadcastReceiver {

    private static final float MIN_DISTANCE = 25;

    @Override
    public void onReceive(Context context, Intent intent) {
        double lat = intent.getDoubleExtra(LocationService.LAT, -1);
        double lng = intent.getDoubleExtra(LocationService.LONG, -1);

        if (lat != -1 && lng != -1) {
            showNotifications(context, new LatLng(lat, lng));
        }
    }

    private void showNotifications(Context context, LatLng latLng) {

        List<Condition> conditions = Condition.getUnfulfilledLocationBasedConditions();
        Set<Long> reminderIdsToCheck = new HashSet<>();

        for (Condition c : conditions) {

            if (c.getLocation() == null) {
                continue;
            }

            boolean isInLocation = isInLocation(c, latLng);

            switch (c.getType()) {

                case TIME:
                    break;
                case WIFI_REACHED:
                case LOCATION_REACHED:
                    if (isInLocation) {
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
                case LOCATION_LEFT:
                    if (isInLocation) {
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

    private boolean isInLocation(Condition c, LatLng latLng) {
        float[] result = new float[1];
        Location.distanceBetween(latLng.latitude, latLng.longitude, c.getLocation().latitude, c.getLocation().longitude, result);
        return result[0] < MIN_DISTANCE;
    }

}
