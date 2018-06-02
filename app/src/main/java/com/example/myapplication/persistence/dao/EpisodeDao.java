package com.example.myapplication.persistence.dao;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.myapplication.persistence.entity.Episode;

import java.util.List;

@Dao
public interface EpisodeDao {

    @Insert
    long insert(Episode episode);

    @Update
    int update(Episode episode);

    @Delete
    void delete(Episode... episode);

    @Query("SELECT * " +
            "FROM Episode ORDER BY id DESC")
    LiveData<List<Episode>> getAllEpisodes();

    @Query("SELECT * FROM Episode WHERE Episode.id=:episodeId")
    LiveData<Episode> getEpisodeById(int episodeId);
}
