package cz.muni.fi.pv239.reminder.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cz.muni.fi.pv239.reminder.R;
import cz.muni.fi.pv239.reminder.model.Condition;



public class ConditionAdapter extends RecyclerView.Adapter<ConditionAdapter.BindingHolder> {

    private List<Condition> mConditions;
    private OnConditionClickedListener mClickListener;

    public interface OnConditionClickedListener {
        void onConditionClicked(Condition condition);
    }

    public ConditionAdapter(List<Condition> mConditions, OnConditionClickedListener mClickListener) {
        this.mConditions = mConditions;
        this.mClickListener = mClickListener;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewDataBinding = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_condition, parent, false);
        return new BindingHolder(viewDataBinding);    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        final Condition condition = mConditions.get(position);
        holder.binding.setVariable(cz.muni.fi.pv239.reminder.BR.condition, condition);
        holder.binding.getRoot().setTag(position);
    }

    @Override
    public int getItemCount() {
        return mConditions.size();
    }

    private void onItemClicked(int position) {
        mClickListener.onConditionClicked(mConditions.get(position));
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
