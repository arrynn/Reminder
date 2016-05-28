package cz.muni.fi.pv239.reminder;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.activeandroid.ActiveAndroid;
import com.google.android.gms.location.LocationServices;

import cz.muni.fi.pv239.reminder.service.LocationService;

/**
 * Created by Marek on 28-May-16.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(this, LocationService.class);
            startService(intent);
        }

    }

}
