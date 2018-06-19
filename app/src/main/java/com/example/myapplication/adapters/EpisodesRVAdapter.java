package com.example.myapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.listeners.EditListener;
import com.example.myapplication.persistence.entity.Episode;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class EpisodesRVAdapter extends RecyclerView.Adapter<EpisodesRVAdapter.ViewHolder> {
    private List<Episode> episodes;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView episodeTextView;
        public TextView episodeDateTextView;
        public TextView episodePeriodTextView;


        public ViewHolder(View v) {
            super(v);
            episodeTextView = (TextView) v.findViewById(R.id.episodeNameTextView);
            episodeDateTextView = (TextView) v.findViewById(R.id.episodeDateTextView);
            episodePeriodTextView = (TextView) v.findViewById(R.id.episodePeriodTextView);
        }
    }

    public EpisodesRVAdapter() {
        episodes = new ArrayList<>();
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
        notifyDataSetChanged();
    }


    @Override
    public EpisodesRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view_episode, parent, false);

        setItemListeners(v);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.episodeTextView.setText(this.episodes.get(position).getEpisode());
        holder.episodeDateTextView.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(this.episodes.get(position).getDate()));
        holder.episodePeriodTextView.setText(this.episodes.get(position).getPeriod());
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public Episode getItem(int position) {
        if (this.episodes == null){
            return null;
        } else{
            return episodes.get(position);
        }
    }

    private void setItemListeners(View v){
        EditListener editListener = new EditListener();
        editListener.setEpisodes(this.episodes);
        v.setOnClickListener(editListener);
    }
}
