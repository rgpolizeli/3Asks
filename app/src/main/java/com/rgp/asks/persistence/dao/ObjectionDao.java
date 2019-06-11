package com.rgp.asks.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rgp.asks.persistence.entity.Objection;

import java.util.List;

@Dao
public interface ObjectionDao {

    @Insert
    long insert(Objection objection);

    @Update
    int update(Objection... objection);

    @Delete
    int delete(Objection... objection);

    @Query("SELECT * " +
            "FROM Objection " +
            "WHERE Objection.beliefId=:beliefId ORDER BY id DESC")
    LiveData<List<Objection>> getObjectionsForBelief(final int beliefId);

}
