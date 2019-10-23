package com.rgp.asks.persistence.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.rgp.asks.interfaces.OnUpdatedEntityListener;
import com.rgp.asks.persistence.AppRoomDatabase;
import com.rgp.asks.persistence.asynctask.UpdateAsyncTask;
import com.rgp.asks.persistence.asynctask.UpdateBeliefAsyncTask;
import com.rgp.asks.persistence.dao.BeliefDao;
import com.rgp.asks.persistence.dao.BeliefThinkingStyleDao;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.persistence.entity.ThinkingStyle;

import java.util.List;

public class BeliefRepository extends EntityRepository<Belief> {

    private BeliefThinkingStyleDao beliefThinkingStyleDao;

    public BeliefRepository(Application application) {
        super(AppRoomDatabase.getDatabase(application).beliefDao());
        this.beliefThinkingStyleDao = AppRoomDatabase.getDatabase(application).beliefThinkingStyleDao();
    }

    public void updateBelief(@NonNull final Belief newBelief, @NonNull final List<ThinkingStyle> toDeleteSelectedThinkingStyles, @NonNull final List<ThinkingStyle> toInsertSelectedThinkingStyles, boolean finishSignal, OnUpdatedEntityListener onUpdatedEntityListener) {
        new UpdateBeliefAsyncTask(
                getDao(),
                this.beliefThinkingStyleDao,
                toDeleteSelectedThinkingStyles,
                toInsertSelectedThinkingStyles,
                finishSignal,
                onUpdatedEntityListener
        ).execute(newBelief);
    }

    @Override
    public void updateEntity(Belief updatedEntity, boolean finishSignal, OnUpdatedEntityListener onUpdatedEntityListener) {
        new UpdateAsyncTask<>(getDao(), finishSignal, onUpdatedEntityListener).execute(updatedEntity);
    }

    @Override
    public LiveData<Belief> getEntityById(int entityId) {
        return ((BeliefDao) getDao()).getBeliefById(entityId);
    }

    public LiveData<List<Belief>> getBeliefsForEpisode(int episodeId) {
        return ((BeliefDao) getDao()).getBeliefsForEpisode(episodeId);
    }
}
