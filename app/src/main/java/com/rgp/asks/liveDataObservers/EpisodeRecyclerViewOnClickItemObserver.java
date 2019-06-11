package com.rgp.asks.liveDataObservers;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.rgp.asks.activities.AsksActivity;
import com.rgp.asks.adapters.EpisodesRecyclerViewAdapter;
import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.persistence.entity.Episode;

import javax.inject.Inject;

import dagger.Lazy;

public class EpisodeRecyclerViewOnClickItemObserver implements Observer<View> {

    @Inject
    Lazy<RecyclerView> lazyEpisodesRecyclerView;

    @Inject
    public EpisodeRecyclerViewOnClickItemObserver() {
    }

    @Override
    public void onChanged(View view) {
        if (view != null) {
            RecyclerView episodesRecyclerView = lazyEpisodesRecyclerView.get();
            int position = episodesRecyclerView.getChildAdapterPosition(view);
            Episode clickedEpisode = getEpisodesRecyclerViewAdapter(episodesRecyclerView).getItem(position);
            startEditEpisodeActivity(view.getContext(), clickedEpisode.getId());
        }
    }

    private EpisodesRecyclerViewAdapter getEpisodesRecyclerViewAdapter(RecyclerView episodesRecyclerView) {
        return (EpisodesRecyclerViewAdapter) episodesRecyclerView.getAdapter();
    }

    private void startEditEpisodeActivity(Context context, int episodeId) {
        Intent intent = new Intent(context, AsksActivity.class);
        intent.putExtra(Constants.ARG_EPISODE, episodeId);
        context.startActivity(intent);
    }
}
