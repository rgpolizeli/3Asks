package com.rgp.asks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rgp.asks.interfaces.OnDeletedEntityListener;
import com.rgp.asks.interfaces.OnUpdatedEntityListener;
import com.rgp.asks.persistence.Repository;
import com.rgp.asks.persistence.entity.Reaction;

public class ReactionViewModel extends AndroidViewModel {
    private int reactionId;
    private LiveData<Reaction> reaction;
    private boolean isReactionInFirstLoad;
    private Reaction modifiableReactionCopy;
    private Repository repository;

    public ReactionViewModel(Application application) {
        super(application);
        this.repository = new Repository(application);
        this.isReactionInFirstLoad = true;
    }

    public boolean isInFirstLoad() {
        return isReactionInFirstLoad;
    }

    public void setIsInFirstLoad(boolean is) {
        this.isReactionInFirstLoad = is;
    }

    public int getId() {
        return this.reactionId;
    }

    public void setId(int id) {
        this.reactionId = id;
    }

    public LiveData<Reaction> getLiveData() {
        if (this.reaction == null) {
            loadReactionById();
        }
        return this.reaction;
    }

    private void loadReactionById() {
        this.reaction = this.repository.getReactionById(this.reactionId);
    }

    public void delete(OnDeletedEntityListener onDeletedEntityListener) {
        Reaction reaction = getReactionFromLiveData();
        if (reaction != null) {
            this.repository.deleteReaction(reaction, onDeletedEntityListener);
        } else {
            //todo: err
        }
    }

    public void update(boolean finishSignal, OnUpdatedEntityListener onUpdatedEntityListener) {
        this.repository.editReaction(getModifiableCopy(), finishSignal, onUpdatedEntityListener);
    }

    public boolean wasChanged() {

        Reaction currentReaction = getReactionFromLiveData();
        Reaction modifiedReaction = getModifiableCopy();

        if (currentReaction != null && modifiedReaction != null && currentReaction.getId() == modifiedReaction.getId()) {
            return !currentReaction.equals(modifiedReaction);
        } else {
            //err
            return false;
        }
    }

    @Nullable
    public Reaction getModifiableCopy() {
        return modifiableReactionCopy;
    }

    public void initModifiableCopy(@NonNull Reaction loadedReaction) {
        if (loadedReaction.getId() == this.reactionId) {
            this.modifiableReactionCopy = new Reaction(
                    loadedReaction.getReaction(),
                    loadedReaction.getReactionCategory(),
                    loadedReaction.getEpisodeId()
            );
            this.modifiableReactionCopy.setId(loadedReaction.getId());
        } else {
            //todo: err
        }
    }

    @Nullable
    private Reaction getReactionFromLiveData() {
        return this.reaction.getValue();
    }

}
