package com.example.myapplication.adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.listeners.EditListener;
import com.example.myapplication.persistence.entity.Episode;

import java.util.ArrayList;
import java.util.List;

public class EpisodesRVAdapter extends RecyclerView.Adapter<EpisodesRVAdapter.ViewHolder> {
    private List<Episode> episodes;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.episodeNameTextView);
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_text_view, parent, false);

        setItemListeners(v);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(this.episodes.get(position).getEpisode());


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
        //ConstraintLayout cl = (ConstraintLayout) v.findViewById(R.id.episodeConstraintLayout);
        EditListener editListener = new EditListener();
        editListener.setEpisodes(this.episodes);
        //cl.setOnClickListener(editListener);
        v.setOnClickListener(editListener);
    }
}
