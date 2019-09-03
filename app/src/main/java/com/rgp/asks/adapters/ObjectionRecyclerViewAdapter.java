package com.rgp.asks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rgp.asks.interfaces.Filterable;
import com.rgp.asks.persistence.entity.Objection;

import java.util.ArrayList;
import java.util.List;

public class ObjectionRecyclerViewAdapter extends RecyclerView.Adapter<ObjectionRecyclerViewAdapter.ViewHolder> implements Filterable {
    @NonNull
    private final List<Objection> objections;
    @NonNull
    private final List<Objection> backupObjections;
    private View.OnClickListener onItemClickListener;

    public ObjectionRecyclerViewAdapter(View.OnClickListener onItemClickListener) {
        this.objections = new ArrayList<>();
        this.backupObjections = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ObjectionRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(com.rgp.asks.R.layout.item_recycler_view_objection, parent, false);
        v.setOnClickListener(this.onItemClickListener);
        return new ViewHolder(v);
    }

    public void setObjections(final List<Objection> objections) {
        this.objections.clear();
        this.backupObjections.clear();
        this.objections.addAll(objections);
        this.backupObjections.addAll(objections);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ObjectionRecyclerViewAdapter.ViewHolder holder, int position) {
        Objection objection = this.objections.get(position);
        holder.objectionTextView.setText(objection.getObjection());
    }

    public Objection getItem(int position) {
        return objections.get(position);
    }

    @Override
    public int getItemCount() {
        return objections.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchedTerm = charSequence.toString().toLowerCase();
                List<Objection> filteredList = new ArrayList<>();
                if (searchedTerm.isEmpty()) {
                    filteredList.addAll(backupObjections);
                } else {
                    for (Objection objection : backupObjections) {
                        //search case-insensitive
                        if (objection.getObjection().toLowerCase().contains(searchedTerm)) {
                            filteredList.add(objection);
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
                List<Objection> filteredList = (List<Objection>) filterResults.values;
                objections.clear();
                objections.addAll(filteredList);
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void removeAllAppliedFilter() {
        objections.clear();
        objections.addAll(backupObjections);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView objectionTextView;

        ViewHolder(View v) {
            super(v);
            objectionTextView = v.findViewById(com.rgp.asks.R.id.objectionTextView);
        }
    }


}