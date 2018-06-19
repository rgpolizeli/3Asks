package com.example.myapplication.persistence.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;

import com.example.myapplication.persistence.entity.Reaction;

import java.util.List;

@Dao
public interface ReactionDao {
    @Insert
    long insert(Reaction reaction);

    @Update
    int update(Reaction... reaction);

    @Delete
    int delete(Reaction... reaction);

    @Query("SELECT * " +
            "FROM Reaction " +
            "WHERE Reaction.episodeId=:episodeId ORDER BY id DESC")
    LiveData<List<Reaction>> getReactionsForEpisode(@NonNull final int episodeId);
}
