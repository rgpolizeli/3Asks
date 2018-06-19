package com.example.myapplication.persistence.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;

import com.example.myapplication.persistence.entity.Belief;

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
