package com.rgp.asks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rgp.asks.persistence.Repository;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.persistence.entity.Reaction;

import java.util.List;

public class EpisodeViewModel extends AndroidViewModel {
    private int episodeId;
    private String episodeNameForToolbarTitle;
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

    public String getEpisodeNameForToolbarTitle() {
        return this.episodeNameForToolbarTitle;
    }

    public void setEpisodeNameForToolbarTitle(@NonNull String episodeNameForToolbarTitle) {
        this.episodeNameForToolbarTitle = episodeNameForToolbarTitle;
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

    public void createReaction(@NonNull String newReaction, @NonNull String newReactionClass) {
        this.repository.createReactionForEpisode(this.episodeId, newReaction, newReactionClass);
    }

    public void editReactionForEpisode(@NonNull final Reaction reaction) {
        if (reaction.getEpisodeId() == this.episodeId) {
            this.repository.editReaction(reaction);
        } else {
            //err
        }
    }

    public void removeReactionForEpisode(@NonNull final Reaction reaction) {
        if (reaction.getEpisodeId() == this.episodeId) {
            this.repository.deleteReaction(reaction);
        } else {
            //err
        }
    }

    public void createBeliefForEpisode(@NonNull final String newBelief) {
        this.repository.createBeliefForEpisode(episodeId, newBelief);
    }

    public void uncheckedSaveEpisode() {
        if (episodeWasChanged()) {
            this.repository.saveEpisode(getModifiableEpisodeCopy());
        } else {
            //err
        }
    }

    public void checkedSaveEpisode() {
        this.repository.saveEpisode(getModifiableEpisodeCopy());
    }

    public boolean episodeWasChanged() {

        Episode currentEpisode = getEpisodeFromLiveData();
        Episode modifiedEpisode = getModifiableEpisodeCopy();

        if (currentEpisode != null && modifiedEpisode != null && currentEpisode.getId() == modifiedEpisode.getId()) {
            if (!modifiedEpisode.getEpisode().isEmpty()) {
                return !currentEpisode.equals(modifiedEpisode);
            } else {
                return !(
                        modifiedEpisode.getDate().equals(currentEpisode.getDate()) &&
                                modifiedEpisode.getDescription().equals(currentEpisode.getDescription()) &&
                                modifiedEpisode.getPeriod().equals(currentEpisode.getPeriod())
                );
            }

        } else {
            //err
            return false;
        }
    }

    public void removeEpisode() {
        Episode episode = getEpisodeFromLiveData();
        if (episode != null) {
            this.repository.deleteEpisode(episode);
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
