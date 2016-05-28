package cz.muni.fi.pv239.reminder.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv239.reminder.R;
import cz.muni.fi.pv239.reminder.ReminderType;
import cz.muni.fi.pv239.reminder.databinding.ActivityNewReminderBinding;
import cz.muni.fi.pv239.reminder.model.Reminder;

public class NewReminderActivity extends AppCompatActivity {

    public static String REMINDER_ID = "reminder_id";

    private ActivityNewReminderBinding mBidning;
    private Reminder mReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBidning = DataBindingUtil.setContentView(this, R.layout.activity_new_reminder);
        Long reminderId = getIntent().getLongExtra(REMINDER_ID, -1);
        mReminder = Reminder.getReminderById(reminderId);
        mBidning.setReminder(mReminder);

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        List<String> wifiList = new ArrayList<>();
        for (WifiConfiguration wifiConfiguration : wifiManager.getConfiguredNetworks()) {
            wifiList.add(wifiConfiguration.SSID);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wifiList);
        mBidning.spinnerSelectWifi.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_new_reminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveReminder();
                return true;
            default:
                return false;
        }
    }

    private void saveReminder() {
        if (mReminder == null) {
            mReminder = new Reminder();
        }
        mReminder.title = mBidning.reminderTitle.getText().toString();
        mReminder.description = mBidning.reminderDescription.getText().toString();
        mReminder.type = mBidning.radioTypeWifi.isChecked() ? ReminderType.TYPE_WIFI : ReminderType.TYPE_GPS;
        mReminder.save();
        finish();
    }
}
