package com.rgp.asks.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rgp.asks.persistence.entity.Episode;

import java.util.List;

@Dao
public interface EpisodeDao {

    @Insert
    long insert(Episode episode);

    @Update
    int update(Episode episode);

    @Delete
    int delete(Episode... episode);

    @Query("SELECT * " +
            "FROM Episode ORDER BY id DESC")
    LiveData<List<Episode>> getAllEpisodes();

    @Query("SELECT * FROM Episode WHERE Episode.id=:episodeId")
    LiveData<Episode> getEpisodeById(int episodeId);
}
