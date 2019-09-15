package com.rgp.asks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rgp.asks.interfaces.OnDeletedEntityListener;
import com.rgp.asks.interfaces.OnInsertedEntityListener;
import com.rgp.asks.interfaces.OnUpdatedEntityListener;
import com.rgp.asks.persistence.Repository;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.persistence.entity.Reaction;

import java.util.List;

public class EpisodeViewModel extends AndroidViewModel {
    private int episodeId;
    private LiveData<Episode> episode;
    private boolean isEpisodeInFirstLoad;
    private Episode modifiableEpisodeCopy;
    private LiveData<List<Reaction>> reactions;
    private LiveData<List<Belief>> beliefs;

    private Repository repository;

    public EpisodeViewModel(Application application) {
        super(application);
        this.repository = new Repository(application);
        this.isEpisodeInFirstLoad = true;
    }

    public boolean isEpisodeInFirstLoad() {
        return isEpisodeInFirstLoad;
    }

    public void setIsEpisodeInFirstLoad(boolean is) {
        this.isEpisodeInFirstLoad = is;
    }

    public int getEpisodeId() {
        return this.episodeId;
    }

    public void setEpisodeId(int episodeId) {
        this.episodeId = episodeId;
    }

    public LiveData<Episode> getEpisodeById() {
        if (this.episode == null) {
            loadEpisodeById();
        }
        return this.episode;
    }

    private void loadEpisodeById() {
        this.episode = this.repository.getEpisodeById(this.episodeId);
    }

    public LiveData<List<Reaction>> getReactionsForEpisode() {
        if (this.reactions == null) {
            loadReactionsForEpisode();
        }
        return reactions;
    }

    private void loadReactionsForEpisode() {
        this.reactions = this.repository.getReactionsForEpisode(this.episodeId);
    }

    public LiveData<List<Belief>> getBeliefsForEpisode() {
        if (this.beliefs == null) {
            loadBeliefsForEpisode();
        }
        return beliefs;
    }

    private void loadBeliefsForEpisode() {
        this.beliefs = this.repository.getBeliefsForEpisode(this.episodeId);
    }

    public void createReaction(@NonNull String newReaction, @NonNull String newReactionClass, @NonNull OnInsertedEntityListener onInsertedEntityListener) {
        this.repository.createReaction(this.episodeId, newReaction, newReactionClass, onInsertedEntityListener);
    }

    public void createBelief(@NonNull final String newBelief, OnInsertedEntityListener onInsertedEntityListener) {
        this.repository.createBelief(episodeId, newBelief, onInsertedEntityListener);
    }

    public void uncheckedSaveEpisode(OnUpdatedEntityListener onUpdatedEntityListener) {
        if (episodeWasChanged()) {
            saveEpisode(false, onUpdatedEntityListener);
        } else {
            //todo: err
        }
    }

    public void checkedSaveEpisode(OnUpdatedEntityListener onUpdatedEntityListener) {
        saveEpisode(true, onUpdatedEntityListener);
    }

    private void saveEpisode(boolean finishSignal, OnUpdatedEntityListener onUpdatedEntityListener) {
        Episode newEpisode = getModifiableEpisodeCopy();
        if (newEpisode != null) {
            this.repository.saveEpisode(newEpisode, finishSignal, onUpdatedEntityListener);
        } else {
            //todo: err
        }
    }

    public boolean episodeWasChanged() {
        Episode currentEpisode = getEpisodeFromLiveData();
        Episode modifiedEpisode = getModifiableEpisodeCopy();

        if (currentEpisode != null && modifiedEpisode != null && currentEpisode.getId() == modifiedEpisode.getId()) {
            return !currentEpisode.equals(modifiedEpisode);
        } else {
            //err
            return false;
        }
    }

    public void removeEpisode(OnDeletedEntityListener onDeletedEntityListener) {
        Episode episode = getEpisodeFromLiveData();
        if (episode != null) {
            this.repository.deleteEpisode(episode, onDeletedEntityListener);
        } else {
            //todo: err
        }
    }

    @Nullable
    public Episode getModifiableEpisodeCopy() {
        return modifiableEpisodeCopy;
    }

    public void initModifiableEpisodeCopy(@NonNull Episode loadedEpisode) {
        if (loadedEpisode.getId() == this.episodeId) {
            this.modifiableEpisodeCopy = new Episode(
                    loadedEpisode.getEpisode(),
                    loadedEpisode.getDescription(),
                    loadedEpisode.getDate(),
                    loadedEpisode.getPeriod()
            );
            this.modifiableEpisodeCopy.setId(loadedEpisode.getId());
        } else {
            //todo: err
        }
    }

    @Nullable
    private Episode getEpisodeFromLiveData() {
        return this.episode.getValue();
    }

}
