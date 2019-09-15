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
    private final List<Objection> dataList;
    @NonNull
    private final List<Objection> backupObjections;
    private View.OnClickListener onItemClickListener;
    private String emptyName;

    public ObjectionRecyclerViewAdapter(String emptyName, View.OnClickListener onItemClickListener) {
        this.dataList = new ArrayList<>();
        this.backupObjections = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
        this.emptyName = emptyName;
    }

    @NonNull
    @Override
    public ObjectionRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(com.rgp.asks.R.layout.item_recycler_view_objection, parent, false);
        v.setOnClickListener(this.onItemClickListener);
        return new ViewHolder(v);
    }

    public void setData(final List<Objection> objections) {
        this.dataList.clear();
        this.backupObjections.clear();
        this.dataList.addAll(objections);
        this.backupObjections.addAll(objections);
        notifyDataSetChanged();
    }

    private String getNameToDisplay(int position) {
        String name = this.dataList.get(position).getObjection();
        if (name.isEmpty()) {
            name = this.emptyName;
        }
        return name;
    }

    @Override
    public void onBindViewHolder(@NonNull ObjectionRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.objectionTextView.setText(getNameToDisplay(position));
    }

    public Objection getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
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
        dataList.addAll(backupObjections);
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