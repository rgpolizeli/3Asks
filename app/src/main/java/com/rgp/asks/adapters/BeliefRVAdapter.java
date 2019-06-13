package com.rgp.asks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rgp.asks.listeners.EditListener;
import com.rgp.asks.persistence.entity.Belief;

import java.util.List;

public class BeliefRVAdapter extends RecyclerView.Adapter<BeliefRVAdapter.ViewHolder> {
    private List<Belief> beliefs;

    public BeliefRVAdapter() {
    }

    @Override
    public BeliefRVAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                         int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(com.rgp.asks.R.layout.item_recycler_view_belief, parent, false);

        setItemListeners(v);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void setBeliefs(List<Belief> beliefs) {
        this.beliefs = beliefs;
        notifyDataSetChanged();
    }

    public Belief getItem(int position) {
        if (this.beliefs == null) {
            return null;
        } else {
            return beliefs.get(position);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.beliefTextView.setText(beliefs.get(position).getBelief());
    }

    @Override
    public int getItemCount() {
        if (this.beliefs == null) return 0;
        else return this.beliefs.size();
    }

    private void setItemListeners(View v) {
        EditListener editListener = new EditListener();
        editListener.setBeliefs(this.beliefs);
        v.setOnClickListener(editListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView beliefTextView;

        public ViewHolder(View v) {
            super(v);
            beliefTextView = v.findViewById(com.rgp.asks.R.id.beliefTextView);
        }
    }
}
