package com.example.myapplication.persistence.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.myapplication.persistence.entity.ThinkingStyle;

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
