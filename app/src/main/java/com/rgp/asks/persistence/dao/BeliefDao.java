package com.rgp.asks.persistence.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rgp.asks.persistence.entity.Belief;

import java.util.List;

@Dao
public interface BeliefDao {
    @Insert
    long insert(@NonNull Belief belief);

    @Update
    int update(@NonNull Belief... belief);

    @Delete
    int delete(@NonNull Belief... belief);

    @Query("SELECT * FROM Belief WHERE Belief.episodeId=:episodeId ORDER BY id DESC")
    LiveData<List<Belief>> getBeliefsForEpisode(int episodeId);

    @Query("SELECT * FROM Belief WHERE Belief.id=:beliefId")
    LiveData<Belief> getBeliefById(int beliefId);
}
