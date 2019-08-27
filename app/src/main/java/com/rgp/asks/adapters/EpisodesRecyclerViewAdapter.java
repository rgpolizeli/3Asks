package com.rgp.asks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rgp.asks.persistence.entity.Episode;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class EpisodesRecyclerViewAdapter extends RecyclerView.Adapter<EpisodesRecyclerViewAdapter.ViewHolder> {
    @NonNull
    private List<Episode> episodes;
    private final View.OnClickListener onItemClickListener;
    private final String emptyEpisodeName;

    public EpisodesRecyclerViewAdapter(@NonNull String emptyEpisodeName, @NonNull View.OnClickListener onItemClickListener) {
        this.episodes = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
        this.emptyEpisodeName = emptyEpisodeName;
    }

    @NonNull
    @Override
    public EpisodesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(com.rgp.asks.R.layout.item_recycler_view_episode, parent, false);
        v.setOnClickListener(this.onItemClickListener);
        return new ViewHolder(v);
    }

    public void setEpisodes(@NonNull final List<Episode> episodes) {
        this.episodes = episodes;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String episodeName = this.episodes.get(position).getEpisode();
        if (episodeName.isEmpty()) {
            episodeName = this.emptyEpisodeName;
        }
        holder.episodeTextView.setText(episodeName);
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
