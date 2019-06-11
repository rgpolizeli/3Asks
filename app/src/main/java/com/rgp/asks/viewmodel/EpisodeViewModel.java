package com.rgp.asks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rgp.asks.persistence.Repository;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.persistence.entity.Reaction;

import java.util.List;

public class EpisodeViewModel extends AndroidViewModel {
    private LiveData<Episode> episode;
    private Episode modifiableEpisodeCopy;
    private LiveData<List<Reaction>> reactions;
    private LiveData<List<Belief>> beliefs;

    private boolean episodeIsLoaded;

    private Repository repository;

    public EpisodeViewModel(Application application) {
        super(application);
        this.repository = new Repository(application);
        this.episodeIsLoaded = false;
    }

    public boolean getEpisodeIsLoaded() {
        return this.episodeIsLoaded;
    }

    public void setEpisodeIsLoaded(boolean value) {
        episodeIsLoaded = true;
    }

    public void loadEpisode(@NonNull final int episodeId) {
        this.episode = this.repository.getEpisodeById(episodeId);
        this.reactions = this.repository.getReactionsForEpisode(episodeId);
        this.beliefs = this.repository.getBeliefsForEpisode(episodeId);
    }

    public LiveData<Episode> getEpisode() {
        return episode;
    }

    public LiveData<List<Reaction>> getReactions() {
        return reactions;
    }

    public LiveData<List<Belief>> getBeliefs() {
        return beliefs;
    }

    public Episode getModifiableEpisodeCopy() {
        return modifiableEpisodeCopy;
    }

    public void setModifiableEpisodeCopy(@NonNull Episode newEpisode) {

        if (this.modifiableEpisodeCopy != null && this.modifiableEpisodeCopy.getId() == newEpisode.getId()) {
            this.modifiableEpisodeCopy = newEpisode;
        } else {
            //not permit
        }

    }

    public void initModifiableEpisodeCopy() {
        Episode currentEpisode = this.episode.getValue();

        if (currentEpisode != null) {
            this.modifiableEpisodeCopy = new Episode(
                    currentEpisode.getEpisode(),
                    currentEpisode.getDescription(),
                    currentEpisode.getDate(),
                    currentEpisode.getPeriod()
            );
            this.modifiableEpisodeCopy.setId(currentEpisode.getId());
        } else {
            //err
        }


    }

    public void createReaction(@NonNull String newReaction, @NonNull String newReactionClass) {
        Episode e = this.episode.getValue();
        if (e != null) {
            this.repository.createReactionForEpisode(e.getId(), newReaction, newReactionClass);
        }
    }

    public void editReaction(@NonNull final Reaction reaction) {
        Episode e = this.episode.getValue();
        if (e != null && e.getId() == reaction.getEpisodeId()) {
            this.repository.editReactionForEpisode(reaction);
        } else {
            //err
        }
    }

    public void removeReaction(@NonNull final Reaction reaction) {
        Episode e = this.episode.getValue();
        if (e != null && e.getId() == reaction.getEpisodeId()) {
            this.repository.deleteReactionForEpisode(reaction);
        } else {
            //err
        }
    }

    public void createBelief(@NonNull final String newBelief) {
        Episode e = this.episode.getValue();
        if (e != null) {
            this.repository.createBeliefForEpisode(e.getId(), newBelief);
        }
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
        Episode currentEpisode = this.getEpisode().getValue();
        Episode modifiedEpisode = this.getModifiableEpisodeCopy();

        if (currentEpisode != null && currentEpisode.getId() == modifiedEpisode.getId()) {
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
        Episode e = this.getEpisode().getValue();
        if (e != null) {
            this.repository.deleteEpisode(e);
        } else {
            //err
        }
    }

}
