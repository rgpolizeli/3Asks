package com.rpolizeli.asks.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.annotation.NonNull;

import com.rpolizeli.asks.persistence.entity.Belief;

import java.util.List;

@Dao
public interface BeliefDao {

    @Insert
    long insert(@NonNull Belief belief);

    @Update
    int update(@NonNull Belief... belief);

    @Delete
    int delete(@NonNull Belief... belief);

    @Query("SELECT * " +
            "FROM Belief " +
            "WHERE Belief.episodeId=:episodeId ORDER BY id DESC")
    LiveData<List<Belief>> getBeliefsForEpisode(@NonNull final int episodeId);

    @Query("SELECT * " +
            "FROM Belief " +
            "WHERE Belief.id=:beliefId")
    LiveData<Belief> getBeliefById(@NonNull final int beliefId);

}
