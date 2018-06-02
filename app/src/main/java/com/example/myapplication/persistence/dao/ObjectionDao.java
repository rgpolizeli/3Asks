package com.example.myapplication.persistence.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.myapplication.persistence.entity.Objection;

import java.util.List;

@Dao
public interface ObjectionDao {

    @Insert
    long insert(Objection objection);

    @Update
    void update(Objection... objection);

    @Delete
    void delete(Objection... objection);

    @Query("SELECT * " +
            "FROM Objection " +
            "WHERE Objection.beliefId=:beliefId ORDER BY id DESC")
    LiveData<List<Objection>> getObjectionsForBelief(final int beliefId);

}
