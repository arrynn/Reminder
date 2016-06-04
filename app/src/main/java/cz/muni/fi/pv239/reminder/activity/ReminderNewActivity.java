package cz.muni.fi.pv239.reminder.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.muni.fi.pv239.reminder.R;
import cz.muni.fi.pv239.reminder.adapter.ConditionAdapter;
import cz.muni.fi.pv239.reminder.databinding.ActivityReminderNewBinding;
import cz.muni.fi.pv239.reminder.model.Condition;
import cz.muni.fi.pv239.reminder.model.ConditionType;
import cz.muni.fi.pv239.reminder.model.Reminder;

public class ReminderNewActivity extends AppCompatActivity implements ConditionAdapter.OnConditionClickedListener {

    public static final String REMINDER_ID = "reminder_id";
    public static final String ALLOWED_CONDITION_TYPES = "allowed_condition_types";
    public static final String CONDITION_EXTRA_NAME = "condition_extra";
    private static final int REQUEST_CODE_NEW_CONDITION = 1;

    private ActivityReminderNewBinding mBinding;
    private Reminder mReminder;

    private ArrayAdapter<Condition> conditionsListAdapter;


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

        initConditions(reminderId);
    }

    private void initConditions(Long reminderId) {

        List<Condition> mConditions = new ArrayList<>();
        if (reminderId != null) {
            mConditions = Condition.getConditionsByReminderId(reminderId);
        }

        ListView listview = (ListView) findViewById(R.id.conditions_list_view);

        //make an arrayadapter for listview
        conditionsListAdapter = new ArrayAdapter<Condition>(this, R.layout.list_conditions, mConditions);

        //set adapter to listview
        listview.setAdapter(conditionsListAdapter);

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

    private void saveReminder() {
        if (mBinding.reminderTitle.getText().toString().isEmpty()) {
            ((TextInputLayout) mBinding.reminderTitle.getParent()).setErrorEnabled(true);
            ((TextInputLayout) mBinding.reminderTitle.getParent()).setError("This field is required.");
            return;
        }
        mReminder.title = mBinding.reminderTitle.getText().toString();
        mReminder.description = mBinding.reminderDescription.getText().toString();

        mReminder.save();
        Log.i(this.getClass().getSimpleName(), "saveReminder: " + mReminder.getId());

        for (Condition c : mReminder.conditions) {
            c.setReminderId(mReminder.getId());
            c.save();
            Log.i(this.getClass().getSimpleName(), "saveCondition: " + c.getId());
        }

        finish();
    }


    // Called when the user clicks the add button
    public void addNewCondition(View view) {
        Intent intent = new Intent(this, ConditionNewActivity.class);

        Set<ConditionType> allowedConditionTypes = getAllowedConditionTypes();

        intent.putExtra(ALLOWED_CONDITION_TYPES, allowedConditionTypes.toArray(new ConditionType[]{}));
        startActivityForResult(intent, REQUEST_CODE_NEW_CONDITION);
    }

    @NonNull
    private Set<ConditionType> getAllowedConditionTypes() {
        Set<ConditionType> usedConditions = new HashSet<>();
        for (Condition c : mReminder.conditions) {
            usedConditions.add(c.getType());
        }
        Set<ConditionType> allowedConditionTypes = new HashSet();
        allowedConditionTypes.addAll(Arrays.asList(ConditionType.values()));
        allowedConditionTypes.removeAll(usedConditions);
        return allowedConditionTypes;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_NEW_CONDITION) {
            if (resultCode == RESULT_OK) {
                Condition condition = data.getParcelableExtra(CONDITION_EXTRA_NAME);
                if (condition != null) {
                    mReminder.conditions.add(condition);
                    conditionsListAdapter.add(condition);

                    if (getAllowedConditionTypes().isEmpty()) {
                        ImageButton button = (ImageButton) findViewById(R.id.add_condition_button);
                        button.setVisibility(View.GONE);
                    }

                }
            }
        }


    }


    @Override
    public void onConditionClicked(Condition condition) {
        //TODO
    }
}
