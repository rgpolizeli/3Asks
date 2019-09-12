package com.rgp.asks.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rgp.asks.persistence.entity.Reaction;

import java.util.List;

@Dao
public interface ReactionDao {
    @Insert
    long insert(Reaction reaction);

    @Update
    int update(Reaction... reaction);

    @Delete
    int delete(Reaction... reaction);

    @Query("SELECT * FROM Reaction WHERE Reaction.episodeId=:episodeId ORDER BY id DESC")
    LiveData<List<Reaction>> getReactionsForEpisode(int episodeId);

    @Query("SELECT * FROM Reaction WHERE Reaction.id=:reactionId")
    LiveData<Reaction> getReactionById(int reactionId);
}
