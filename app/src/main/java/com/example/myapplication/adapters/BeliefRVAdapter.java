package com.example.myapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.listeners.EditListener;
import com.example.myapplication.persistence.entity.Belief;

import java.util.List;

public class BeliefRVAdapter extends RecyclerView.Adapter<BeliefRVAdapter.ViewHolder> {
    private List<Belief> beliefs;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView beliefTextView;
        public ViewHolder(View v) {
            super(v);
            beliefTextView = v.findViewById(R.id.beliefTextView);
        }
    }

    public BeliefRVAdapter() {
    }

    public void setBeliefs(List<Belief> beliefs) {
        this.beliefs = beliefs;
        notifyDataSetChanged();
    }

    public Belief getItem(int position) {
        if (this.beliefs == null){
            return null;
        } else{
            return beliefs.get(position);
        }
    }


    @Override
    public BeliefRVAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                         int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view_belief, parent, false);

        setItemListeners(v);

        ViewHolder vh = new ViewHolder(v);
        return vh;
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

    private void setItemListeners(View v){
        EditListener editListener = new EditListener();
        editListener.setBeliefs(this.beliefs);
        v.setOnClickListener(editListener);
    }
}
