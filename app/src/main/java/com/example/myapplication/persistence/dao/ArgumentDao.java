package com.example.myapplication.persistence.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.myapplication.persistence.entity.Argument;

import java.util.List;

@Dao
public interface ArgumentDao {

    @Insert
    long insert(Argument argument);

    @Update
    int update(Argument... argument);

    @Delete
    int delete(Argument... argument);

    @Query("SELECT * " +
            "FROM Argument " +
            "WHERE Argument.beliefId=:beliefId ORDER BY id DESC")
    LiveData<List<Argument>> getArgumentsForBelief(final int beliefId);

}
