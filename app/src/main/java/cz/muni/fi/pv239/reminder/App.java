package cz.muni.fi.pv239.reminder;

import android.app.Application;
import android.content.Intent;

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

        Intent intent = new Intent(this, LocationService.class);
        startService(intent);

    }

}
