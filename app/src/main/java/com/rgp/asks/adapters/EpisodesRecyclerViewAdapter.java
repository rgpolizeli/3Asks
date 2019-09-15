package com.rgp.asks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rgp.asks.interfaces.Filterable;
import com.rgp.asks.persistence.entity.Episode;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class EpisodesRecyclerViewAdapter extends RecyclerView.Adapter<EpisodesRecyclerViewAdapter.ViewHolder> implements Filterable {
    @NonNull
    private final List<Episode> dataList;
    @NonNull
    private final List<Episode> backupEpisodes;
    private final View.OnClickListener onItemClickListener;
    private final String emptyName;

    public EpisodesRecyclerViewAdapter(@NonNull String emptyName, @NonNull View.OnClickListener onItemClickListener) {
        this.dataList = new ArrayList<>();
        this.backupEpisodes = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
        this.emptyName = emptyName;
    }

    @NonNull
    @Override
    public EpisodesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(com.rgp.asks.R.layout.item_recycler_view_episode, parent, false);
        v.setOnClickListener(this.onItemClickListener);
        return new ViewHolder(v);
    }

    public void setData(@NonNull final List<Episode> episodes) {
        this.dataList.clear();
        this.backupEpisodes.clear();
        this.dataList.addAll(episodes);
        this.backupEpisodes.addAll(episodes);
        notifyDataSetChanged();
    }

    private String getNameToDisplay(int position) {
        String name = this.dataList.get(position).getEpisode();
        if (name.isEmpty()) {
            name = this.emptyName;
        }
        return name;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.episodeTextView.setText(getNameToDisplay(position));
        holder.episodeDateTextView.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(this.dataList.get(position).getDate()));
        holder.episodePeriodTextView.setText(this.dataList.get(position).getPeriod());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public Episode getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchedTerm = charSequence.toString().toLowerCase();
                List<Episode> filteredEpisodes = new ArrayList<>();
                if (searchedTerm.isEmpty()) {
                    filteredEpisodes.addAll(backupEpisodes);
                } else {
                    for (Episode episode : backupEpisodes) {
                        //search case-insensitive
                        if (episode.getEpisode().toLowerCase().contains(searchedTerm)) {
                            filteredEpisodes.add(episode);
                        } else if (episode.getDescription().toLowerCase().contains(searchedTerm)) {
                            filteredEpisodes.add(episode);
                        } else if (episode.getPeriod().toLowerCase().contains(searchedTerm)) {
                            filteredEpisodes.add(episode);
                        } else if (DateFormat.getDateInstance(DateFormat.SHORT).format(episode.getDate()).toLowerCase().contains(searchedTerm)) {
                            filteredEpisodes.add(episode);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.count = filteredEpisodes.size();
                filterResults.values = filteredEpisodes;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                List<Episode> filteredEpisodes = (List<Episode>) filterResults.values;
                //this if is necessary because when the filtered results is empty, the filtered results received is null, even the filtered list maked in performFiltering is not null, only empty. See the issue #35.
                if (filteredEpisodes == null) {
                    filteredEpisodes = new ArrayList<>();
                }
                dataList.clear();
                dataList.addAll(filteredEpisodes);
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void removeAllAppliedFilter() {
        dataList.clear();
        dataList.addAll(backupEpisodes);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView episodeTextView;
        TextView episodeDateTextView;
        TextView episodePeriodTextView;

        ViewHolder(View v) {
            super(v);
            episodeTextView = v.findViewById(com.rgp.asks.R.id.episodeNameTextView);
            episodeDateTextView = v.findViewById(com.rgp.asks.R.id.episodeDateTextView);
            episodePeriodTextView = v.findViewById(com.rgp.asks.R.id.episodePeriodTextView);
        }
    }
}
