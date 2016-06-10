package cz.muni.fi.pv239.reminder.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.GregorianCalendar;

import cz.muni.fi.pv239.reminder.model.Condition;
import cz.muni.fi.pv239.reminder.model.ConditionType;
import cz.muni.fi.pv239.reminder.service.AlarmReciever;

public class AlarmUtils {

    public static long getTimeToScheduleAlarm(Condition condition) {
        if (condition != null & ConditionType.TIME.equals(condition.getType())) {
            Calendar time = new GregorianCalendar();
            time.set(Calendar.HOUR, condition.getTimeHours());
            time.set(Calendar.MINUTE, condition.getTimeMinutes());
            time.set(Calendar.SECOND, 0);

            if (time.after(new GregorianCalendar())) {
                // time is after now - ok
            } else {
                time.add(Calendar.DAY_OF_YEAR, 1);
            }

            return time.getTimeInMillis();
        }
        return 0;
    }

}