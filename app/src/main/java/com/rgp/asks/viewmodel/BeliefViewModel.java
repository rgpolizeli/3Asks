package com.rgp.asks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rgp.asks.persistence.Repository;
import com.rgp.asks.persistence.entity.Argument;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.persistence.entity.Objection;
import com.rgp.asks.persistence.entity.ThinkingStyle;

import java.util.ArrayList;
import java.util.List;

public class BeliefViewModel extends AndroidViewModel {
    private int beliefId;
    private LiveData<Belief> beliefLiveData;
    private boolean isBeliefFirstLoad;
    private Belief modifiableBeliefCopy;
    private LiveData<List<ThinkingStyle>> thinkingStylesLiveData;
    private boolean isThinkingStylesFirstLoad;
    private List<ThinkingStyle> modifiableThinkingStylesCopy;
    private LiveData<List<Argument>> argumentsLiveData;
    private LiveData<List<Objection>> objectionsLiveData;
    private Repository repository;

    public BeliefViewModel(@NonNull Application application) {
        super(application);
        this.repository = new Repository(application);
        this.isBeliefFirstLoad = true;
        this.isThinkingStylesFirstLoad = true;
    }

    public boolean isBeliefFirstLoad() {
        return this.isBeliefFirstLoad;
    }

    public void setIsBeliefFirstLoad(boolean is) {
        this.isBeliefFirstLoad = is;
    }

    public boolean isThinkingStylesFirstLoad() {
        return this.isThinkingStylesFirstLoad;
    }

    public void setIsThinkingStylesFirstLoad(boolean is) {
        this.isThinkingStylesFirstLoad = is;
    }

    public int getBeliefId() {
        return this.beliefId;
    }

    public void setBeliefId(int beliefId) {
        this.beliefId = beliefId;
    }

    public LiveData<Belief> getBeliefLiveData() {
        if (this.beliefLiveData == null) {
            loadBelief();
        }
        return this.beliefLiveData;
    }

    private void loadBelief() {
        this.beliefLiveData = this.repository.getBeliefById(beliefId);
    }

    public LiveData<List<ThinkingStyle>> getThinkingStylesLiveData() {
        if (this.thinkingStylesLiveData == null) {
            loadThinkingStyles();
        }
        return this.thinkingStylesLiveData;
    }

    private void loadThinkingStyles() {
        this.thinkingStylesLiveData = this.repository.getThinkingStylesForBelief(beliefId);
    }

    public LiveData<List<Argument>> getArgumentsLiveData() {
        if (this.argumentsLiveData == null) {
            loadArguments();
        }
        return argumentsLiveData;
    }

    private void loadArguments() {
        this.argumentsLiveData = this.repository.getArgumentsForBelief(this.beliefId);
    }

    public LiveData<List<Objection>> getObjectionsLiveData() {
        if (this.objectionsLiveData == null) {
            loadObjections();
        }
        return objectionsLiveData;
    }

    private void loadObjections() {
        this.objectionsLiveData = this.repository.getObjectionsForBelief(this.beliefId);
    }

    public void initModifiableBeliefCopy(@NonNull Belief belief) {
        this.modifiableBeliefCopy = new Belief(belief.getBelief(), belief.getEpisodeId());
        this.modifiableBeliefCopy.setId(belief.getId());
    }

    public Belief getModifiableBeliefCopy() {
        return modifiableBeliefCopy;
    }

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

    public void createArgument(@NonNull final String newArgument) {
        Belief b = beliefLiveData.getValue();
        if (b != null) {
            this.repository.createArgumentForBelief(b.getId(), newArgument);
        }
    }

    public void editArgument(@NonNull final Argument argument) {
        Belief b = this.beliefLiveData.getValue();
        if (b != null && b.getId() == argument.getBeliefId()) {
            this.repository.editArgumentForBelief(argument);
        } else {
            //err
        }
    }

    public void removeArgument(@NonNull final Argument argument) {
        Belief b = this.beliefLiveData.getValue();
        if (b != null && b.getId() == argument.getBeliefId()) {
            this.repository.deleteArgumentForBelief(argument);
        } else {
            //err
        }
    }

    public void createObjection(@NonNull final String newObjection) {
        Belief b = beliefLiveData.getValue();
        if (b != null) {
            this.repository.createObjectionForBelief(b.getId(), newObjection);
        }
    }

    public void editObjection(@NonNull final Objection objection) {
        Belief b = this.beliefLiveData.getValue();
        if (b != null && b.getId() == objection.getBeliefId()) {
            this.repository.editObjectionForBelief(objection);
        } else {
            //err
        }
    }

    public void removeObjection(@NonNull final Objection objection) {
        Belief b = this.beliefLiveData.getValue();
        if (b != null && b.getId() == objection.getBeliefId()) {
            this.repository.deleteObjectionForBelief(objection);
        } else {
            //err
        }
    }

    private void saveBelief() {
        Belief b = this.beliefLiveData.getValue();
        if (b != null && this.modifiableBeliefCopy.getId() == b.getId()) {
            List<ThinkingStyle> toDelete = this.getToDeleteThinkingStyles();
            List<ThinkingStyle> toInsert = this.getToInsertThinkingStyles();
            this.repository.saveBelief(this.modifiableBeliefCopy, toDelete, toInsert);
        }
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

    public void removeBelief() {
        Belief b = this.getBeliefLiveData().getValue();
        if (b != null) {
            this.repository.deleteBelief(b);
        } else {
            //err
        }
    }

    public boolean beliefWasChanged() {
        Belief currentBelief = this.getBeliefLiveData().getValue();
        Belief modifiedBelief = this.getModifiableBeliefCopy();

        List<ThinkingStyle> selectedThinkingStyles = this.getThinkingStylesLiveData().getValue();
        List<ThinkingStyle> modifiableSelectedThinkingStylesCopy = this.getModifiableThinkingStylesCopy();

        if (currentBelief != null && currentBelief.getId() == modifiedBelief.getId()) {
            if (!modifiedBelief.getBelief().isEmpty()) {
                return (
                        !currentBelief.getBelief().equals(modifiedBelief.getBelief()) ||
                                isListsEquals(selectedThinkingStyles, modifiableSelectedThinkingStylesCopy)
                );
            } else {
                return isListsEquals(selectedThinkingStyles, modifiableSelectedThinkingStylesCopy);
            }

        } else {
            //err
            return false;
        }
    }

    public void uncheckedSaveBelief() {
        if (beliefWasChanged()) {
            saveBelief();
        } else {
            //err
        }
    }

    public void checkedSaveBelief() {
        saveBelief();
    }

    private boolean isListsEquals(List<?> A, List<?> B) {
        if (A.size() != B.size()) {
            return true;
        }
        return !A.containsAll(B) || !B.containsAll(A);
    }

}
