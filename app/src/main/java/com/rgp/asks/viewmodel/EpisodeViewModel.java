package com.rgp.asks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.rgp.asks.interfaces.OnDeletedEntityListener;
import com.rgp.asks.interfaces.OnInsertedEntityListener;
import com.rgp.asks.interfaces.OnUpdatedEntityListener;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.persistence.entity.Reaction;
import com.rgp.asks.persistence.repositories.BeliefRepository;
import com.rgp.asks.persistence.repositories.EpisodeRepository;
import com.rgp.asks.persistence.repositories.ReactionRepository;

import java.util.List;

public class EpisodeViewModel extends EntityViewModel<Episode> {
    private LiveData<List<Reaction>> reactions;
    private LiveData<List<Belief>> beliefs;
    private EpisodeRepository episodeRepository;
    private ReactionRepository reactionRepository;
    private BeliefRepository beliefRepository;

    public EpisodeViewModel(@NonNull Application application) {
        super(application);
        this.episodeRepository = new EpisodeRepository(application);
        this.reactionRepository = new ReactionRepository(application);
        this.beliefRepository = new BeliefRepository(application);
    }

    public LiveData<Episode> getEpisode() {
        return super.getEntityById(this.episodeRepository);
    }

    public void updateEpisode(boolean finishSignal, OnUpdatedEntityListener onUpdatedEntityListener) {
        super.updateEntity(this.episodeRepository, finishSignal, onUpdatedEntityListener);
    }

    public void deleteEpisode(OnDeletedEntityListener onDeletedEntityListener) {
        super.deleteEntity(this.episodeRepository, onDeletedEntityListener);
    }

    public LiveData<List<Reaction>> getReactionsForEpisode() {
        if (this.reactions == null) {
            this.reactions = this.reactionRepository.getReactionsForEpisode(super.getEntityId());
        }
        return reactions;
    }

    public LiveData<List<Belief>> getBeliefsForEpisode() {
        if (this.beliefs == null) {
            this.beliefs = this.beliefRepository.getBeliefsForEpisode(super.getEntityId());
        }
        return beliefs;
    }

    public void insertReaction(@NonNull Reaction newReaction, @NonNull OnInsertedEntityListener onInsertedEntityListener) {
        this.reactionRepository.insertEntity(newReaction, onInsertedEntityListener);
    }

    public void insertBelief(@NonNull Belief newBelief, OnInsertedEntityListener onInsertedEntityListener) {
        this.beliefRepository.insertEntity(newBelief, onInsertedEntityListener);
    }
}
