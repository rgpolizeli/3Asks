package com.rgp.asks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rgp.asks.interfaces.I_EpisodeCreatorController;
import com.rgp.asks.persistence.Repository;
import com.rgp.asks.persistence.entity.Episode;

import java.util.List;


public class MainViewModel extends AndroidViewModel implements I_EpisodeCreatorController {

    private LiveData<List<Episode>> episodes;
    private Repository repository;

    public MainViewModel(Application application) {
        super(application);
        repository = new Repository(application);
    }

    @Override
    public void createEpisode(@NonNull String newEpisodeName, @NonNull String newEpisodeDate, @NonNull String newEpisodePeriod) {
        this.repository.createEpisode(newEpisodeName, newEpisodeDate, newEpisodePeriod);
    }

    public LiveData<List<Episode>> getAllEpisodes() {
        if (this.episodes == null) {
            this.episodes = this.repository.getAllEpisodes();
        }
        return this.episodes;
    }
}
