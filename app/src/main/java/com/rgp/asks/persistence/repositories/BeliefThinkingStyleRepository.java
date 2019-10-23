package com.rgp.asks.persistence.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.rgp.asks.persistence.AppRoomDatabase;
import com.rgp.asks.persistence.dao.BeliefThinkingStyleDao;
import com.rgp.asks.persistence.entity.BeliefThinkingStyle;
import com.rgp.asks.persistence.entity.ThinkingStyle;

import java.util.List;

public class BeliefThinkingStyleRepository extends EntityRepository<BeliefThinkingStyle> {

    public BeliefThinkingStyleRepository(Application application) {
        super(AppRoomDatabase.getDatabase(application).beliefThinkingStyleDao());
    }

    public LiveData<List<ThinkingStyle>> getThinkingStylesForBelief(int beliefId) {
        return ((BeliefThinkingStyleDao) getDao()).getThinkingStylesForBelief(beliefId);
    }

    @Override
    public LiveData<BeliefThinkingStyle> getEntityById(int entityId) {
        return null;
    }
}
