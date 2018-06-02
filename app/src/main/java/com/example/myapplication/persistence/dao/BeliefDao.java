package com.example.myapplication.persistence.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.myapplication.persistence.entity.Belief;
import com.example.myapplication.persistence.entity.Objection;

import java.util.List;

@Dao
public interface BeliefDao {

    @Insert
    long insert(Belief belief);

    @Update
    void update(Belief... belief);

    @Delete
    void delete(Belief... belief);

    @Query("SELECT * " +
            "FROM Belief " +
            "WHERE Belief.episodeId=:episodeId ORDER BY id DESC")
    LiveData<List<Belief>> getBeliefsForEpisode(final int episodeId);

}
