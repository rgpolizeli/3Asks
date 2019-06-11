package com.rgp.asks.dagger;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rgp.asks.R;
import com.rgp.asks.activities.MainActivity;
import com.rgp.asks.adapters.EpisodesRecyclerViewAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class RecyclerViewModule {

    @Provides
    RecyclerView provideEpisodesRecyclerView(@NonNull MainActivity mainActivity, @NonNull RecyclerView.LayoutManager layoutManager, @NonNull EpisodesRecyclerViewAdapter episodesRecyclerViewAdapter) {
        RecyclerView episodesRecyclerView = mainActivity.findViewById(R.id.episodesRecyclerView);
        episodesRecyclerView.setLayoutManager(layoutManager);
        episodesRecyclerView.setAdapter(episodesRecyclerViewAdapter);
        return episodesRecyclerView;
    }

    @Provides
    RecyclerView.LayoutManager provideLinearLayoutManager(MainActivity mainActivity) {
        return new LinearLayoutManager(mainActivity);
    }

}
