package com.rgp.asks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rgp.asks.persistence.entity.Belief;

import java.util.List;

public class BeliefRVAdapter extends RecyclerView.Adapter<BeliefRVAdapter.ViewHolder> {
    private List<Belief> beliefs;
    private final View.OnClickListener onItemClickListener;
    private final String emptyThought;

    public BeliefRVAdapter(@NonNull String emptyThought, @NonNull View.OnClickListener onItemClickListener) {
        this.emptyThought = emptyThought;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BeliefRVAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(com.rgp.asks.R.layout.item_recycler_view_belief, parent, false);
        v.setOnClickListener(this.onItemClickListener);
        return new ViewHolder(v);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String thought = beliefs.get(position).getBelief();
        if (thought.isEmpty()) {
            thought = this.emptyThought;
        }
        holder.beliefTextView.setText(thought);
    }

    @Override
    public int getItemCount() {
        if (this.beliefs == null) return 0;
        else return this.beliefs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView beliefTextView;

        ViewHolder(View v) {
            super(v);
            beliefTextView = v.findViewById(com.rgp.asks.R.id.beliefTextView);
        }
    }
}
