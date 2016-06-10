package cz.muni.fi.pv239.reminder.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cz.muni.fi.pv239.reminder.activity.ReminderNewActivity;
import cz.muni.fi.pv239.reminder.model.Condition;
import cz.muni.fi.pv239.reminder.model.Reminder;
import cz.muni.fi.pv239.reminder.utils.NotificationUtils;

public class AlarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Long conditionId = intent.getLongExtra(ReminderNewActivity.CONDITION_ID, -1);
        Log.i(getClass().getName(), "conditionId=" + conditionId);
        Condition condition = Condition.getConditionById(conditionId);

        if (condition != null) {
            condition.setFulfilled(true);
            condition.save();
        } else {
            return;
        }

        Reminder reminder = Reminder.getReminderById(condition.getReminderId());
        boolean showReminder = true;
        for (Condition c : reminder.conditions) {
            showReminder &= c.isFulfilled();
        }
        if (showReminder) {
            NotificationUtils.showNotification(context, reminder);
        }

    }

}