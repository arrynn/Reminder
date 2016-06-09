package cz.muni.fi.pv239.reminder.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import cz.muni.fi.pv239.reminder.R;
import cz.muni.fi.pv239.reminder.fragment.AboutFragment;
import cz.muni.fi.pv239.reminder.fragment.RemindersFragment;
import cz.muni.fi.pv239.reminder.model.Condition;
import cz.muni.fi.pv239.reminder.model.ConditionType;
import cz.muni.fi.pv239.reminder.model.Reminder;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // TODO - remove
        // insertTestData();


        setFragment(RemindersFragment.newInstance());
    }

    @Deprecated
    private void insertTestData() {
        // TODO - test data
        if (Reminder.getAllReminders().isEmpty()) {
            Reminder r = new Reminder();
            r.title = "Vynést odpadky";
            r.description = "Nezapomenout, nebo mám zaracha!";
            r.save();

            Condition c = new Condition();
            c.setReminderId(r.getId());
            c.setType(ConditionType.TIME);
            c.setTimeHours(8);
            c.setTimeMinutes(0);
            c.save();
            r.conditions.add(c);

            Condition c1 = new Condition();
            c1.setReminderId(r.getId());
            c1.setType(ConditionType.WIFI_LOST);
            c1.setWifiSsid("INTERNET_DOMA");
            c1.save();
            r.conditions.add(c1);

            Condition c2 = new Condition();
            c2.setReminderId(r.getId());
            c2.setType(ConditionType.LOCATION_REACHED);
            c2.setLocationName("Palackého Třída 33");
            c2.save();
            r.conditions.add(c2);

            r.save();

            Reminder r2 = new Reminder();
            r2.title = "Koupit chleba";
            r2.description = "Kmínový, stačí půlka";
            r2.save();

            Reminder r1 = new Reminder();
            r1.title = "Badminton";
            r1.description = "s Pavlem, hala Sprint";
            r1.save();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_reminders) {
            setFragment(RemindersFragment.newInstance());
        } else if (id == R.id.nav_about) {
            setFragment(AboutFragment.newInstance());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
