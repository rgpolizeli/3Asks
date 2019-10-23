package com.rgp.asks.persistence.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.rgp.asks.persistence.AppRoomDatabase;
import com.rgp.asks.persistence.dao.ReactionDao;
import com.rgp.asks.persistence.entity.Reaction;

import java.util.List;

public class ReactionRepository extends EntityRepository<Reaction> {

    public ReactionRepository(@NonNull Application application) {
        super(AppRoomDatabase.getDatabase(application).reactionDao());
    }

    public LiveData<List<Reaction>> getReactionsForEpisode(int episodeId) {
        return ((ReactionDao) getDao()).getReactionsForEpisode(episodeId);
    }

    @Override
    public LiveData<Reaction> getEntityById(int entityId) {
        return ((ReactionDao) getDao()).getReactionById(entityId);
    }
}
