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
    private LiveData<Belief> belief;
    private Belief modifiableBeliefCopy;
    private LiveData<List<ThinkingStyle>> selectedThinkingStyles;
    private List<ThinkingStyle> modifiableSelectedThinkingStylesCopy;
    private LiveData<List<Argument>> arguments;
    private LiveData<List<Objection>> objections;
    private boolean beliefIsLoaded;
    private boolean selectedThinkingStylesIsLoaded;
    private Repository repository;

    public BeliefViewModel(@NonNull Application application) {
        super(application);
        this.repository = new Repository(application);

        this.beliefIsLoaded = false;
        this.selectedThinkingStylesIsLoaded = false;
    }

    public boolean getBeliefIsLoaded() {
        return this.beliefIsLoaded;
    }

    public void setBeliefIsLoaded(boolean value) {
        beliefIsLoaded = true;
    }

    public boolean getSelectedThinkingStylesIsLoaded() {
        return this.selectedThinkingStylesIsLoaded;
    }

    public void setSelectedThinkingStylesIsLoaded(boolean value) {
        selectedThinkingStylesIsLoaded = true;
    }

    public void loadBelief(@NonNull final int beliefId) {
        this.belief = this.repository.getBeliefById(beliefId);
        this.selectedThinkingStyles = this.repository.getThinkingStylesForBelief(beliefId);
        this.arguments = this.repository.getArgumentsForBelief(beliefId);
        this.objections = this.repository.getObjectionsForBelief(beliefId);
    }

    public LiveData<Belief> getBelief() {
        return belief;
    }

    public LiveData<List<ThinkingStyle>> getSelectedThinkingStyles() {
        return selectedThinkingStyles;
    }

    public LiveData<List<Argument>> getArguments() {
        return arguments;
    }

    public LiveData<List<Objection>> getObjections() {
        return objections;
    }

    public Belief getModifiableBeliefCopy() {
        return modifiableBeliefCopy;
    }

    public void setModifiableBeliefCopy(@NonNull Belief newBelief) {

        if (this.modifiableBeliefCopy != null && this.modifiableBeliefCopy.getId() == newBelief.getId()) {
            this.modifiableBeliefCopy = newBelief;
        } else {
            //not permit
        }

    }

    public List<ThinkingStyle> getModifiableSelectedThinkingStylesCopy() {
        return modifiableSelectedThinkingStylesCopy;
    }

    public void addUnhelpfulThinkingStyle(@NonNull final ThinkingStyle unhelpfulThinkingStyle) {
        this.getModifiableSelectedThinkingStylesCopy().add(unhelpfulThinkingStyle);
    }

    public void removeUnhelpfulThinkingStyle(@NonNull final ThinkingStyle unhelpfulThinkingStyle) {
        this.getModifiableSelectedThinkingStylesCopy().remove(unhelpfulThinkingStyle);
    }

    public void initModifiableBeliefCopy() {
        Belief b = this.belief.getValue();
        if (b != null) {
            this.modifiableBeliefCopy = new Belief(b.getBelief(), b.getEpisodeId());
            this.modifiableBeliefCopy.setId(b.getId());
        }
    }

    public void initModifiableSelectedThinkingStylesCopy() {
        List<ThinkingStyle> selectedThinkingStyles = this.selectedThinkingStyles.getValue();
        if (selectedThinkingStyles != null) {
            this.modifiableSelectedThinkingStylesCopy = new ArrayList<>(selectedThinkingStyles);
        }
    }

    public void createArgument(@NonNull final String newArgument) {
        Belief b = belief.getValue();
        if (b != null) {
            this.repository.createArgumentForBelief(b.getId(), newArgument);
        }
    }

    public void editArgument(@NonNull final Argument argument) {
        Belief b = this.belief.getValue();
        if (b != null && b.getId() == argument.getBeliefId()) {
            this.repository.editArgumentForBelief(argument);
        } else {
            //err
        }
    }

    public void removeArgument(@NonNull final Argument argument) {
        Belief b = this.belief.getValue();
        if (b != null && b.getId() == argument.getBeliefId()) {
            this.repository.deleteArgumentForBelief(argument);
        } else {
            //err
        }
    }

    public void createObjection(@NonNull final String newObjection) {
        Belief b = belief.getValue();
        if (b != null) {
            this.repository.createObjectionForBelief(b.getId(), newObjection);
        }
    }

    public void editObjection(@NonNull final Objection objection) {
        Belief b = this.belief.getValue();
        if (b != null && b.getId() == objection.getBeliefId()) {
            this.repository.editObjectionForBelief(objection);
        } else {
            //err
        }
    }

    public void removeObjection(@NonNull final Objection objection) {
        Belief b = this.belief.getValue();
        if (b != null && b.getId() == objection.getBeliefId()) {
            this.repository.deleteObjectionForBelief(objection);
        } else {
            //err
        }
    }

    private void saveBelief() {
        Belief b = this.belief.getValue();
        if (b != null && this.modifiableBeliefCopy.getId() == b.getId()) {
            List<ThinkingStyle> toDelete = this.getToDeleteThinkingStyles();
            List<ThinkingStyle> toInsert = this.getToInsertThinkingStyles();
            this.repository.saveBelief(this.modifiableBeliefCopy, toDelete, toInsert);
        }
    }

    private List<ThinkingStyle> getToDeleteThinkingStyles() {
        List<ThinkingStyle> oldSelectedThinkingStyles = this.selectedThinkingStyles.getValue();
        List<ThinkingStyle> newSelectedThinkingStyles = this.getModifiableSelectedThinkingStylesCopy();
        List<ThinkingStyle> toDelete = new ArrayList<>();

        for (ThinkingStyle thinkingStyle : oldSelectedThinkingStyles) {
            if (!newSelectedThinkingStyles.contains(thinkingStyle)) {
                toDelete.add(thinkingStyle);
            }
        }

        return toDelete;
    }

    private List<ThinkingStyle> getToInsertThinkingStyles() {
        List<ThinkingStyle> oldSelectedThinkingStyles = this.selectedThinkingStyles.getValue();
        List<ThinkingStyle> newSelectedThinkingStyles = this.getModifiableSelectedThinkingStylesCopy();
        List<ThinkingStyle> toInsert = new ArrayList<>();

        for (ThinkingStyle thinkingStyle : newSelectedThinkingStyles) {
            if (!oldSelectedThinkingStyles.contains(thinkingStyle)) {
                toInsert.add(thinkingStyle);
            }
        }

        return toInsert;
    }

    public void removeBelief() {
        Belief b = this.getBelief().getValue();
        if (b != null) {
            this.repository.deleteBelief(b);
        } else {
            //err
        }
    }

    public boolean beliefWasChanged() {
        Belief currentBelief = this.getBelief().getValue();
        Belief modifiedBelief = this.getModifiableBeliefCopy();

        List<ThinkingStyle> selectedThinkingStyles = this.getSelectedThinkingStyles().getValue();
        List<ThinkingStyle> modifiableSelectedThinkingStylesCopy = this.getModifiableSelectedThinkingStylesCopy();


        if (currentBelief != null && currentBelief.getId() == modifiedBelief.getId()) {
            if (!modifiedBelief.getBelief().isEmpty()) {
                return (
                        !currentBelief.getBelief().equals(modifiedBelief.getBelief()) ||
                                !equalsLists(selectedThinkingStyles, modifiableSelectedThinkingStylesCopy)
                );
            } else {
                return !equalsLists(selectedThinkingStyles, modifiableSelectedThinkingStylesCopy);
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

    private boolean equalsLists(List<?> A, List<?> B) {

        if (A.size() != B.size()) {
            return false;
        }

        return A.containsAll(B) && B.containsAll(A);
    }

}
