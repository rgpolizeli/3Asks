package com.rgp.asks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rgp.asks.interfaces.OnInsertedEntityListener;
import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.persistence.repositories.EpisodeRepository;

import java.util.List;


public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Episode>> episodes;
    private EpisodeRepository episodeRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.episodeRepository = new EpisodeRepository(application);
    }

    public void insertEpisode(@NonNull Episode entity, @NonNull OnInsertedEntityListener onInsertedEntityListener) {
        this.episodeRepository.insertEntity(entity, onInsertedEntityListener);
    }

    public LiveData<List<Episode>> getAllEpisodes() {
        if (this.episodes == null) {
            this.episodes = this.episodeRepository.getAllEpisodes();
        }
        return this.episodes;
    }
}
