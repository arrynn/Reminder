package cz.muni.fi.pv239.reminder.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cz.muni.fi.pv239.reminder.R;
import cz.muni.fi.pv239.reminder.model.Reminder;


/**
 * Created by Marek on 28-May-16.
 */
public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.BindingHolder> {

    private List<Reminder> mReminders;
    private OnReminderClickedListener mClickListener;

    public interface OnReminderClickedListener {
        void onReminderClicked(Reminder reminder);
    }

    public ReminderAdapter(List<Reminder> mReminders, OnReminderClickedListener mClickListener) {
        this.mReminders = mReminders;
        this.mClickListener = mClickListener;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewDataBinding = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder, parent, false);
        return new BindingHolder(viewDataBinding);    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        final Reminder reminder = mReminders.get(position);
        holder.binding.setVariable(cz.muni.fi.pv239.reminder.BR.reminder, reminder);
        holder.binding.getRoot().setTag(position);
    }

    @Override
    public int getItemCount() {
        return mReminders.size();
    }

    private void onItemClicked(int position) {
        mClickListener.onReminderClicked(mReminders.get(position));
    }

    public class BindingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ViewDataBinding binding;

        public BindingHolder(View rowView) {
            super(rowView);
            binding = DataBindingUtil.bind(rowView);
            binding.getRoot().setOnClickListener(this);
        }
        public ViewDataBinding getBinding() {
            return binding;
        }

        @Override
        public void onClick(View v) {
            onItemClicked((Integer) binding.getRoot().getTag());
        }
    }

}
