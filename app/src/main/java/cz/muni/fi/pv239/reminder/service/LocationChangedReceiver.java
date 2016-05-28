package cz.muni.fi.pv239.reminder.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import cz.muni.fi.pv239.reminder.ReminderType;
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
        List<Reminder> reminders = Reminder.getReminderByType(ReminderType.TYPE_LOCATION);

        for (Reminder reminder : reminders) {
            float[] result = new float[1];
            Location.distanceBetween(latLng.latitude, latLng.longitude, reminder.location.latitude, reminder.location.longitude, result);
            if (result[0] < MIN_DISTANCE) {
                NotificationUtils.showNotification(context, reminder);
            }

        }
    }
}
