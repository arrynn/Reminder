package cz.muni.fi.pv239.reminder.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cz.muni.fi.pv239.reminder.R;
import cz.muni.fi.pv239.reminder.activity.ReminderDetailActivity;
import cz.muni.fi.pv239.reminder.activity.ReminderNewActivity;
import cz.muni.fi.pv239.reminder.adapter.ReminderAdapter;
import cz.muni.fi.pv239.reminder.databinding.FragmentRemindersBinding;
import cz.muni.fi.pv239.reminder.model.Reminder;

public class RemindersFragment extends Fragment implements View.OnClickListener, ReminderAdapter.OnReminderClickedListener {

    private ReminderAdapter mAdapter;

    private List<Reminder> mReminders;

    private FragmentRemindersBinding mBinding;


    public RemindersFragment() {
        // Required empty public constructor
    }

    public static RemindersFragment newInstance() {
        return new RemindersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mReminders.clear();
        mReminders.addAll(Reminder.getAllReminders());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_reminders, container, false);

        mBinding.fab.setOnClickListener(this);
        // in content do not change the layout size of the RecyclerView
        mBinding.recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mBinding.recyclerView.setLayoutManager(mLayoutManager);

//        createDummyReminders();

        // specify an adapter (see also next example)
        mReminders = Reminder.getAllReminders();
        mAdapter = new ReminderAdapter(mReminders, this);
        mBinding.recyclerView.setAdapter(mAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent intent = new Intent(getActivity(), ReminderNewActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onReminderClicked(Reminder reminder) {
        Intent intent = new Intent(getActivity(), ReminderDetailActivity.class);
        intent.putExtra(ReminderNewActivity.REMINDER_ID, reminder.getId());
        startActivity(intent);
    }
}
