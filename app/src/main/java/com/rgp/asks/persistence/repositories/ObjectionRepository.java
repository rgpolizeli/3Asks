package com.rgp.asks.persistence.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.rgp.asks.persistence.AppRoomDatabase;
import com.rgp.asks.persistence.dao.ObjectionDao;
import com.rgp.asks.persistence.entity.Objection;

import java.util.List;

public class ObjectionRepository extends EntityRepository<Objection> {

    public ObjectionRepository(@NonNull Application application) {
        super(AppRoomDatabase.getDatabase(application).objectionDao());
    }

    public LiveData<List<Objection>> getObjectionsForBelief(int episodeId) {
        return ((ObjectionDao) getDao()).getObjectionsForBelief(episodeId);
    }

    @Override
    public LiveData<Objection> getEntityById(int entityId) {
        return ((ObjectionDao) getDao()).getObjectionById(entityId);
    }
}
