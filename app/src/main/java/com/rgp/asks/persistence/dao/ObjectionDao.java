package com.rgp.asks.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.rgp.asks.persistence.entity.Objection;

import java.util.List;

@Dao
public interface ObjectionDao extends EntityDao<Objection> {
    @Query("SELECT * FROM Objection WHERE Objection.beliefId=:beliefId ORDER BY id DESC")
    LiveData<List<Objection>> getObjectionsForBelief(final int beliefId);

    @Query("SELECT * FROM Objection WHERE Objection.id=:objectionId")
    LiveData<Objection> getObjectionById(int objectionId);
}
