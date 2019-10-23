package com.rgp.asks.persistence.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.rgp.asks.persistence.AppRoomDatabase;
import com.rgp.asks.persistence.dao.ArgumentDao;
import com.rgp.asks.persistence.entity.Argument;

import java.util.List;

public class ArgumentRepository extends EntityRepository<Argument> {

    public ArgumentRepository(@NonNull Application application) {
        super(AppRoomDatabase.getDatabase(application).argumentDao());
    }

    public LiveData<List<Argument>> getArgumentsForBelief(int episodeId) {
        return ((ArgumentDao) getDao()).getArgumentsForBelief(episodeId);
    }

    @Override
    public LiveData<Argument> getEntityById(int entityId) {
        return ((ArgumentDao) getDao()).getArgumentById(entityId);
    }
}
