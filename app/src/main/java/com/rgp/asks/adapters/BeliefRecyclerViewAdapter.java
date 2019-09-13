package com.rgp.asks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rgp.asks.interfaces.Filterable;
import com.rgp.asks.persistence.entity.Belief;

import java.util.ArrayList;
import java.util.List;

public class BeliefRecyclerViewAdapter extends RecyclerView.Adapter<BeliefRecyclerViewAdapter.ViewHolder> implements Filterable {
    @NonNull
    private final List<Belief> beliefs;
    @NonNull
    private final List<Belief> backupBeliefs;
    private final View.OnClickListener onItemClickListener;
    private final String emptyThought;

    public BeliefRecyclerViewAdapter(@NonNull String emptyThought, @NonNull View.OnClickListener onItemClickListener) {
        this.beliefs = new ArrayList<>();
        this.backupBeliefs = new ArrayList<>();
        this.emptyThought = emptyThought;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BeliefRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(com.rgp.asks.R.layout.item_recycler_view_belief, parent, false);
        v.setOnClickListener(this.onItemClickListener);
        return new ViewHolder(v);
    }

    public void setData(final List<Belief> beliefs) {
        this.beliefs.clear();
        this.backupBeliefs.clear();
        this.beliefs.addAll(beliefs);
        this.backupBeliefs.addAll(beliefs);
        notifyDataSetChanged();
    }

    public Belief getItem(int position) {
        return beliefs.get(position);
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
        return this.beliefs.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchedTerm = charSequence.toString().toLowerCase();
                List<Belief> filteredList = new ArrayList<>();
                if (searchedTerm.isEmpty()) {
                    filteredList.addAll(backupBeliefs);
                } else {
                    for (Belief belief : backupBeliefs) {
                        //search case-insensitive
                        if (belief.getBelief().toLowerCase().contains(searchedTerm)) {
                            filteredList.add(belief);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.count = filteredList.size();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                List<Belief> filteredList = (List<Belief>) filterResults.values;
                //this if is necessary because when the filtered results is empty, the filtered results received is null, even the filtered list maked in performFiltering is not null, only empty. See the issue #35.
                if (filteredList == null) {
                    filteredList = new ArrayList<>();
                }
                beliefs.clear();
                beliefs.addAll(filteredList);
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void removeAllAppliedFilter() {
        beliefs.clear();
        beliefs.addAll(backupBeliefs);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView beliefTextView;

        ViewHolder(View v) {
            super(v);
            beliefTextView = v.findViewById(com.rgp.asks.R.id.beliefTextView);
        }
    }
}
