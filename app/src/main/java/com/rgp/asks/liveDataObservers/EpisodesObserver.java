package com.rgp.asks.liveDataObservers;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.rgp.asks.activities.AsksActivity;
import com.rgp.asks.adapters.EpisodesRecyclerViewAdapter;
import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.persistence.entity.Episode;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

public class EpisodesObserver implements Observer<List<Episode>> {
    @Inject
    Lazy<RecyclerView> lazyEpisodesRecyclerView;

    @Inject
    public EpisodesObserver() {
    }

    @Override
    public void onChanged(List<Episode> episodes) {
        getEpisodesRecyclerViewAdapter(lazyEpisodesRecyclerView.get()).setEpisodes(episodes);
        //showViews();
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
