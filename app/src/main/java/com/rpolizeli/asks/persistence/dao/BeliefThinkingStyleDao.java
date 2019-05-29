package com.rpolizeli.asks.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rpolizeli.asks.persistence.entity.BeliefThinkingStyle;
import com.rpolizeli.asks.persistence.entity.ThinkingStyle;

import java.util.List;

@Dao
public interface BeliefThinkingStyleDao {

    @Insert
    void insert(BeliefThinkingStyle beliefThinkingStyle);

    @Delete
    int delete(BeliefThinkingStyle... beliefThinkingStyles);

    @Query("SELECT ThinkingStyle.thinkingStyle FROM BeliefThinkingStyle INNER JOIN ThinkingStyle ON ThinkingStyle.thinkingStyle = BeliefThinkingStyle.thinkingStyleId WHERE BeliefThinkingStyle.beliefId=:beliefId")
    LiveData<List<ThinkingStyle>> getThinkingStylesForBelief(final int beliefId);

}
