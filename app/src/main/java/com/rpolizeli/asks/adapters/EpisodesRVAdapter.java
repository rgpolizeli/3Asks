package com.rpolizeli.asks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rpolizeli.asks.listeners.EditListener;
import com.rpolizeli.asks.listeners.OnItemClickListener;
import com.rpolizeli.asks.persistence.entity.Episode;

import java.text.DateFormat;
import java.util.List;

public class EpisodesRVAdapter extends RecyclerView.Adapter<EpisodesRVAdapter.ViewHolder> {
    private @NonNull List<Episode> episodes;
    private @NonNull final OnItemClickListener onClickListItemListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView episodeTextView;
        public TextView episodeDateTextView;
        public TextView episodePeriodTextView;

        public ViewHolder(View v) {
            super(v);
            episodeTextView = v.findViewById(com.rpolizeli.asks.R.id.episodeNameTextView);
            episodeDateTextView = v.findViewById(com.rpolizeli.asks.R.id.episodeDateTextView);
            episodePeriodTextView = v.findViewById(com.rpolizeli.asks.R.id.episodePeriodTextView);
        }
    }

    public EpisodesRVAdapter(@NonNull final List<Episode> episodes, @NonNull final OnItemClickListener onItemClickListener) {
        this.episodes = episodes;
        this.onClickListItemListener = onItemClickListener;
    }

    public void setEpisodes(@NonNull final List<Episode> episodes) {
        this.episodes = episodes;
        notifyDataSetChanged();
    }


    @Override
    public EpisodesRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(com.rpolizeli.asks.R.layout.item_recycler_view_episode, parent, false);

        setItemListeners(v);

        return new ViewHolder(v);
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

    private void setItemListeners(View v){
        EditListener editListener = new EditListener();
        editListener.setEpisodes(this.episodes);
        v.setOnClickListener(editListener);
    }
}
