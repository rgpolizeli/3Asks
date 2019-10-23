package com.rgp.asks.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.rgp.asks.persistence.entity.BeliefThinkingStyle;
import com.rgp.asks.persistence.entity.ThinkingStyle;

import java.util.List;

@Dao
public interface BeliefThinkingStyleDao extends EntityDao<BeliefThinkingStyle> {
    @Query("SELECT ThinkingStyle.thinkingStyle FROM BeliefThinkingStyle INNER JOIN ThinkingStyle ON ThinkingStyle.thinkingStyle = BeliefThinkingStyle.thinkingStyleId WHERE BeliefThinkingStyle.beliefId=:beliefId")
    LiveData<List<ThinkingStyle>> getThinkingStylesForBelief(int beliefId);
}
