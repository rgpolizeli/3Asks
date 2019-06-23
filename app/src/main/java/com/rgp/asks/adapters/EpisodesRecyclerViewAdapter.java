package com.rgp.asks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rgp.asks.listeners.EditListener;
import com.rgp.asks.persistence.entity.Episode;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class EpisodesRecyclerViewAdapter extends RecyclerView.Adapter<EpisodesRecyclerViewAdapter.ViewHolder> {
    @NonNull
    private List<Episode> episodes;

    public EpisodesRecyclerViewAdapter() {
        this.episodes = new ArrayList<>();
    }

    @Override
    public EpisodesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(com.rgp.asks.R.layout.item_recycler_view_episode, parent, false);

        setItemListeners(v);

        return new ViewHolder(v);
    }

    public void setEpisodes(@NonNull final List<Episode> episodes) {
        this.episodes = episodes;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.episodeTextView.setText(this.episodes.get(position).getEpisode());
        holder.episodeDateTextView.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(this.episodes.get(position).getDate()));
        holder.episodePeriodTextView.setText(this.episodes.get(position).getPeriod());
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public Episode getItem(int position) {
        return episodes.get(position);
    }

    private void setItemListeners(View v) {
        EditListener editListener = new EditListener();
        editListener.setEpisodes(this.episodes);
        v.setOnClickListener(editListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView episodeTextView;
        public TextView episodeDateTextView;
        public TextView episodePeriodTextView;

        public ViewHolder(View v) {
            super(v);
            episodeTextView = v.findViewById(com.rgp.asks.R.id.episodeNameTextView);
            episodeDateTextView = v.findViewById(com.rgp.asks.R.id.episodeDateTextView);
            episodePeriodTextView = v.findViewById(com.rgp.asks.R.id.episodePeriodTextView);
        }
    }
}
