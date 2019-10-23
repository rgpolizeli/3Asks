package com.rgp.asks.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.rgp.asks.persistence.entity.Belief;

import java.util.List;

@Dao
public interface BeliefDao extends EntityDao<Belief> {
    @Query("SELECT * FROM Belief WHERE Belief.episodeId=:episodeId ORDER BY id DESC")
    LiveData<List<Belief>> getBeliefsForEpisode(int episodeId);

    @Query("SELECT * FROM Belief WHERE Belief.id=:beliefId")
    LiveData<Belief> getBeliefById(int beliefId);
}
