package com.rgp.asks.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rgp.asks.persistence.entity.Argument;

import java.util.List;

@Dao
public interface ArgumentDao {
    @Insert
    long insert(Argument argument);

    @Update
    int update(Argument... argument);

    @Delete
    int delete(Argument... argument);

    @Query("SELECT * FROM Argument WHERE Argument.beliefId=:beliefId ORDER BY id DESC")
    LiveData<List<Argument>> getArgumentsForBelief(final int beliefId);
}
