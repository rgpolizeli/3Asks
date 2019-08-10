package com.rgp.asks.persistence.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.rgp.asks.persistence.entity.ThinkingStyle;

@Dao
public interface ThinkingStyleDao {
    @Insert
    void insert(ThinkingStyle... thinkingStyle);
}
