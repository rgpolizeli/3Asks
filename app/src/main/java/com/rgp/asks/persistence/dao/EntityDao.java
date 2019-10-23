package com.rgp.asks.persistence.dao;

import androidx.annotation.NonNull;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

public interface EntityDao<T> {
    @Insert
    long insert(@NonNull T entity);

    @Update
    int update(@NonNull T entity);

    @Delete
    int delete(@NonNull T entity);
}
