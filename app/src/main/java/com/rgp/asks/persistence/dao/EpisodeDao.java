package com.rgp.asks.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.rgp.asks.persistence.entity.Episode;

import java.util.List;

@Dao
public interface EpisodeDao extends EntityDao<Episode> {
    @Query("SELECT * FROM Episode ORDER BY id DESC")
    LiveData<List<Episode>> getAllEpisodes();

    @Query("SELECT * FROM Episode WHERE Episode.id=:entityId")
    LiveData<Episode> getEntityById(int entityId);
}
