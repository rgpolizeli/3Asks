package com.rgp.asks.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rgp.asks.interfaces.Filterable;
import com.rgp.asks.persistence.entity.Argument;

import java.util.ArrayList;
import java.util.List;

public class ArgumentRecyclerViewAdapter extends RecyclerView.Adapter<ArgumentRecyclerViewAdapter.ViewHolder> implements Filterable {

    @NonNull
    private final List<Argument> dataList;
    @NonNull
    private final List<Argument> backupArguments;
    private View.OnClickListener onItemClickListener;
    private String emptyName;

    public ArgumentRecyclerViewAdapter(String emptyName, View.OnClickListener onItemClickListener) {
        this.dataList = new ArrayList<>();
        this.backupArguments = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
        this.emptyName = emptyName;
    }

    @NonNull
    @Override
    public ArgumentRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(com.rgp.asks.R.layout.item_recycler_view_argument, parent, false);
        v.setOnClickListener(this.onItemClickListener);
        return new ViewHolder(v);
    }

    public void setData(final List<Argument> arguments) {
        this.dataList.clear();
        this.backupArguments.clear();
        this.dataList.addAll(arguments);
        this.backupArguments.addAll(arguments);
        notifyDataSetChanged();
    }

    private String getNameToDisplay(int position) {
        String name = this.dataList.get(position).getArgument();
        if (name.isEmpty()) {
            name = this.emptyName;
        }
        return name;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.argumentTextView.setText(getNameToDisplay(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public Argument getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchedTerm = charSequence.toString().toLowerCase();
                List<Argument> filteredList = new ArrayList<>();
                if (searchedTerm.isEmpty()) {
                    filteredList.addAll(backupArguments);
                } else {
                    for (Argument argument : backupArguments) {
                        //search case-insensitive
                        if (argument.getArgument().toLowerCase().contains(searchedTerm)) {
                            filteredList.add(argument);
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
                List<Argument> filteredList = (List<Argument>) filterResults.values;
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
        dataList.addAll(backupArguments);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView argumentTextView;

        ViewHolder(View v) {
            super(v);
            argumentTextView = v.findViewById(com.rgp.asks.R.id.argumentTextView);
        }
    }
}
