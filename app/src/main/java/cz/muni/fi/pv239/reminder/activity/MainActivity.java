package cz.muni.fi.pv239.reminder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.activeandroid.ActiveAndroid;

import java.util.List;

import cz.muni.fi.pv239.reminder.R;
import cz.muni.fi.pv239.reminder.adapter.ReminderAdapter;
import cz.muni.fi.pv239.reminder.ReminderType;
import cz.muni.fi.pv239.reminder.model.Label;
import cz.muni.fi.pv239.reminder.model.Reminder;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ReminderAdapter.OnReminderClickedListener,
        View.OnClickListener {

    private RecyclerView mRecyclerView;
    private ReminderAdapter mAdapter;
    private FloatingActionButton mFab;

    private List<Reminder> mReminders;

    @Override
    public void onReminderClicked(Reminder reminder) {
        Intent intent = new Intent(MainActivity.this, NewReminderActivity.class);
        intent.putExtra(NewReminderActivity.REMINDER_ID, reminder.getId());
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

//        createDummyReminders();

        // specify an adapter (see also next example)
        mReminders = Reminder.getAllReminders();
        mAdapter = new ReminderAdapter(mReminders, this);
        mRecyclerView.setAdapter(mAdapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReminders.clear();
        mReminders.addAll(Reminder.getAllReminders());
        mAdapter.notifyDataSetChanged();
    }

    private void createDummyReminders() {
        try {
            ActiveAndroid.beginTransaction();
            for (int i = 0; i < 10; i++) {
                Reminder r = new Reminder();
                r.title = "Title " + i;
                r.description = "Description " + i;
                if (i < 5) {
                    r.type = ReminderType.TYPE_LOCATION;
                } else {
                    r.type = ReminderType.TYPE_WIFI;
                }
                r.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent intent = new Intent(MainActivity.this, NewReminderActivity.class);
                startActivity(intent);
                break;
        }
    }
}
