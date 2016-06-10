package cz.muni.fi.pv239.reminder.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.PlaceReport;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv239.reminder.R;
import cz.muni.fi.pv239.reminder.databinding.ActivityConditionNewBinding;
import cz.muni.fi.pv239.reminder.model.Condition;
import cz.muni.fi.pv239.reminder.model.ConditionType;
import cz.muni.fi.pv239.reminder.service.LocationService;

public class ConditionNewActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    public static final int PERMISSION_LOCATION = 0;
    private static final int PLACE_PICKER_REQUEST = 1;

    private ActivityConditionNewBinding mBinding;

    private GoogleApiClient mGoogleApiClient;
    private Place mSelectedPlace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_condition_new);

        initAllowedConditionTypes();
        initWifiSpinner();
        initLocations();

    }

    private void initAllowedConditionTypes() {
        ConditionType[] allowedConditionTypes = (ConditionType[]) getIntent().getSerializableExtra(ReminderNewActivity.ALLOWED_CONDITION_TYPES);
        ArrayAdapter conditionTypeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, allowedConditionTypes);
        final Spinner conditionTypeSpinner = mBinding.spinnerConditionType;
        conditionTypeSpinner.setAdapter(conditionTypeAdapter);
        conditionTypeSpinner.setSelection(0);

        final View timePicker = findViewById(R.id.timePicker);
        final View wifiSpinner = findViewById(R.id.spinner_select_wifi);
        final View locationView = findViewById(R.id.view_location);

        conditionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ConditionType type = (ConditionType) conditionTypeSpinner.getItemAtPosition(position);
                switch (type) {

                    case TIME:
                        timePicker.setVisibility(View.VISIBLE);
                        wifiSpinner.setVisibility(View.GONE);
                        locationView.setVisibility(View.GONE);
                        break;
                    case WIFI_REACHED:
                    case WIFI_LOST:
                        timePicker.setVisibility(View.GONE);
                        wifiSpinner.setVisibility(View.VISIBLE);
                        locationView.setVisibility(View.GONE);
                        break;
                    case LOCATION_REACHED:
                    case LOCATION_LEFT:
                        timePicker.setVisibility(View.GONE);
                        wifiSpinner.setVisibility(View.GONE);
                        locationView.setVisibility(View.VISIBLE);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                timePicker.setVisibility(View.GONE);
                wifiSpinner.setVisibility(View.GONE);
                locationView.setVisibility(View.GONE);
            }

        });
    }


    private void initWifiSpinner() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        List<String> wifiList = new ArrayList<>();

        if (wifiManager != null && wifiManager.getConfiguredNetworks() != null) {
            for (WifiConfiguration wifiConfiguration : wifiManager.getConfiguredNetworks()) {
                wifiList.add(wifiConfiguration.SSID);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wifiList);
            mBinding.spinnerSelectWifi.setAdapter(adapter);


            // setCurrentWifi
            if (wifiManager.getConnectionInfo() != null) {
                String currentWifi = wifiManager.getConnectionInfo().getSSID();

                for (int i = 0; i < mBinding.spinnerSelectWifi.getAdapter().getCount(); i++) {
                    String item = (String) mBinding.spinnerSelectWifi.getAdapter().getItem(i);
                    if (item.equals(currentWifi)) {
                        mBinding.spinnerSelectWifi.setSelection(i);
                    }
                }
            }

        }
    }

    private void initLocations() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_condition_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                boolean isValid = validateCondition();
                if (isValid) {
                    Intent intent = new Intent();
                    intent.putExtra(ReminderNewActivity.CONDITION_EXTRA_NAME, parseConditionFromFields());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                return true;
            default:
                return false;
        }

    }

    private boolean validateCondition() {

        ConditionType conditionType = (ConditionType) mBinding.spinnerConditionType.getSelectedItem();
        if (conditionType == null) {
            TextView errorText = (TextView) ((Spinner) mBinding.spinnerConditionType).getSelectedView();
            errorText.setError("empty");
            errorText.setTextColor(Color.RED);
            errorText.setText("This field is required.");
            return false;
        }

        switch (conditionType) {

            case TIME:
                // TimePicker ensures correctness
                break;
            case WIFI_REACHED:
            case WIFI_LOST:
                if (mBinding.spinnerSelectWifi.getSelectedItem() == null) {
                    // FIXME - error message
                    return false;
                }
                break;
            case LOCATION_REACHED:
            case LOCATION_LEFT:
                if (mSelectedPlace == null) {
                    // FIXME - error message
                    return false;
                }
                break;
        }

        return true;
    }

    private Condition parseConditionFromFields() {
       final Condition c = new Condition();
        c.setType((ConditionType) mBinding.spinnerConditionType.getSelectedItem());

        switch (c.getType()) {

            case TIME:
                c.setTimeHours(mBinding.timePicker.getCurrentHour());
                c.setTimeMinutes(mBinding.timePicker.getCurrentMinute());
                c.setLocation(null);
                c.setLocationName(null);
                c.setPreconditionFulfilled(true);
                break;
            case WIFI_REACHED:
            case WIFI_LOST:
                c.setWifiSsid((String) mBinding.spinnerSelectWifi.getSelectedItem());

                // FIXME - set current location
                /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_LOCATION);
                } else {

                    PendingResult<PlaceLikelihoodBuffer> currentPlace = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);

                    currentPlace.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                        @Override
                        public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                            for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                c.setLocation(placeLikelihood.getPlace().getLatLng());
                                c.setLocationName(getPlaceName(placeLikelihood.getPlace()));
                                break;
                            }
                            likelyPlaces.release();
                        }
                    });

                }*/

                break;
            case LOCATION_REACHED:
            case LOCATION_LEFT:
                // this is being set in onActivityResult
                c.setLocation(mSelectedPlace.getLatLng());
                c.setLocationName(getPlaceName(mSelectedPlace));
                break;
        }

        return c;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_select_location:
                selectPlace();
                break;
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public void selectPlace() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_LOCATION);
        } else {

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            try {
                startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(getClass().getName(), "onActivityResult");
        if (requestCode == PLACE_PICKER_REQUEST) {
            Log.i(getClass().getName(), "resultCode=" + resultCode);
            if (resultCode == RESULT_OK) {
                mSelectedPlace = PlacePicker.getPlace(this, data);
                Log.i(getClass().getName(), "mSelectedPlace=" + mSelectedPlace);
                if (mSelectedPlace != null) {
                    final TextView locationTextView = (TextView) findViewById(R.id.text_view_location);
                    locationTextView.setText(getPlaceName(mSelectedPlace));
                }
            }
        }
    }

    private String getPlaceName(Place place) {
        if (place == null) {
            return null;
        }
        Object name = place.getName() != null ? place.getName() : place.getAddress();
        return java.util.Objects.toString(name, "");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, LocationService.class);
                    startService(intent);
                    selectPlace();
                }
                break;
            }
        }
    }


}
