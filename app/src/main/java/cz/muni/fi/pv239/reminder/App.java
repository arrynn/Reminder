package cz.muni.fi.pv239.reminder;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Created by Marek on 28-May-16.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }

}
