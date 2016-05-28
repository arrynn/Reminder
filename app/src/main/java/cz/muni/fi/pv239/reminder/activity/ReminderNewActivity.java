package cz.muni.fi.pv239.reminder.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv239.reminder.R;
import cz.muni.fi.pv239.reminder.ReminderType;
import cz.muni.fi.pv239.reminder.databinding.ActivityReminderNewBinding;
import cz.muni.fi.pv239.reminder.model.Reminder;

public class ReminderNewActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    public static String REMINDER_ID = "reminder_id";

    private int PLACE_PICKER_REQUEST = 1;

    private ActivityReminderNewBinding mBinding;
    private Reminder mReminder;

    private GoogleApiClient mGoogleApiClient;

    private Place mSelectedPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_reminder_new);
        Long reminderId = getIntent().getLongExtra(REMINDER_ID, -1);
        mReminder = Reminder.getReminderById(reminderId);
        if (mReminder == null) {
            mReminder = new Reminder();
        }
        mBinding.setReminder(mReminder);

        mBinding.radioTypeLocation.setOnCheckedChangeListener(this);
        mBinding.radioTypeWifi.setOnCheckedChangeListener(this);
        mBinding.buttonSelectLocation.setOnClickListener(this);

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        List<String> wifiList = new ArrayList<>();

        if (wifiManager != null && wifiManager.getConfiguredNetworks() != null) {
            for (WifiConfiguration wifiConfiguration : wifiManager.getConfiguredNetworks()) {
                wifiList.add(wifiConfiguration.SSID);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wifiList);
            mBinding.spinnerSelectWifi.setAdapter(adapter);

            if (mReminder.type == ReminderType.TYPE_WIFI && mReminder.identifier != null) {
                setProperWifi();
            }
        }

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_reminder_new, menu);
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

    private void setProperWifi() {
        for (int i = 0; i < mBinding.spinnerSelectWifi.getAdapter().getCount(); i++) {
            String item = (String) mBinding.spinnerSelectWifi.getAdapter().getItem(i);
            if(item.equals(mReminder.identifier)) {
                mBinding.spinnerSelectWifi.setSelection(i       );
            }
        }
    }

    private void saveReminder() {
        mReminder.title = mBinding.reminderTitle.getText().toString();
        mReminder.description = mBinding.reminderDescription.getText().toString();
        if (mBinding.radioTypeLocation.isChecked()) {
            mReminder.type = ReminderType.TYPE_LOCATION;
            mReminder.location = mSelectedPlace.getLatLng();
            mReminder.identifier = mSelectedPlace.getName() != null ? mSelectedPlace.getName().toString() : mSelectedPlace.getAddress().toString();
        } else if (mBinding.radioTypeWifi.isChecked()) {
            mReminder.type = ReminderType.TYPE_WIFI;
            mReminder.identifier = mBinding.spinnerSelectWifi.getSelectedItem().toString();
        }
        mReminder.save();
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.radio_type_location:
                if (isChecked) {
                    mBinding.buttonSelectLocation.setVisibility(View.VISIBLE);
                    mBinding.spinnerSelectWifi.setVisibility(View.GONE);
                }
                break;
            case R.id.radio_type_wifi:
                if (isChecked && mReminder != null) {
                    mBinding.buttonSelectLocation.setVisibility(View.GONE);
                    mBinding.spinnerSelectWifi.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_select_location:
                selectPlace();
                break;
        }
    }

    private void selectPlace() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                mSelectedPlace = PlacePicker.getPlace(this, data);
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}