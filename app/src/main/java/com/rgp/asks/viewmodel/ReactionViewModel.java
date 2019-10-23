package com.rgp.asks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.rgp.asks.interfaces.OnDeletedEntityListener;
import com.rgp.asks.interfaces.OnUpdatedEntityListener;
import com.rgp.asks.persistence.entity.Reaction;
import com.rgp.asks.persistence.repositories.ReactionRepository;

public class ReactionViewModel extends EntityViewModel<Reaction> {
    private ReactionRepository reactionRepository;

    public ReactionViewModel(@NonNull Application application) {
        super(application);
        this.reactionRepository = new ReactionRepository(application);
    }

    public LiveData<Reaction> getReaction() {
        return super.getEntityById(this.reactionRepository);
    }

    public void updateReaction(boolean finishSignal, OnUpdatedEntityListener onUpdatedEntityListener) {
        super.updateEntity(this.reactionRepository, finishSignal, onUpdatedEntityListener);
    }

    public void deleteReaction(OnDeletedEntityListener onDeletedEntityListener) {
        super.deleteEntity(this.reactionRepository, onDeletedEntityListener);
    }

}
