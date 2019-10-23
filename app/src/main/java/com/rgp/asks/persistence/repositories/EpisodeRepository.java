package com.rgp.asks.persistence.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.rgp.asks.persistence.AppRoomDatabase;
import com.rgp.asks.persistence.dao.EpisodeDao;
import com.rgp.asks.persistence.entity.Episode;

import java.util.List;

public class EpisodeRepository extends EntityRepository<Episode> {

    public EpisodeRepository(Application application) {
        super(AppRoomDatabase.getDatabase(application).episodeDao());
    }

    public LiveData<List<Episode>> getAllEpisodes() {
        return ((EpisodeDao) getDao()).getAllEpisodes();
    }

    @Override
    public LiveData<Episode> getEntityById(int entityId) {
        return ((EpisodeDao) getDao()).getEntityById(entityId);
    }
}
