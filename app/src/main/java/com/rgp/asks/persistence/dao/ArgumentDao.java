package com.rgp.asks.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.rgp.asks.persistence.entity.Argument;

import java.util.List;

@Dao
public interface ArgumentDao extends EntityDao<Argument> {
    @Query("SELECT * FROM Argument WHERE Argument.beliefId=:beliefId ORDER BY id DESC")
    LiveData<List<Argument>> getArgumentsForBelief(final int beliefId);

    @Query("SELECT * FROM Argument WHERE Argument.id=:argumentId")
    LiveData<Argument> getArgumentById(int argumentId);
}
