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
    private final List<Belief> dataList;
    @NonNull
    private final List<Belief> backupBeliefs;
    private final View.OnClickListener onItemClickListener;
    private final String emptyName;

    public BeliefRecyclerViewAdapter(@NonNull String emptyName, @NonNull View.OnClickListener onItemClickListener) {
        this.dataList = new ArrayList<>();
        this.backupBeliefs = new ArrayList<>();
        this.emptyName = emptyName;
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
        this.dataList.clear();
        this.backupBeliefs.clear();
        this.dataList.addAll(beliefs);
        this.backupBeliefs.addAll(beliefs);
        notifyDataSetChanged();
    }

    public Belief getItem(int position) {
        return dataList.get(position);
    }

    private String getNameToDisplay(int position) {
        String name = this.dataList.get(position).getBelief();
        if (name.isEmpty()) {
            name = this.emptyName;
        }
        return name;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.beliefTextView.setText(getNameToDisplay(position));
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
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
                dataList.clear();
                dataList.addAll(filteredList);
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void removeAllAppliedFilter() {
        dataList.clear();
        dataList.addAll(backupBeliefs);
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
