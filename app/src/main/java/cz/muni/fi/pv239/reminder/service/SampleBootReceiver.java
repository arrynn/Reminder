package cz.muni.fi.pv239.reminder.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.GregorianCalendar;
import java.util.List;

import cz.muni.fi.pv239.reminder.model.Condition;
import cz.muni.fi.pv239.reminder.model.ConditionType;
import cz.muni.fi.pv239.reminder.utils.AlarmUtils;

public class SampleBootReceiver extends BroadcastReceiver {

    public static final String CONDITION_ID = "condition_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(getClass().getName(), intent.getAction());
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            // schedule alarm for all time-based conditions

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            List<Condition> conditions = Condition.getUnfulfilledTimeBasedConditions();

            for (Condition condition : conditions) {
                if (ConditionType.TIME.equals(condition.getType())) {
                    long time = AlarmUtils.getTimeToScheduleAlarm(condition);
                    if (time <= 0) {
                        return;
                    }
                    Intent intentAlarm = new Intent(context, AlarmReciever.class);
                    intentAlarm.putExtra(CONDITION_ID, condition.getId());
                    alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
                    Log.i(getClass().getName(), "scheduledAlarm. conditionId=" + condition.getId());
                    Log.i(getClass().getName(), "scheduledAlarm. in time (ms)" + (time - new GregorianCalendar().getTimeInMillis()));
                }

            }

        }
    }


}