package com.rpolizeli.asks.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.rpolizeli.asks.persistence.entity.ThinkingStyle;

import java.util.List;

@Dao
public interface ThinkingStyleDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(ThinkingStyle... thinkingStyle);

    @Delete
    void delete(ThinkingStyle... thinkingStyle);

    @Query("SELECT * " +
            "FROM ThinkingStyle")
    LiveData<List<ThinkingStyle>> getAllThinkingStyles();

}
