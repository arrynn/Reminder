package cz.muni.fi.pv239.reminder.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import cz.muni.fi.pv239.reminder.R;
import cz.muni.fi.pv239.reminder.databinding.ActivityReminderDetailBinding;
import cz.muni.fi.pv239.reminder.model.Reminder;

/**
 * Created by Marek on 28-May-16.
 */
public class ReminderDetailActivity extends AppCompatActivity {


    public static String REMINDER_ID = "reminder_id";

    private ActivityReminderDetailBinding mBinding;
    private Reminder mReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_reminder_detail);
        Long reminderId = getIntent().getLongExtra(REMINDER_ID, -1);
        mReminder = Reminder.getReminderById(reminderId);
        mBinding.setReminder(mReminder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_reminder_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                editReminder();
                return true;
            default:
                return false;
        }
    }

    private void editReminder() {
        Intent intent = new Intent(ReminderDetailActivity.this, ReminderNewActivity.class);
        intent.putExtra(ReminderNewActivity.REMINDER_ID, mReminder.getId());
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}
