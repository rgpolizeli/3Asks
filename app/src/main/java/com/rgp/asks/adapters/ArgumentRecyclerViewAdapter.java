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
    private final List<Argument> arguments;
    @NonNull
    private final List<Argument> backupArguments;
    private View.OnClickListener onItemClickListener;

    public ArgumentRecyclerViewAdapter(View.OnClickListener onItemClickListener) {
        this.arguments = new ArrayList<>();
        this.backupArguments = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ArgumentRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(com.rgp.asks.R.layout.item_recycler_view_argument, parent, false);
        v.setOnClickListener(this.onItemClickListener);
        return new ViewHolder(v);
    }

    public void setArguments(final List<Argument> arguments) {
        this.arguments.clear();
        this.backupArguments.clear();
        this.arguments.addAll(arguments);
        this.backupArguments.addAll(arguments);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Argument argument = this.arguments.get(position);
        holder.argumentTextView.setText(argument.getArgument());
    }

    @Override
    public int getItemCount() {
        return arguments.size();
    }

    public Argument getItem(int position) {
        return arguments.get(position);
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
                arguments.clear();
                arguments.addAll(filteredList);
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void removeAllAppliedFilter() {
        arguments.clear();
        arguments.addAll(backupArguments);
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
