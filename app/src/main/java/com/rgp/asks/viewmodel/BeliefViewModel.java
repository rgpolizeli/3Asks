package com.rgp.asks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.rgp.asks.interfaces.OnDeletedEntityListener;
import com.rgp.asks.interfaces.OnInsertedEntityListener;
import com.rgp.asks.interfaces.OnUpdatedEntityListener;
import com.rgp.asks.persistence.entity.Argument;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.persistence.entity.Objection;
import com.rgp.asks.persistence.entity.ThinkingStyle;
import com.rgp.asks.persistence.repositories.ArgumentRepository;
import com.rgp.asks.persistence.repositories.BeliefRepository;
import com.rgp.asks.persistence.repositories.BeliefThinkingStyleRepository;
import com.rgp.asks.persistence.repositories.ObjectionRepository;

import java.util.ArrayList;
import java.util.List;

public class BeliefViewModel extends EntityViewModel<Belief> {
    private LiveData<List<ThinkingStyle>> thinkingStylesLiveData;
    private boolean isThinkingStylesFirstLoad;
    private List<ThinkingStyle> modifiableThinkingStylesCopy;
    private LiveData<List<Argument>> argumentsLiveData;
    private LiveData<List<Objection>> objectionsLiveData;
    private BeliefRepository beliefRepository;
    private BeliefThinkingStyleRepository beliefThinkingStyleRepository;
    private ArgumentRepository argumentRepository;
    private ObjectionRepository objectionRepository;

    public BeliefViewModel(@NonNull Application application) {
        super(application);
        this.beliefRepository = new BeliefRepository(application);
        this.beliefThinkingStyleRepository = new BeliefThinkingStyleRepository(application);
        this.argumentRepository = new ArgumentRepository(application);
        this.objectionRepository = new ObjectionRepository(application);
        this.isThinkingStylesFirstLoad = true;
    }

    public boolean isThinkingStylesFirstLoad() {
        return this.isThinkingStylesFirstLoad;
    }

    public void setIsThinkingStylesFirstLoad(boolean is) {
        this.isThinkingStylesFirstLoad = is;
    }

    public LiveData<Belief> getBelief() {
        return super.getEntityById(this.beliefRepository);
    }

    public LiveData<List<ThinkingStyle>> getThinkingStylesLiveData() {
        if (this.thinkingStylesLiveData == null) {
            loadThinkingStyles();
        }
        return this.thinkingStylesLiveData;
    }

    private void loadThinkingStyles() {
        this.thinkingStylesLiveData = this.beliefThinkingStyleRepository.getThinkingStylesForBelief(super.getEntityId());
    }

    public LiveData<List<Argument>> getArgumentsLiveData() {
        if (this.argumentsLiveData == null) {
            this.argumentsLiveData = this.argumentRepository.getArgumentsForBelief(super.getEntityId());
        }
        return argumentsLiveData;
    }

    public LiveData<List<Objection>> getObjectionsLiveData() {
        if (this.objectionsLiveData == null) {
            this.objectionsLiveData = this.objectionRepository.getObjectionsForBelief(super.getEntityId());
        }
        return objectionsLiveData;
    }

    /**
     * Creates a new list with the thinking styles received.
     *
     * @param thinkingStyles of the belief.
     */
    public void initModifiableSelectedThinkingStylesCopy(@NonNull List<ThinkingStyle> thinkingStyles) {
        this.modifiableThinkingStylesCopy = new ArrayList<>(thinkingStyles);
    }

    private List<ThinkingStyle> getModifiableThinkingStylesCopy() {
        return modifiableThinkingStylesCopy;
    }

    public void addUnhelpfulThinkingStyle(@NonNull final ThinkingStyle unhelpfulThinkingStyle) {
        this.getModifiableThinkingStylesCopy().add(unhelpfulThinkingStyle);
    }

    public void removeUnhelpfulThinkingStyle(@NonNull final ThinkingStyle unhelpfulThinkingStyle) {
        this.getModifiableThinkingStylesCopy().remove(unhelpfulThinkingStyle);
    }

    public void insertArgument(@NonNull final Argument newArgument, OnInsertedEntityListener onInsertedEntityListener) {
        this.argumentRepository.insertEntity(newArgument, onInsertedEntityListener);
    }

    public void insertObjection(@NonNull final Objection newObjection, OnInsertedEntityListener onInsertedEntityListener) {
        this.objectionRepository.insertEntity(newObjection, onInsertedEntityListener);
    }

    public void updateBelief(boolean finishSignal, OnUpdatedEntityListener onUpdatedEntityListener) {
        List<ThinkingStyle> toDelete = this.getToDeleteThinkingStyles();
        List<ThinkingStyle> toInsert = this.getToInsertThinkingStyles();
        this.beliefRepository.updateBelief(super.getModifiableEntityCopy(), toDelete, toInsert, finishSignal, onUpdatedEntityListener);
    }

    public void deleteBelief(OnDeletedEntityListener onDeletedEntityListener) {
        super.deleteEntity(this.beliefRepository, onDeletedEntityListener);
    }

    private List<ThinkingStyle> getToDeleteThinkingStyles() {
        List<ThinkingStyle> oldSelectedThinkingStyles = this.thinkingStylesLiveData.getValue();
        List<ThinkingStyle> newSelectedThinkingStyles = this.getModifiableThinkingStylesCopy();
        List<ThinkingStyle> toDelete = new ArrayList<>();

        for (ThinkingStyle thinkingStyle : oldSelectedThinkingStyles) {
            if (!newSelectedThinkingStyles.contains(thinkingStyle)) {
                toDelete.add(thinkingStyle);
            }
        }

        return toDelete;
    }

    private List<ThinkingStyle> getToInsertThinkingStyles() {
        List<ThinkingStyle> oldSelectedThinkingStyles = this.thinkingStylesLiveData.getValue();
        List<ThinkingStyle> newSelectedThinkingStyles = this.getModifiableThinkingStylesCopy();
        List<ThinkingStyle> toInsert = new ArrayList<>();

        for (ThinkingStyle thinkingStyle : newSelectedThinkingStyles) {
            if (!oldSelectedThinkingStyles.contains(thinkingStyle)) {
                toInsert.add(thinkingStyle);
            }
        }

        return toInsert;
    }

    public boolean beliefWasChanged() {
        List<ThinkingStyle> selectedThinkingStyles = this.getThinkingStylesLiveData().getValue();
        List<ThinkingStyle> modifiableSelectedThinkingStylesCopy = this.getModifiableThinkingStylesCopy();
        return (
                super.entityWasChanged() ||
                        isListsEquals(selectedThinkingStyles, modifiableSelectedThinkingStylesCopy)
        );
    }

    private boolean isListsEquals(List<?> A, List<?> B) {
        if (A.size() != B.size()) {
            return true;
        }
        return !A.containsAll(B) || !B.containsAll(A);
    }
}
